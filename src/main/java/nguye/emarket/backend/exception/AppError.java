package nguye.emarket.backend.exception;

import lombok.Getter;

@Getter
public enum AppError {

    // Error codes
    GENERIC_ERROR("EMARKET-001", "The system is unable to complete the request."),
    BAD_CREDENTIALS("EMARKET_002", "Invalid username or password."),
    RESOURCE_ALREADY_EXIST("EMARKET_003", " already exists."),
    RESOURCE_NOT_FOUND("EMARKET_004", " not found."),
    ACCESS_DENIED("EMARKET_005", "You must be authorized to complete this request."),;

    private final String errorCode;
    private final String message;

    AppError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
