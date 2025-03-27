package nguye.emarket.backend.exception;

import lombok.Getter;

@Getter
public class AccessDeniedException extends RuntimeException {

    private final String errorCode;
    private final String message;

    public AccessDeniedException() {
        this.errorCode = AppError.ACCESS_DENIED.getErrorCode();
        this.message = AppError.ACCESS_DENIED.getMessage();
    }
}
