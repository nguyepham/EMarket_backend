package nguye.emarket.backend.controller;

import jakarta.validation.Valid;
import nguye.emarket.backend.api.ProductApi;
import nguye.emarket.backend.model.ImagesUpdateResponse;
import nguye.emarket.backend.model.Product;
import nguye.emarket.backend.model.SuccessResponse;
import nguye.emarket.backend.service.implementation.BasicProductService;
import nguye.emarket.backend.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProductController implements ProductApi {

    private final BasicProductService basicProductService;

    public ProductController(BasicProductService basicProductService) {
        this.basicProductService = basicProductService;
    }

    @Override
    public ResponseEntity<Product> createProduct(
            @Valid @RequestBody Product newProduct) {
        SecurityUtil.authenticateUser(newProduct.getSellerUsername());
        return ResponseEntity.ok(basicProductService.createProduct(newProduct));
    }

    @Override
    public ResponseEntity<ImagesUpdateResponse> updateProductImage(
            @PathVariable("productId") Integer productId,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        basicProductService.authenticateUser(productId);

        return ResponseEntity.ok(new ImagesUpdateResponse(
                basicProductService.updateProductImage(productId, files)));
    }

    @Override
    public ResponseEntity<Product> getProduct(@PathVariable("productId") Integer productId) {
        return ResponseEntity.ok(basicProductService.getProduct(productId));
    }

    @Override
    public ResponseEntity<List<Product>> getProductsByCategory(
            @PathVariable("categoryName") String categoryName,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size,
            @RequestParam(value = "sortBy", required = true) String sortBy,
            @RequestParam(value = "sortDirection", required = true) String sortDirection) {
        return ResponseEntity.ok(basicProductService.getProductsByCategory(categoryName, page, size, sortBy, sortDirection));
    }

    @Override
    public ResponseEntity<List<Product>> getProductsBySeller(
            @PathVariable("username") String username,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size,
            @RequestParam(value = "sortBy", required = true) String sortBy,
            @RequestParam(value = "sortDirection", required = true) String sortDirection) {
        return ResponseEntity.ok(basicProductService.getProductsBySeller(username, page, size, sortBy, sortDirection));
    }

    @Override
    public ResponseEntity<Product> updateProduct(
            @PathVariable("productId") Integer productId,
            @Valid @RequestBody Product productUpdate
    ) {
        basicProductService.authenticateUser(productId);
        return ResponseEntity.ok(basicProductService.updateProduct(productId, productUpdate));
    }

    @Override
    public ResponseEntity<SuccessResponse> deleteProduct(@PathVariable("productId") Integer productId) {
        basicProductService.authenticateUser(productId);
        basicProductService.deleteProduct(productId);
        return ResponseEntity.ok(new SuccessResponse("Product deleted successfully"));
    }
}
