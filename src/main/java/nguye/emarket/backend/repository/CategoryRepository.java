package nguye.emarket.backend.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import nguye.emarket.backend.entity.CategoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {

    boolean existsByName(@NotNull @Size(max = 50) String name);

    Optional<CategoryEntity> findByName(@NotNull @Size(max = 50) String name);

    @Query("SELECT c FROM CategoryEntity c WHERE c.products IS EMPTY")
    List<CategoryEntity> findUnusedCategories();

}
