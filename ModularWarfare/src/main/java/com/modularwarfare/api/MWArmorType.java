package com.modularwarfare.api;

public enum MWArmorType {
    Head(new int[0]),
    Chest(new int[0]),
    Legs(new int[0]),
    Feet(new int[0]),
    Vest(1);

    int[] validSlots;

    MWArmorType(int... validSlots) {
        this.validSlots = validSlots;
    }

    public boolean hasSlot(int slot) {
        for (int s : validSlots) {
            if (s == slot) return true;
        }
        return false;
    }

    public int[] getValidSlots() {
        return validSlots;
    }

    public static boolean isVanilla(MWArmorType type) {
        return type != Head && type != Chest && type != Legs && type != Feet;
    }
}