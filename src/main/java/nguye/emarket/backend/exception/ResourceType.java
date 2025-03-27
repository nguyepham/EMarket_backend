package nguye.emarket.backend.exception;

import lombok.Getter;

@Getter
public enum ResourceType {

    USERNAME("Username"),
    PASSWORD("Password"),
    EMAIL("Email"),
    USER_ADDRESS("User address"),
    PROFILE("Profile"),

    USER("User"),;

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }
}
