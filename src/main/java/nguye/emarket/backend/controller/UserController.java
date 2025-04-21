package nguye.emarket.backend.controller;

import jakarta.validation.Valid;
import nguye.emarket.backend.api.UserApi;
import nguye.emarket.backend.model.*;
import nguye.emarket.backend.service.implementation.BasicUserService;
import nguye.emarket.backend.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UserController implements UserApi {

    private final BasicUserService basicUserService;

    public UserController(BasicUserService basicUserService) {
        this.basicUserService = basicUserService;
    }

    @Override
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        return ResponseEntity.ok(basicUserService.getUser(username));
    }

    @Override
    public ResponseEntity<SuccessResponse> deleteUser(@PathVariable("username") String username) {
        SecurityUtil.authenticateUser(username);
        basicUserService.deleteUser(username);
        return ResponseEntity.ok(new SuccessResponse("Account deleted successfully"));
    }

    @Override
    public ResponseEntity<Profile> getDetails(@PathVariable("username") String username) {
        return ResponseEntity.ok(basicUserService.getDetails(username));
    }

    @Override
    public ResponseEntity<UserUpdate> updateDetails(
            @PathVariable("username") String username,
            @Valid @RequestBody UserUpdate userUpdate) {
        SecurityUtil.authenticateUser(username);
        return ResponseEntity.ok(basicUserService.updateDetails(username, userUpdate));
    }

    @Override
    public ResponseEntity<SuccessResponse> updatePassword(
            @PathVariable("username") String username,
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        SecurityUtil.authenticateUser(username);
        basicUserService.updatePassword(username, updatePasswordRequest);
        return ResponseEntity.ok(new SuccessResponse("Password updated successfully"));
    }

    @Override
    public ResponseEntity<SuccessResponse> updateAvatar(
            @PathVariable("username") String username,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        SecurityUtil.authenticateUser(username);
        String imageUrl = basicUserService.updateAvatar(username, file);
        return ResponseEntity.ok(new SuccessResponse(imageUrl));
    }

    @Override
    public ResponseEntity<SuccessResponse> deleteAvatar(
            @PathVariable("username") String username) {
        SecurityUtil.authenticateUser(username);
        basicUserService.deleteAvatar(username);
        return ResponseEntity.ok(new SuccessResponse("Avatar deleted successfully"));
    }
}
