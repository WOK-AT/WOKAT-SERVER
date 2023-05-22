package com.sopt.wokat.domain.place.entity;

public enum Space {
    
    FREE_ZONE("0"),
    MEETING_ROOM("1"),
    CAFE("2"),
    ZONE_AND_ROOM("3")
    ;

    private final String value;
    
    Space(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Space fromValue(String value) {
        for (Space role : Space.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid Role value: " + value);
    }

}
