package com.modularwarfare.loader.api;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.resources.ResourceLocation;

public class ObjModelLoader {

    public static AbstractObjModel load(String path) {
        try {
            return new AbstractObjModel(new ResourceLocation(ModularWarfare.MOD_ID, path));
        } catch (Exception e) {
            ModularWarfare.LOGGER.error("Failed to load model: " + path, e);
            return new AbstractObjModel();
        }
    }

    public static AbstractObjModel load(BaseType type, String path) {
        try {
            String fullPath = String.format("%s/%s", type.contentPack, path);
            return new AbstractObjModel(new ResourceLocation(ModularWarfare.MOD_ID, fullPath));
        } catch (Exception e) {
            ModularWarfare.LOGGER.error("Failed to load model: " + path, e);
            return new AbstractObjModel();
        }
    }
}