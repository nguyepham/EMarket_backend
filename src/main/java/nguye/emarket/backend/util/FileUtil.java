package nguye.emarket.backend.util;

public class FileUtil {
    
    public static final String UPLOAD_DIR = "uploads";
    public static final String AVATAR_UPLOAD_DIR = "uploads/avatars/";
    public static final String PRODUCT_UPLOAD_DIR = "uploads/products/";
    public static final String REVIEW_UPLOAD_DIR = "uploads/reviews/";

    public static String getFileExtension(String fileName) {
        return "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
