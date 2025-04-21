package nguye.emarket.backend.repository;

import nguye.emarket.backend.entity.ProductImageEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductImageRepository extends CrudRepository<ProductImageEntity, Integer> {
    void deleteByProductId(int productId);
}
