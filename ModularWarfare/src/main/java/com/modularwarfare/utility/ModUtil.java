package com.modularwarfare.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;

public class ModUtil {
    public static final int BACKPACK_CONTENT_OFFSET_X = 180;
    public static final int INVENTORY_SLOT_SIZE_PIXELS = 18;
    public static final int BACKPACK_SLOT_OFFSET_X = 76;
    public static final int BACKPACK_SLOT_OFFSET_Y = 7;
    public static final int BACKPACK_CONTENT_OFFSET_Y = 18;

    public static int getBrightness(Entity entity) {
        BlockPos pos = BlockPos.containing(entity.getX(), entity.getY(), entity.getZ());
        var level = entity.level();
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return Math.max(blockLight, skyLight);
    }
}