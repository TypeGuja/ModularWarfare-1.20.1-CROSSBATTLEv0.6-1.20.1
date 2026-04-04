package com.modularwarfare.common.type;

import com.modularwarfare.common.armor.ArmorType;
import com.modularwarfare.common.backpacks.BackpackType;
import com.modularwarfare.common.guns.*;

import java.util.ArrayList;

public class ContentTypes {
    public static ArrayList<TypeEntry> values = new ArrayList<>();
    private static int typeId = 0;

    public static void registerTypes() {
        registerType("guns", GunType.class);
        registerType("ammo", AmmoType.class);
        registerType("attachments", AttachmentType.class);
        registerType("armor", ArmorType.class);
        registerType("bullets", BulletType.class);
        registerType("sprays", SprayType.class);
        registerType("backpacks", BackpackType.class);
    }

    private static void registerType(String name, Class<? extends BaseType> typeClass) {
        values.add(new TypeEntry(name, typeClass, typeId));
        typeId++;
    }
}