package nguye.emarket.backend.service.implementation;

import nguye.emarket.backend.assembler.CategoryAssembler;
import nguye.emarket.backend.entity.CategoryEntity;
import nguye.emarket.backend.exception.ResourceAlreadyExistException;
import nguye.emarket.backend.exception.ResourceNotFoundException;
import nguye.emarket.backend.exception.ResourceType;
import nguye.emarket.backend.model.Category;
import nguye.emarket.backend.repository.CategoryRepository;
import nguye.emarket.backend.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BasicCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryAssembler assembler;

    public BasicCategoryService(CategoryRepository categoryRepository, CategoryAssembler assembler) {
        this.categoryRepository = categoryRepository;
        this.assembler = assembler;
    }

    @Transactional
    @Override
    public CategoryEntity createCategory(Category newCategory) {

        if (categoryRepository.existsByName(newCategory.getName())) {
            throw new ResourceAlreadyExistException(ResourceType.CATEGORY);
        }
        CategoryEntity parentCategory = categoryRepository.findByName(newCategory.getParentName()).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.CATEGORY)
        );

        CategoryEntity categoryEntity = new CategoryEntity(
                newCategory.getName(),
                parentCategory.getLevel() + 1,
                parentCategory.getPath() + newCategory.getName() + "/",
                parentCategory.getName());
        return categoryRepository.save(categoryEntity);
    }

    @Override
    public List<Category> getAllCategories() {
        Iterable<CategoryEntity> categories = categoryRepository.findAll();

        return StreamSupport.stream(categories.spliterator(), false)
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }
}
