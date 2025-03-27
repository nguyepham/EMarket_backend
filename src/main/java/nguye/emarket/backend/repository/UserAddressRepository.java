package nguye.emarket.backend.repository;

import nguye.emarket.backend.entity.UserAddressEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserAddressRepository extends CrudRepository<UserAddressEntity, Integer> {
}
