package com.modularwarfare.common.guns;

public enum WeaponDotColorType {
    RED,
    BLUE,
    GREEN;

    public static WeaponDotColorType fromString(String modeName) {
        for (WeaponDotColorType type : values()) {
            if (type.name().equalsIgnoreCase(modeName)) {
                return type;
            }
        }
        return null;
    }
}