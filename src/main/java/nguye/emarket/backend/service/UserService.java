package nguye.emarket.backend.service;

import nguye.emarket.backend.exception.FileUploadException;
import nguye.emarket.backend.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    void createUser(UserCreate newUser);

    User getUser(String username);

    Profile getDetails(String username);

    UserUpdate updateDetails(String username, UserUpdate details);

    void updatePassword(String username, UpdatePasswordRequest request);

    String updateAvatar(String username, MultipartFile file) throws FileUploadException, IOException;

    void deleteUser(String username);

    void deleteAvatar(String username);
}
