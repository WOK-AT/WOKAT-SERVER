package com.sopt.wokat.domain.member.entity;

public enum Role {
    
    ROLE_MEMBER("MEMBER"),
    ROLE_ADMIN("ADMIN");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid Role value: " + value);
    }
    
}
