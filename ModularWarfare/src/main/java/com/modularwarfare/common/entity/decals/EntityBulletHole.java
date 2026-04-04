package com.modularwarfare.common.entity.decals;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityBulletHole extends EntityDecal {

    public EntityBulletHole(EntityType<?> type, Level level) {
        super(type, level);
        this.maxTimeAlive = 200;
    }

    @Override
    public ResourceLocation getDecalTexture() {
        return new ResourceLocation("modularwarfare", String.format("textures/entity/bullethole/bullethole%d.png", this.getTextureNumber()));
    }

    @Override
    public int getTextureCount() {
        return 1;
    }
}