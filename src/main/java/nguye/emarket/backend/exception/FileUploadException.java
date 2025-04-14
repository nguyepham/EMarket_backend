package nguye.emarket.backend.exception;

import lombok.Getter;

@Getter
public class FileUploadException extends RuntimeException {

    private final String errorCode;
    private final String message;

    public FileUploadException(String fileName) {
        this.errorCode = AppError.FILE_UPLOAD_FAILED.getErrorCode();
        this.message = AppError.FILE_UPLOAD_FAILED.getMessage() + fileName;
    }
}
