package models.enums;


import java.util.Arrays;

public enum ProfileEnum {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_CUSTOMER("ROLE_CUSTOMER"),
    ROLE_TECHNICIAN("ROLE_TECHNICIAN");

    private final String description;

    ProfileEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ProfileEnum toEnum(final String description) {
        return Arrays.stream(ProfileEnum.values())
                .filter(profileEnum -> profileEnum.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid description: " + description));
    }
}
