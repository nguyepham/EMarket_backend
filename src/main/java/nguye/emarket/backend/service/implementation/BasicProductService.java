package nguye.emarket.backend.service.implementation;

import nguye.emarket.backend.assembler.ProductAssembler;
import nguye.emarket.backend.entity.CategoryEntity;
import nguye.emarket.backend.entity.ProductEntity;
import nguye.emarket.backend.entity.ProductImageEntity;
import nguye.emarket.backend.entity.UserEntity;
import nguye.emarket.backend.exception.FileUploadException;
import nguye.emarket.backend.exception.ResourceNotFoundException;
import nguye.emarket.backend.exception.ResourceType;
import nguye.emarket.backend.model.Category;
import nguye.emarket.backend.model.Product;
import nguye.emarket.backend.repository.CategoryRepository;
import nguye.emarket.backend.repository.ProductImageRepository;
import nguye.emarket.backend.repository.ProductRepository;
import nguye.emarket.backend.repository.UserRepository;
import nguye.emarket.backend.service.ProductService;
import nguye.emarket.backend.util.FileUtil;
import nguye.emarket.backend.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static nguye.emarket.backend.util.FileUtil.PRODUCT_UPLOAD_DIR;

@Service
public class BasicProductService implements ProductService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BasicCategoryService basicCategoryService;
    private final ProductImageRepository productImageRepository;
    private final ProductAssembler assembler;

    public BasicProductService(UserRepository userRepository,
                               CategoryRepository categoryRepository,
                               ProductRepository productRepository,
                               BasicCategoryService basicCategoryService,
                               ProductImageRepository productImageRepository,
                               ProductAssembler assembler) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.basicCategoryService = basicCategoryService;
        this.productImageRepository = productImageRepository;
        this.assembler = assembler;
    }

    @Override
    public void authenticateUser(int productId) {
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.PRODUCT)
        );
        SecurityUtil.authenticateUser(productEntity.getSeller().getUsername());
    }

    @Transactional
    @Override
    public Product createProduct(Product newProduct) {

        ProductEntity productEntity = savetoDatabase(newProduct);

        return assembler.toModel(productEntity);
    }

    private ProductEntity savetoDatabase(Product newProduct) {

        UserEntity sellerEntity = userRepository.findByUsername(newProduct.getSellerUsername()).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.USER)
        );

        List<Integer> categoryIds = new ArrayList<>();
        for (Category category : newProduct.getCategories()) {
            if (category.getId() == null) {
                CategoryEntity categoryEntity = basicCategoryService.createCategory(category);
                categoryIds.add(categoryEntity.getId());
            } else {
                categoryIds.add(category.getId());
            }
        }

        List<CategoryEntity> categories = (List<CategoryEntity>) categoryRepository.findAllById(categoryIds);

        ProductEntity productEntity = new ProductEntity(
                sellerEntity,
                newProduct.getName(),
                newProduct.getUnitPrice(),
                new Timestamp(newProduct.getUpdatedAt().getTime()),
                newProduct.getShortDescription(),
                newProduct.getQtyInStock());

        productEntity.getCategories().clear();
        productEntity.getCategories().addAll(categories);

        productRepository.save(productEntity);
        return productEntity;
    }

    @Transactional
    @Override
    public List<String> updateProductImage(int productId, List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();
        if (files == null || files.isEmpty()) {
            return imageUrls;
        }
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.PRODUCT)
        );

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            try {
                String filename = UUID.randomUUID() + "-" + productId + "-product-image" + FileUtil.getFileExtension(
                        Objects.requireNonNull(file.getOriginalFilename()));
                Path filepath = Paths.get(PRODUCT_UPLOAD_DIR, filename);
                Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

                String imageUrl = "/products/" + filename;
                imageUrls.add(imageUrl);

            } catch (IOException e) {
                throw new FileUploadException(file.getOriginalFilename());
            }
        }
        if (!imageUrls.isEmpty()) {
            productEntity.setCoverImageUrl(imageUrls.get(0));
        } else {
            productEntity.setCoverImageUrl(null);
        }

        for (ProductImageEntity image : productEntity.getImages()) {
            Path oldPath = Paths.get(PRODUCT_UPLOAD_DIR, Paths.get(image.getImageUrl()).getFileName().toString());
            try {
                Files.deleteIfExists(oldPath);
            } catch (IOException ignored) {}
        }
        productEntity.getImages().clear();

        for (String imageUrl : imageUrls) {
            productEntity.getImages().add(new ProductImageEntity(productEntity, imageUrl));

        }
        productRepository.save(productEntity);
        return imageUrls;
    }

    @Override
    public Product getProduct(int productId) {
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.PRODUCT)
        );
        return assembler.toModel(productEntity);
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName, int page, int size, String sortBy, String sortDirection) {
        CategoryEntity categoryEntity = categoryRepository.findByName(categoryName).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.CATEGORY)
        );

        Pageable pageable = (Objects.equals(sortDirection, "asc") ?
                PageRequest.of(page, size, Sort.by(sortBy).ascending()) :
                PageRequest.of(page, size, Sort.by(sortBy).descending()));

        Page<ProductEntity> productPage = productRepository.findByCategory(
                categoryEntity.getId(),
                pageable
        );
        return assembler.toListModel(productPage.getContent());
    }

    @Override
    public List<Product> getProductsBySeller(String username, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = (Objects.equals(sortDirection, "asc") ?
                PageRequest.of(page, size, Sort.by(sortBy).ascending()) :
                PageRequest.of(page, size, Sort.by(sortBy).descending()));
        Page<ProductEntity> productPage = productRepository.findBySeller(
                username,
                pageable
        );
        return assembler.toListModel(productPage.getContent());
    }

    @Override
    public Product updateProduct(int productId, Product productUpdate) {
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.PRODUCT)
        );

        if (productUpdate.getName() != null) {
            productEntity.setName(productUpdate.getName());
        }
        if (productUpdate.getUnitPrice() != null) {
            productEntity.setUnitPrice(productUpdate.getUnitPrice());
        }
        if (productUpdate.getCategories() != null) {
            productEntity.getCategories().clear();

            List<Integer> categoryIds = new ArrayList<>();
            for (Category category : productUpdate.getCategories()) {
                if (category.getId() != null) {
                    categoryIds.add(category.getId());
                } else {
                    CategoryEntity categoryEntity = basicCategoryService.createCategory(category);
                    categoryIds.add(categoryEntity.getId());
                }
            }
            List<CategoryEntity> categories = (List<CategoryEntity>) categoryRepository.findAllById(categoryIds);
            productEntity.getCategories().addAll(categories);
        }

        if (productUpdate.getShortDescription() != null) {
            productEntity.setShortDescription(productUpdate.getShortDescription());
        }
        if (productUpdate.getDescription() != null) {
            productEntity.setDescription(productUpdate.getDescription());
        }
        if (productUpdate.getQtyInStock() != null) {
            productEntity.setQtyInStock(productUpdate.getQtyInStock());
        }
        if (productUpdate.getUpdatedAt() != null) {
            productEntity.setUpdatedAt(new Timestamp(productUpdate.getUpdatedAt().getTime()));
        }
        productRepository.save(productEntity);
        return assembler.toModel(productEntity);
    }

    @Transactional
    @Override
    public void deleteProduct(int productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            productImageRepository.deleteByProductId(productId);
            List<CategoryEntity> unusedCategories = categoryRepository.findUnusedCategories();
            categoryRepository.deleteAll(unusedCategories);
        }
    }
}
