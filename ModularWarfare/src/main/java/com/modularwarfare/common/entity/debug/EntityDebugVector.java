package com.modularwarfare.common.entity.debug;

import com.modularwarfare.common.entity.ModEntities;
import com.modularwarfare.common.vector.Vector3f;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityDebugVector extends EntityDebugColor {
    private static final EntityDataAccessor<Float> POINTING_X = SynchedEntityData.defineId(EntityDebugVector.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> POINTING_Y = SynchedEntityData.defineId(EntityDebugVector.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> POINTING_Z = SynchedEntityData.defineId(EntityDebugVector.class, EntityDataSerializers.FLOAT);
    public int life = 1000;

    public EntityDebugVector(EntityType<?> type, Level level) {
        super(type, level);
        this.setBoundingBox(new AABB(-0.125, -0.125, -0.125, 0.125, 0.125, 0.125));
    }

    public EntityDebugVector(Level w, Vector3f u, Vector3f v, int i, float r, float g, float b) {
        this(ModEntities.DEBUG_VECTOR.get(), w);
        this.setPos(u.x, u.y, u.z);
        this.setPointing(v.x, v.y, v.z);
        this.setColor(r, g, b);
        this.life = i;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(POINTING_X, 1.0f);
        this.entityData.define(POINTING_Y, 1.0f);
        this.entityData.define(POINTING_Z, 1.0f);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setPointingX(tag.getFloat("pointing_x"));
        this.setPointingY(tag.getFloat("pointing_y"));
        this.setPointingZ(tag.getFloat("pointing_z"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("pointing_x", this.getPointingX());
        tag.putFloat("pointing_y", this.getPointingY());
        tag.putFloat("pointing_z", this.getPointingZ());
    }

    @Override
    public void tick() {
        this.life--;
        if (this.life <= 0) {
            this.discard();
        }
    }

    public void setPointingX(float x) { this.entityData.set(POINTING_X, x); }
    public float getPointingX() { return this.entityData.get(POINTING_X); }
    public void setPointingY(float y) { this.entityData.set(POINTING_Y, y); }
    public float getPointingY() { return this.entityData.get(POINTING_Y); }
    public void setPointingZ(float z) { this.entityData.set(POINTING_Z, z); }
    public float getPointingZ() { return this.entityData.get(POINTING_Z); }
    public void setPointing(float x, float y, float z) {
        this.setPointingX(x);
        this.setPointingY(y);
        this.setPointingZ(z);
    }
}