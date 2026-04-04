package com.modularwarfare.common.guns;

public enum WeaponScopeType {
    DEFAULT,
    REDDOT,
    TWO,
    FOUR,
    EIGHT,
    FIFTEEN;

    public static WeaponScopeType fromString(String modeName) {
        for (WeaponScopeType type : values()) {
            if (type.name().equalsIgnoreCase(modeName)) {
                return type;
            }
        }
        return null;
    }
}