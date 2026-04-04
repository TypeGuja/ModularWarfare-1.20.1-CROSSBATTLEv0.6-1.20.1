package com.modularwarfare.common.guns;

import com.modularwarfare.common.type.BaseType;

public class SkinType {
    public String internalName;
    public String displayName;
    public String skinAsset;

    public String getSkin() {
        return skinAsset != null ? skinAsset : internalName;
    }

    public static SkinType getDefaultSkin(BaseType baseType) {
        SkinType skinType = new SkinType();
        skinType.skinAsset = skinType.internalName = baseType.internalName;
        skinType.displayName = baseType.displayName + " - Default";
        return skinType;
    }

    @Override
    public String toString() {
        return skinAsset;
    }
}