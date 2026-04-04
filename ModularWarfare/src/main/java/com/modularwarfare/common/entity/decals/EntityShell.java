package com.modularwarfare.common.entity.decals;


import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.entity.ModEntities;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.guns.WeaponType;
import com.modularwarfare.common.handler.ServerTickHandler;
import com.modularwarfare.common.network.PacketPlaySound;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityShell extends Entity {
    private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(EntityShell.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> WEAPON_TYPE = SynchedEntityData.defineId(EntityShell.class, EntityDataSerializers.STRING);
    protected int ticksInGround;
    protected int maxTimeAlive;
    public boolean playedSound;

    public EntityShell(EntityType<?> type, Level level) {
        super(type, level);
        this.setBoundingBox(new AABB(-0.125, -0.125, -0.125, 0.125, 0.125, 0.125));
        this.maxTimeAlive = 20 * ModularWarfare.ModConfig.INSTANCE.despawnTimeShellCasing;
        this.playedSound = false;
    }

    public EntityShell(Level level, Player thrower, ItemGun gun) {
        this(ModEntities.SHELL.get(), level);

        if (!level.isClientSide && gun != null && gun.type != null) {
            this.setWeaponType(gun.type.weaponType);
            Vec3 rotateYaw = Vec3.ZERO;

            if (ServerTickHandler.playerAimInstant.get(thrower.getName().getString()) != null) {
                if (ServerTickHandler.playerAimInstant.get(thrower.getName().getString())) {
                    rotateYaw = gun.type.shellEjectOffsetAiming.xRot((float) -Math.toRadians(thrower.getXRot()))
                            .yRot((float) -Math.toRadians(thrower.getYRot()));
                } else {
                    rotateYaw = gun.type.shellEjectOffsetNormal.xRot((float) -Math.toRadians(thrower.getXRot()))
                            .yRot((float) -Math.toRadians(thrower.getYRot()));
                }
            }

            Vec3 source = new Vec3(
                    thrower.getX() + thrower.getDeltaMovement().x + rotateYaw.x,
                    thrower.getY() + thrower.getEyeHeight() - 0.1f + thrower.getDeltaMovement().y + rotateYaw.y,
                    thrower.getZ() + thrower.getDeltaMovement().z + rotateYaw.z);
            this.setPos(source.x, source.y, source.z);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(AGE, 0);
        this.entityData.define(WEAPON_TYPE, "");
    }

    public int getAge() { return this.entityData.get(AGE); }
    public void setAge(int num) { this.entityData.set(AGE, num); }
    public String getWeaponType() { return this.entityData.get(WEAPON_TYPE); }
    public void setWeaponType(WeaponType type) {
        this.entityData.set(WEAPON_TYPE, type != null ? type.name().toLowerCase() : "");
    }

    @Override
    public void tick() {
        super.tick();
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        if (this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.04, this.getDeltaMovement().z);
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
            if (!this.playedSound) {
                ModularWarfare.NETWORK.sendToAllAround(new PacketPlaySound(this.blockPosition(), "casing", 0.1f, 1.0f),
                        this.getX(), this.getY(), this.getZ(), 3.0f, this.level().dimension());
                this.playedSound = true;
            }
        }

        this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);

        float f = (float) Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
        this.setXRot((float) (Math.atan2(this.getDeltaMovement().y, f) * 180.0 / Math.PI));
        this.setYRot((float) (Math.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z) * 180.0 / Math.PI));

        if (this.getAge() > -this.maxTimeAlive) {
            this.setAge(this.getAge() - 1);
        } else {
            this.discard();
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}

    public void setHeadingFromThrower(Entity thrower, float pitch, float yaw, float offset, float velocity, float inaccuracy) {
        float f = -net.minecraft.util.Mth.sin(yaw * (float) Math.PI / 180) * net.minecraft.util.Mth.cos(pitch * (float) Math.PI / 180);
        float f1 = -net.minecraft.util.Mth.sin((pitch + offset) * (float) Math.PI / 180);
        float f2 = net.minecraft.util.Mth.cos(yaw * (float) Math.PI / 180) * net.minecraft.util.Mth.cos(pitch * (float) Math.PI / 180);
        this.setDeltaMovement(f, f1, f2);
        this.setDeltaMovement(this.getDeltaMovement().scale(velocity));
        this.setDeltaMovement(this.getDeltaMovement().add(thrower.getDeltaMovement()));
    }
}