package nguye.emarket.backend.repository;

import nguye.emarket.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    @Modifying
    @Query("DELETE FROM UserEntity u WHERE u.username = :username")
    void deleteByUsername(@Param("username") String username);

}
