package nguye.emarket.backend.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class InvalidJwtException extends AuthenticationException {

    private final String message;
    public InvalidJwtException(String message) {
        super(message);
        this.message = "InvalidJwtException: " + message;
    }
}
