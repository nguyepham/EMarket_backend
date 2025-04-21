package nguye.emarket.backend.assembler;

import nguye.emarket.backend.controller.ProductController;
import nguye.emarket.backend.entity.CategoryEntity;
import nguye.emarket.backend.model.Category;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Component
public class CategoryAssembler extends
        RepresentationModelAssemblerSupport<CategoryEntity, Category> {

    public CategoryAssembler() {
        super(ProductController.class, Category.class);
    }

    @Override
    public Category toModel(CategoryEntity entity) {
        Category category = new Category(
                entity.getName(),
                entity.getLevel() > 0 ? entity.getParentName() : "root");

        category.setId(entity.getId());
        category.setLevel(entity.getLevel());
        category.setPath(entity.getPath());

        return category;
//                .add(linkTo(methodOn(ProductController.class).getDetails(entity.getId())).withSelfRel());
    }

    public List<Category> toListModel(Iterable<CategoryEntity> entities) {
        if (Objects.isNull(entities)) {
            return List.of();
        }
        return StreamSupport.stream(entities.spliterator(), false).map(this::toModel)
                .collect(toList());
    }
}
