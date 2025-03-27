package nguye.emarket.backend.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String errorCode;
    private final String message;

    public ResourceNotFoundException(ResourceType resourceType) {
        this.errorCode = AppError.RESOURCE_NOT_FOUND.getErrorCode();
        this.message = resourceType.getValue() + AppError.RESOURCE_NOT_FOUND.getMessage();
    }
}
