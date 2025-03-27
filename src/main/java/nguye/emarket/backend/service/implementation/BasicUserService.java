package nguye.emarket.backend.service.implementation;

import nguye.emarket.backend.assembler.ProfileAssembler;
import nguye.emarket.backend.assembler.UserAssembler;
import nguye.emarket.backend.authentication.SecurityUser;
import nguye.emarket.backend.entity.PasswordEntity;
import nguye.emarket.backend.entity.ProfileEntity;
import nguye.emarket.backend.entity.UserAddressEntity;
import nguye.emarket.backend.entity.UserEntity;
import nguye.emarket.backend.exception.AccessDeniedException;
import nguye.emarket.backend.exception.ResourceAlreadyExistException;
import nguye.emarket.backend.exception.ResourceNotFoundException;
import nguye.emarket.backend.exception.ResourceType;
import nguye.emarket.backend.model.*;
import nguye.emarket.backend.repository.PasswordRepository;
import nguye.emarket.backend.repository.ProfileRepository;
import nguye.emarket.backend.repository.UserAddressRepository;
import nguye.emarket.backend.repository.UserRepository;
import nguye.emarket.backend.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final UserAddressRepository userAddressRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAssembler userAssembler;
    private final ProfileAssembler profileAssembler;

    public BasicUserService(UserRepository userRepository, PasswordRepository passwordRepository,
                            UserAddressRepository userAddressRepository,
                            ProfileRepository profileRepository, PasswordEncoder passwordEncoder, UserAssembler userAssembler,
                            ProfileAssembler profileAssembler) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.userAddressRepository = userAddressRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAssembler = userAssembler;
        this.profileAssembler = profileAssembler;
    }

    @Transactional
    @Override
    public void createUser(UserCreate newUser) {

        if (userRepository.existsByUsername(newUser.getUsername())) {
            throw new ResourceAlreadyExistException(ResourceType.USERNAME);
        }
        if (profileRepository.existsByEmail(newUser.getEmail())) {
            throw new ResourceAlreadyExistException(ResourceType.EMAIL);
        }

        UserEntity userEntity = new UserEntity(newUser.getUsername());
        PasswordEntity passwordEntity = new PasswordEntity(
                userEntity,
                passwordEncoder.encode(newUser.getPassword().getText()),
                new Timestamp(newUser.getPassword().getUpdatedAt().getTime()));
        UserAddressEntity userAddressEntity = new UserAddressEntity(
                userEntity,
                newUser.getAddress().getProvince(),
                newUser.getAddress().getDistrict(),
                newUser.getAddress().getStreetAndNumber());

        ProfileEntity profileEntity = new ProfileEntity(userEntity, newUser.getAge());

        userRepository.save(userEntity);
        passwordRepository.save(passwordEntity);
        userAddressRepository.save(userAddressEntity);
        profileRepository.save(profileEntity);
    }

    @Override
    public User getUser(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.USER));
        return userAssembler.toModel(userEntity);
    }

    @Override
    public Profile getDetails(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.USER));
        int userId = userEntity.getId();

        ProfileEntity profileEntity = profileRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.PROFILE));
        UserAddressEntity userAddressEntity = userAddressRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.USER_ADDRESS));

        return profileAssembler.toModel(profileEntity, userAddressEntity);
    }

    @Transactional
    @Override
    public UserUpdate updateDetails(String username, UserUpdate details) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        if (!securityUser.getUsername().equals(username)) {
            throw new AccessDeniedException();
        }

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.USER));
        int userId = userEntity.getId();
        ProfileEntity profileEntity = profileRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.PROFILE));
        UserAddressEntity userAddressEntity = userAddressRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.USER_ADDRESS));
        UserUpdate newDetails = new UserUpdate();

        if (details.getFirstName() != null) {
            profileEntity.setFirstName(details.getFirstName());
        }
        if (profileEntity.getFirstName() != null) {
            newDetails.setFirstName(profileEntity.getFirstName());
        }

        if (details.getLastName() != null) {
            profileEntity.setLastName(details.getLastName());
        }
        if (profileEntity.getLastName() != null) {
            newDetails.setLastName(profileEntity.getLastName());
        }

        if (details.getEmail() != null) {
            profileEntity.setEmail(details.getEmail());
        }
        if (profileEntity.getEmail() != null) {
            newDetails.setEmail(profileEntity.getEmail());
        }

        if (details.getAge() != null) {
            profileEntity.setAge(details.getAge());
        }
        if (profileEntity.getAge() != 0) {
            newDetails.setAge(profileEntity.getAge());
        }

        if (details.getGender() != null) {
            profileEntity.setGender(details.getGender().getValue());
        }
        if (profileEntity.getGender() != null) {
            newDetails.setGender(UserUpdate.GenderEnum.valueOf(profileEntity.getGender()));
        }

        if (details.getAddress() != null) {
            userAddressEntity.setProvince(details.getAddress().getProvince());
            userAddressEntity.setDistrict(details.getAddress().getDistrict());
            userAddressEntity.setStreetAndNumber(details.getAddress().getStreetAndNumber());

            Address userAddress = new Address()
                    .province(userAddressEntity.getProvince())
                    .district(userAddressEntity.getDistrict())
                    .streetAndNumber(userAddressEntity.getStreetAndNumber());
            newDetails.setAddress(userAddress);
        }

        profileRepository.save(profileEntity);
        userAddressRepository.save(userAddressEntity);

        return newDetails;
    }

    @Transactional
    @Override
    public void updatePassword(String username, UpdatePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        if (!securityUser.getUsername().equals(username)) {
            throw new AccessDeniedException();
        }
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.USER));
        int userId = userEntity.getId();

        PasswordEntity passwordEntity = passwordRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.PASSWORD));
        if (!passwordEncoder.matches(request.getOldPassword(), passwordEntity.getText())) {
            throw new AccessDeniedException();
        }

        passwordEntity.setText(request.getNewPassword().getText());
        passwordEntity.setUpdatedAt(new Timestamp(request.getNewPassword().getUpdatedAt().getTime()));
        passwordRepository.save(passwordEntity);
    }

    @Override
    public void deleteUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        if (!securityUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                && !securityUser.getUsername().equals(username)) {
            throw new AccessDeniedException();
        }
        if (userRepository.existsByUsername(username)) {
            userRepository.deleteByUsername(username);
        }
    }


//    private void getProfileEntity(UserCreate newUser, UserEntity userEntity) {
//        ProfileEntity profileEntity =
//        if (newUser.getFirstName() != null) {
//            profileEntity.setFirstName(newUser.getFirstName());
//        }
//        if (newUser.getLastName() != null) {
//            profileEntity.setLastName(newUser.getLastName());
//        }
//        if (newUser.getEmail() != null) {
//            profileEntity.setEmail(newUser.getEmail());
//        }
//        if (newUser.getGender() != null) {
//            profileEntity.setGender(newUser.getGender());
//        }
//        return profileEntity;
//    }
}
