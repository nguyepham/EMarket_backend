package nguye.emarket.backend.service;

import nguye.emarket.backend.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    void authenticateUser(int productId);

    Product createProduct(Product newProduct);

    List<String> updateProductImage(int productId, List<MultipartFile> files);

    Product getProduct(int productId);

    List<Product> getProductsByCategory(String categoryName, int page, int size, String sortBy, String sortDirection);

    List<Product> getProductsBySeller(String username, int page, int size, String sortBy, String sortDirection);

    Product updateProduct(int productId, Product productUpdate);

    void deleteProduct(int productId);
}
