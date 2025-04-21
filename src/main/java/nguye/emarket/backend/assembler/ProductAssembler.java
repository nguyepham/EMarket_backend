package nguye.emarket.backend.assembler;

import nguye.emarket.backend.controller.ProductController;
import nguye.emarket.backend.entity.ProductEntity;
import nguye.emarket.backend.model.Product;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductAssembler extends
        RepresentationModelAssemblerSupport<ProductEntity, Product> {

    private final UserAssembler userAssembler;
    private final CategoryAssembler categoryAssembler;

    public ProductAssembler(UserAssembler userAssembler, CategoryAssembler categoryAssembler) {
        super(ProductController.class, Product.class);
        this.userAssembler = userAssembler;
        this.categoryAssembler = categoryAssembler;
    }

    @Override
    public Product toModel(ProductEntity entity) {
        Product product = new Product()
                .id(entity.getId())
                .name(entity.getName())
                .unitPrice(entity.getUnitPrice())
                .shortDescription(entity.getShortDescription())
                .qtyInStock(entity.getQtyInStock())
                .updatedAt(entity.getUpdatedAt())
                .seller(userAssembler.toModel(entity.getSeller()))
                .categories(categoryAssembler.toListModel(entity.getCategories()));

        if (entity.getDescription() != null) {
            product.setDescription(entity.getDescription());
        }
        if (entity.getCoverImageUrl() != null) {
            product.setCoverImageUrl(entity.getCoverImageUrl());
        }

        return product
                .add(linkTo(methodOn(ProductController.class).getProduct(entity.getId())).withSelfRel())
                .add(linkTo(methodOn(ProductController.class).deleteProduct(entity.getId())).withRel("delete"));
    }

    public List<Product> toListModel(Iterable<ProductEntity> entities) {
        if (Objects.isNull(entities)) {
            return List.of();
        }
        return StreamSupport.stream(entities.spliterator(), false).map(this::toModel)
                .collect(toList());
    }
}
