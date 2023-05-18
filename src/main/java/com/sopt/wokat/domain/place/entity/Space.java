package com.sopt.wokat.domain.place.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Space {
    
    FREE_ZONE("0"),
    MEETING_ROOM("1"),
    CAFE("2");

    private final String value;

}
