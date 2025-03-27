package nguye.emarket.backend.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class BadCredentialsException extends AuthenticationException {

    private final String errorCode;
    private final String message;

    public BadCredentialsException() {
        super(AppError.BAD_CREDENTIALS.getMessage());
        this.errorCode = AppError.BAD_CREDENTIALS.getErrorCode();
        this.message = AppError.BAD_CREDENTIALS.getMessage();
    }
}
