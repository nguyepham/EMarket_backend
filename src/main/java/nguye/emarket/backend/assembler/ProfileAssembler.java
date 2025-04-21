package nguye.emarket.backend.assembler;

import nguye.emarket.backend.controller.UserController;
import nguye.emarket.backend.entity.AddressEntity;
import nguye.emarket.backend.entity.ProfileEntity;
import nguye.emarket.backend.entity.UserEntity;
import nguye.emarket.backend.exception.ResourceNotFoundException;
import nguye.emarket.backend.exception.ResourceType;
import nguye.emarket.backend.model.Address;
import nguye.emarket.backend.model.Profile;
import nguye.emarket.backend.repository.UserRepository;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProfileAssembler extends
        RepresentationModelAssemblerSupport<ProfileEntity, Profile> {

    private final UserRepository userRepository;
    
    public ProfileAssembler(UserRepository userRepository) {
        super(UserController.class, Profile.class);
        this.userRepository = userRepository;
    }

    public Profile toModel(ProfileEntity entity, AddressEntity addressEntity) {

        Profile profile = new Profile(entity.getAge(), Profile.AuthorityEnum.fromValue(entity.getAuthority()));
        UserEntity userEntity = userRepository.findById(entity.getId()).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.USER));

        if (entity.getFirstName() != null) {
            profile.setFirstName(entity.getFirstName());
        }
        if (entity.getLastName() != null) {
            profile.setLastName(entity.getLastName());
        }
        if (entity.getEmail() != null) {
            profile.setEmail(entity.getEmail());
        }
        if (entity.getGender() != null) {
            profile.setGender(Profile.GenderEnum.fromValue(entity.getGender()));
        }

        Address userAddress = new Address()
                .province(addressEntity.getProvince())
                .district(addressEntity.getDistrict())
                .streetAndNumber(addressEntity.getStreetAndNumber());

        return profile
                .address(userAddress)
                .add(linkTo(methodOn(UserController.class).getDetails(userEntity.getUsername())).withSelfRel())
                .add(linkTo(UserController.class)
                        .slash("api/v1/user")
                        .slash(userEntity.getUsername())
                        .slash("details")
                        .withRel("update")) // Manually constructing the updateDetails link
                .add(linkTo(UserController.class)
                        .slash("api/v1/user")
                        .slash(userEntity.getUsername())
                        .slash("password")
                        .withRel("update_password")) // Manually constructing the updatePassword link
                .add(linkTo(methodOn(UserController.class).deleteUser(userEntity.getUsername())).withRel("delete"));
    }

    @Override
    public Profile toModel(ProfileEntity entity) {
        return null;
    }
}
