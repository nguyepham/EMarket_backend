package nguye.emarket.backend.exception;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistException extends RuntimeException {

    private final String errorCode;
    private final String message;

    public ResourceAlreadyExistException(ResourceType resourceType) {
        this.errorCode = AppError.RESOURCE_ALREADY_EXIST.getErrorCode();
        this.message = resourceType.getValue() + AppError.RESOURCE_ALREADY_EXIST.getMessage();
    }
}
