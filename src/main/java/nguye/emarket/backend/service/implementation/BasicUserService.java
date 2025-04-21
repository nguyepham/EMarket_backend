package nguye.emarket.backend.service.implementation;

import nguye.emarket.backend.assembler.ProfileAssembler;
import nguye.emarket.backend.assembler.UserAssembler;
import nguye.emarket.backend.entity.AddressEntity;
import nguye.emarket.backend.entity.PasswordEntity;
import nguye.emarket.backend.entity.ProfileEntity;
import nguye.emarket.backend.entity.UserEntity;
import nguye.emarket.backend.exception.*;
import nguye.emarket.backend.model.*;
import nguye.emarket.backend.repository.AddressRepository;
import nguye.emarket.backend.repository.PasswordRepository;
import nguye.emarket.backend.repository.ProfileRepository;
import nguye.emarket.backend.repository.UserRepository;
import nguye.emarket.backend.service.UserService;
import nguye.emarket.backend.util.FileUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

import static nguye.emarket.backend.util.FileUtil.AVATAR_UPLOAD_DIR;
import static nguye.emarket.backend.util.FileUtil.UPLOAD_DIR;

@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final AddressRepository addressRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAssembler userAssembler;
    private final ProfileAssembler profileAssembler;

    public BasicUserService(UserRepository userRepository,
                            PasswordRepository passwordRepository,
                            AddressRepository addressRepository,
                            ProfileRepository profileRepository,
                            PasswordEncoder passwordEncoder,
                            UserAssembler userAssembler,
                            ProfileAssembler profileAssembler) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.addressRepository = addressRepository;
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
        if (newUser.getEmail() != null && profileRepository.existsByEmail(newUser.getEmail())) {
            throw new ResourceAlreadyExistException(ResourceType.EMAIL);
        }

        UserEntity userEntity = new UserEntity(newUser.getUsername());
        PasswordEntity passwordEntity = new PasswordEntity(
                userEntity,
                newUser.getPassword().getText(),
                new Timestamp(newUser.getPassword().getUpdatedAt().getTime()));
        AddressEntity addressEntity = new AddressEntity(
                userEntity,
                newUser.getAddress().getProvince(),
                newUser.getAddress().getDistrict(),
                newUser.getAddress().getStreetAndNumber());

        ProfileEntity profileEntity = new ProfileEntity(userEntity, newUser.getAge());

        userRepository.save(userEntity);
        passwordRepository.save(passwordEntity);
        addressRepository.save(addressEntity);
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
        AddressEntity addressEntity = addressRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.ADDRESS));

        return profileAssembler.toModel(profileEntity, addressEntity);
    }

    @Transactional
    @Override
    public UserUpdate updateDetails(String username, UserUpdate details) {

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.USER));
        int userId = userEntity.getId();
        ProfileEntity profileEntity = profileRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.PROFILE));
        AddressEntity addressEntity = addressRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.ADDRESS));
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
            addressEntity.setProvince(details.getAddress().getProvince());
            addressEntity.setDistrict(details.getAddress().getDistrict());
            addressEntity.setStreetAndNumber(details.getAddress().getStreetAndNumber());

            Address userAddress = new Address()
                    .province(addressEntity.getProvince())
                    .district(addressEntity.getDistrict())
                    .streetAndNumber(addressEntity.getStreetAndNumber());
            newDetails.setAddress(userAddress);
        }

        profileRepository.save(profileEntity);
        addressRepository.save(addressEntity);

        return newDetails;
    }

    @Transactional
    @Override
    public void updatePassword(String username, UpdatePasswordRequest request) {

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

    @Transactional
    @Override
    public String updateAvatar(String username, MultipartFile file) throws FileUploadException {

        String imageUrl = null;
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.USER));
        try {
            String oldImageUrl = userEntity.getImageUrl();
            if (oldImageUrl != null && !oldImageUrl.isBlank()) {
                Path oldFilePath = Paths.get(AVATAR_UPLOAD_DIR, Paths.get(oldImageUrl).getFileName().toString());
                try {
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException e) {
                    throw new FileUploadException(file.getOriginalFilename());
                }
            }

            String filename = UUID.randomUUID() + "-" + username + "-avatar" + FileUtil.getFileExtension(
                    Objects.requireNonNull(file.getOriginalFilename()));
            Path filepath = Paths.get(AVATAR_UPLOAD_DIR, filename);
            Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

            imageUrl = "/avatars/" + filename;
            userEntity.setImageUrl(imageUrl);
            userRepository.save(userEntity);
        } catch (IOException e) {
            throw new FileUploadException(file.getOriginalFilename());
        }
        return imageUrl;
    }

    @Transactional
    @Override
    public void deleteUser(String username) {

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.USER));
        String imageUrl = userEntity.getImageUrl();
        if (imageUrl != null) {
            Path filepath = Paths.get(UPLOAD_DIR, imageUrl);
            try {
                Files.deleteIfExists(filepath);
            } catch (IOException e) {
                throw new ResourceNotFoundException(ResourceType.IMAGE);
            }
        }
        userRepository.deleteByUsername(username);
    }

    @Transactional
    @Override
    public void deleteAvatar(String username) {

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(ResourceType.USER));

        String imageUrl = userEntity.getImageUrl();
        if (imageUrl != null) {
            Path filepath = Paths.get(UPLOAD_DIR, imageUrl);
            try {
                Files.deleteIfExists(filepath);
            } catch (IOException e) {
                throw new ResourceNotFoundException(ResourceType.IMAGE);
            }
            userEntity.setImageUrl(null);
            userRepository.save(userEntity);
        }
    }
}
