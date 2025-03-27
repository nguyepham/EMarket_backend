package nguye.emarket.backend.repository;

import nguye.emarket.backend.entity.PasswordEntity;
import org.springframework.data.repository.CrudRepository;

public interface PasswordRepository extends CrudRepository<PasswordEntity, Integer> {
}
