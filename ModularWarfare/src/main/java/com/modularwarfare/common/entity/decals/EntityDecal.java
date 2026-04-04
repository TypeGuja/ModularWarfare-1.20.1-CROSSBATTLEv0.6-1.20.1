package com.modularwarfare.common.entity.decals;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class EntityDecal extends Entity {
    private EnumDecalSide side;
    protected int maxTimeAlive;
    private static final EntityDataAccessor<Boolean> PERMANENT = SynchedEntityData.defineId(EntityDecal.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(EntityDecal.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SEED = SynchedEntityData.defineId(EntityDecal.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SIDE_ID = SynchedEntityData.defineId(EntityDecal.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TEXTURE_NUMBER = SynchedEntityData.defineId(EntityDecal.class, EntityDataSerializers.INT);

    public EntityDecal(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setSide(EnumDecalSide.ALL);
        this.setBoundingBox(new AABB(-0.5, -0.125, -0.5, 0.5, 0.125, 0.5));
        this.maxTimeAlive = 900;
    }

    public abstract ResourceLocation getDecalTexture();
    public abstract int getTextureCount();

    @Override
    protected void defineSynchedData() {
        this.entityData.define(PERMANENT, false);
        this.entityData.define(AGE, 0);
        this.entityData.define(SEED, 0);
        this.entityData.define(SIDE_ID, 0);
        this.entityData.define(TEXTURE_NUMBER, this.random.nextInt(this.getTextureCount()));
    }

    public EnumDecalSide getSide() { return side; }
    public void setSide(EnumDecalSide side) {
        this.side = side;
        this.setSideID(side.getId());
    }

    public float getAgeRatio() {
        return Math.max(0.0f, (float) (this.getAge() + this.maxTimeAlive) / (float) this.maxTimeAlive);
    }

    public boolean isPermanent() { return this.entityData.get(PERMANENT); }
    public void setPermanent(boolean bool) { this.entityData.set(PERMANENT, bool); }
    public int getAge() { return this.entityData.get(AGE); }
    public void setAge(int num) { this.entityData.set(AGE, num); }
    public int getSeed() { return this.entityData.get(SEED); }
    public void setSeed(int num) { this.entityData.set(SEED, num); }
    public int getSideID() { return this.entityData.get(SIDE_ID); }
    public void setSideID(int num) { this.entityData.set(SIDE_ID, num); }
    public int getTextureNumber() { return this.entityData.get(TEXTURE_NUMBER); }
    public void setTextureNumber(int num) { this.entityData.set(TEXTURE_NUMBER, num); }

    @Override
    public boolean isPickable() { return false; }

    @Override
    public void tick() {
        this.noPhysics = true;
        if (!this.isPermanent()) {
            if (this.getAge() > -this.maxTimeAlive) {
                this.setAge(this.getAge() - 1);
            } else {
                this.discard();
            }
        }
        this.noPhysics = false;
        this.setNoGravity(true);
    }

    @Override
    public boolean isInWater() { return false; }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.setPermanent(tag.getBoolean("Permanent"));
        this.setAge(tag.getInt("Age"));
        this.setSeed(tag.getInt("Seed"));
        this.setSideID(tag.getInt("SideID"));
        if (tag.contains("TextureNumber")) {
            this.setTextureNumber(tag.getInt("TextureNumber"));
        }
        this.side = EnumDecalSide.getEnumFromId(this.getSideID());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("Permanent", this.isPermanent());
        tag.putInt("Age", this.getAge());
        tag.putInt("Seed", this.getSeed());
        tag.putInt("SideID", this.getSideID());
        tag.putInt("TextureNumber", this.getTextureNumber());
    }

    public enum EnumDecalSide {
        ALL(0), WALLS(1), FLOOR(2), NORTH(3), EAST(4), SOUTH(5), WEST(6);

        private final int id;
        EnumDecalSide(int id) { this.id = id; }
        public static EnumDecalSide getEnumFromId(int id) { return values()[id]; }
        public int getId() { return id; }
    }
}