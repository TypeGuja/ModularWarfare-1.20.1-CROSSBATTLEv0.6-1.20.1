package com.modularwarfare.common.guns;

public enum WeaponFireMode {
    SEMI,
    FULL,
    BURST;

    public static WeaponFireMode fromString(String modeName) {
        for (WeaponFireMode mode : values()) {
            if (mode.name().equalsIgnoreCase(modeName)) {
                return mode;
            }
        }
        return null;
    }
}