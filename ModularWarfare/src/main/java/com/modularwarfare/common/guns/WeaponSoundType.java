package com.modularwarfare.common.guns;

public enum WeaponSoundType {
    DryFire("weaponDryFire", 8, "defemptyclick"),
    Fire("weaponFire", 64, null),
    FireSuppressed("weaponFireSuppressed", 32, null),
    FireLast("weaponFireLast", 16, null),
    Reload("weaponReload", 16, "reload"),
    Pump("weaponBolt", 8, null),
    BulletLoad("weaponBulletLoad", 8, null),
    Crack("crack", 10, "crack"),
    Equip("equip", 8, "equip"),
    Hitmarker("hitmarker", 8, "hitmarker"),
    Penetration("penetration", 20, "penetration"),
    Load("weaponLoad", 12, "load"),
    Unload("weaponUnload", 12, "unload"),
    ReloadEmpty("weaponReloadEmpty", 12, null),
    Charge("weaponCharge", 16, null),
    ModeSwitch("weaponModeSwitch", 8, "defweaponmodeswitch"),
    FlyBy("bulletFlyBy", 3, "flyby"),
    Casing("casing", 3, "casing"),
    Spray("spray", 8, "spray"),
    Punched("punched", 64, "punched"),
    AttachmentOpen("attachment.open", 10, "attachment.open"),
    AttachmentApply("attachment.apply", 10, "attachment.apply"),
    ImpactDirt("impact.dirt", 10, "impact.dirt"),
    ImpactGlass("impact.glass", 10, "impact.glass"),
    ImpactMetal("impact.metal", 10, "impact.metal"),
    ImpactStone("impact.stone", 10, "impact.stone"),
    ImpactWater("impact.water", 10, "impact.water"),
    ImpactWood("impact.wood", 10, "impact.wood");

    public String eventName;
    public int defaultRange;
    public String defaultSound;

    WeaponSoundType(String eventName, int defaultRange, String defaultSound) {
        this.eventName = eventName;
        this.defaultRange = defaultRange;
        this.defaultSound = defaultSound;
    }

    public static WeaponSoundType fromString(String input) {
        for (WeaponSoundType type : values()) {
            if (type.toString().equalsIgnoreCase(input)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.eventName;
    }
}