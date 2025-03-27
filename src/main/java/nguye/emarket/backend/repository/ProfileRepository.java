package nguye.emarket.backend.repository;

import nguye.emarket.backend.entity.ProfileEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer> {

    boolean existsByEmail(String email);
}
