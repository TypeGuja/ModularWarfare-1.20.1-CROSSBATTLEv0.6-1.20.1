package com.modularwarfare.common.entity.debug;

import com.modularwarfare.common.entity.ModEntities;
import com.modularwarfare.common.vector.Vector3f;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityDebugAABB extends Entity {
    public Vector3f vector;
    public int life;
    public float red = 1.0f;
    public float green = 1.0f;
    public float blue = 1.0f;
    public float rotationRoll;
    public Vector3f offset;

    public EntityDebugAABB(EntityType<?> type, Level level) {
        super(type, level);
    }

    public EntityDebugAABB(Level w, Vector3f u, Vector3f v, int i, float r, float g, float b, float yaw, float pitch, float roll, Vector3f offset) {
        super(ModEntities.DEBUG_AABB.get(), w);
        this.setPos(u.x, u.y, u.z);
        this.setYRot(yaw);
        this.setXRot(pitch);
        this.rotationRoll = roll;
        this.vector = v;
        this.life = i;
        this.red = r;
        this.green = g;
        this.blue = b;
        this.offset = offset;
    }

    @Override
    public void tick() {
        this.life--;
        if (this.life <= 0) {
            this.discard();
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void defineSynchedData() {}
}