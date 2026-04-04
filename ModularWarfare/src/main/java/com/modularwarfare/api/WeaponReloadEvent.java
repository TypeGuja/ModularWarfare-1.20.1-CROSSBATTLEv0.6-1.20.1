package com.modularwarfare.api;

import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemGun;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;

public class WeaponReloadEvent extends WeaponEvent {

    public WeaponReloadEvent(Player entityPlayer, ItemStack stackWeapon, ItemGun itemWeapon) {
        super(entityPlayer, stackWeapon, itemWeapon);
    }

    public static class Post extends WeaponReloadEvent {
        private final boolean offhandReload;
        private final boolean multiMagReload;
        private final boolean loadOnly;
        private final boolean unloadOnly;
        private final int reloadAmount;
        private int reloadTime;

        public Post(Player entityPlayer, ItemStack stackWeapon, ItemGun itemWeapon,
                    boolean offhandReload, boolean multiMagReload, boolean loadOnly,
                    boolean unloadOnly, int reloadTime, int reloadAmount) {
            super(entityPlayer, stackWeapon, itemWeapon);
            this.offhandReload = offhandReload;
            this.multiMagReload = multiMagReload;
            this.loadOnly = loadOnly;
            this.unloadOnly = unloadOnly;
            this.reloadTime = reloadTime;
            this.reloadAmount = reloadAmount;
        }

        public boolean isOffhandReload() { return offhandReload; }
        public boolean isMultiMagReload() { return multiMagReload; }
        public boolean isLoadOnly() { return loadOnly; }
        public boolean isUnload() { return unloadOnly; }
        public int getReloadTime() { return reloadTime; }
        public int getReloadCount() { return reloadAmount; }
    }

    @Cancelable
    public static class Pre extends WeaponReloadEvent {
        private final boolean offhandReload;
        private final boolean multiMagReload;
        private int reloadTime;

        public Pre(Player entityPlayer, ItemStack stackWeapon, ItemGun itemWeapon,
                   boolean offhandReload, boolean multiMagReload) {
            super(entityPlayer, stackWeapon, itemWeapon);
            this.offhandReload = offhandReload;
            this.multiMagReload = multiMagReload;
            GunType type = itemWeapon.type;
            if (offhandReload && type.offhandReloadTime != null) {
                this.reloadTime = (int) (type.offhandReloadTime * 0.8f);
            } else {
                this.reloadTime = (int) type.reloadTime;
            }
        }

        public int getReloadTime() { return reloadTime; }
        public void setReloadTime(int updatedReloadTime) { this.reloadTime = updatedReloadTime; }
        public boolean isOffhandReload() { return offhandReload; }
        public boolean isMultiMagReload() { return multiMagReload; }
    }
}