package nguye.emarket.backend.controller;

import nguye.emarket.backend.api.CategoryApi;
import nguye.emarket.backend.model.Category;
import nguye.emarket.backend.service.implementation.BasicCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController implements CategoryApi {

    private final BasicCategoryService basicCategoryService;

    public CategoryController(BasicCategoryService basicCategoryService) {
        this.basicCategoryService = basicCategoryService;
    }

    @Override
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(basicCategoryService.getAllCategories());
    }
}
