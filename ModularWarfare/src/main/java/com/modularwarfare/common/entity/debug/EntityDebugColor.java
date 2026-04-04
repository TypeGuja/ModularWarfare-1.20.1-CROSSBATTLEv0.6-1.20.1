package com.modularwarfare.common.entity.debug;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class EntityDebugColor extends Entity {
    private static final EntityDataAccessor<Float> COLOR_RED = SynchedEntityData.defineId(EntityDebugColor.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> COLOR_GREEN = SynchedEntityData.defineId(EntityDebugColor.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> COLOR_BLUE = SynchedEntityData.defineId(EntityDebugColor.class, EntityDataSerializers.FLOAT);

    public EntityDebugColor(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(COLOR_RED, 1.0f);
        this.entityData.define(COLOR_GREEN, 1.0f);
        this.entityData.define(COLOR_BLUE, 1.0f);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.setColorRed(tag.getFloat("color_red"));
        this.setColorGreen(tag.getFloat("color_green"));
        this.setColorBlue(tag.getFloat("color_blue"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("color_red", this.getColorRed());
        tag.putFloat("color_green", this.getColorGreen());
        tag.putFloat("color_blue", this.getColorBlue());
    }

    public void setColorRed(float red) { this.entityData.set(COLOR_RED, red); }
    public float getColorRed() { return this.entityData.get(COLOR_RED); }
    public void setColorGreen(float green) { this.entityData.set(COLOR_GREEN, green); }
    public float getColorGreen() { return this.entityData.get(COLOR_GREEN); }
    public void setColorBlue(float blue) { this.entityData.set(COLOR_BLUE, blue); }
    public float getColorBlue() { return this.entityData.get(COLOR_BLUE); }
    public void setColor(float r, float g, float b) {
        this.setColorRed(r);
        this.setColorGreen(g);
        this.setColorBlue(b);
    }
}