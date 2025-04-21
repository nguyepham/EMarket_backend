package nguye.emarket.backend.repository;

import nguye.emarket.backend.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<ProductEntity, Integer> {

    @Query("SELECT p FROM ProductEntity p JOIN p.categories c WHERE c.id = :categoryId")
    Page<ProductEntity> findByCategory(@Param("categoryId") int categoryId, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.seller.username = :username")
    Page<ProductEntity> findBySeller(@Param("username") String username, Pageable pageable);
}
