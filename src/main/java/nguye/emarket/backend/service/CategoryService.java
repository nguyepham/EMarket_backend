package nguye.emarket.backend.service;

import nguye.emarket.backend.entity.CategoryEntity;
import nguye.emarket.backend.model.Category;

import java.util.List;

public interface CategoryService {

    CategoryEntity createCategory(Category newCategory);

    List<Category> getAllCategories();
}
