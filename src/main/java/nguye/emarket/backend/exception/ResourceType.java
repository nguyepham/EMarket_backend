package nguye.emarket.backend.exception;

import lombok.Getter;

@Getter
public enum ResourceType {

    USERNAME("Username"),
    PASSWORD("Password"),
    IMAGE("Image"),
    EMAIL("Email"),
    ADDRESS("Address"),
    PROFILE("Profile"),
    PRODUCT("Product"),
    PRODUCT_REVIEW("Product Review"),
    PRODUCT_REVIEW_IMAGE("Product Review Image"),
    SELLER_REVIEW("Seller Review"),
    SELLER_REVIEW_IMAGE("Seller Review Image"),
    CATEGORY("Category"),
    USER("User"),;

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }
}
