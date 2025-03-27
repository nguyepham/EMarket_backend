package nguye.emarket.backend.service;

import nguye.emarket.backend.model.*;

public interface UserService {

    void createUser(UserCreate newUser);

    User getUser(String username);

    Profile getDetails(String username);

    UserUpdate updateDetails(String username, UserUpdate details);

    void updatePassword(String username, UpdatePasswordRequest request);

    void deleteUser(String username);
}
