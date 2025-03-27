package nguye.emarket.backend.controller;

import jakarta.validation.Valid;
import nguye.emarket.backend.api.AuthApi;
import nguye.emarket.backend.authentication.SuccessfulAuthentication;
import nguye.emarket.backend.model.AuthenticationRequest;
import nguye.emarket.backend.model.UserCreate;
import nguye.emarket.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<String> login(@Valid @RequestBody AuthenticationRequest payload)
            throws AuthenticationException {

        String username = payload.getUsername();
        String password = payload.getPassword();

        SuccessfulAuthentication successfulAuthentication =
                (SuccessfulAuthentication) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        return ResponseEntity.ok(successfulAuthentication.getDetails().toString());
    }

    @Override
    public ResponseEntity<String> signUp (@Valid @RequestBody UserCreate newUser) {
        userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("Signed up successfully");
    }
}
