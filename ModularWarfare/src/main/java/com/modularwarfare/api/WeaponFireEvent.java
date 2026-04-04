package com.modularwarfare.api;

import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemGun;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;

import java.util.List;
import java.util.Random;

public class WeaponFireEvent extends WeaponEvent {

    public WeaponFireEvent(Player entityPlayer, ItemStack stackWeapon, ItemGun itemWeapon) {
        super(entityPlayer, stackWeapon, itemWeapon);
    }

    public static class Post extends WeaponFireEvent {
        private List<Entity> affectedEntities;
        private int fireTickDelay;
        private float damage;
        private float pitchRecoil;
        private float yawRecoil;

        public Post(Player entityPlayer, ItemStack stackWeapon, ItemGun itemWeapon,
                    List<Entity> affectedEntities, boolean isHeadshot) {
            super(entityPlayer, stackWeapon, itemWeapon);
            this.affectedEntities = affectedEntities;
            Random rand = new Random();
            GunType type = itemWeapon.type;
            this.pitchRecoil = type.recoilPitch + (rand.nextFloat() * (type.randomRecoilPitch * 2.0f) - type.randomRecoilPitch);
            this.yawRecoil = type.recoilYaw + (rand.nextFloat() * (type.randomRecoilYaw * 2.0f) - type.randomRecoilYaw);
            this.damage = type.gunDamage;
            if (isHeadshot) {
                this.damage += type.gunDamageHeadshotBonus;
            }
            this.fireTickDelay = type.fireTickDelay;
        }

        public List<Entity> getAffectedEntities() { return affectedEntities; }
        public void setAffectedEntities(List<Entity> updatedList) { this.affectedEntities = updatedList; }
        public float getRecoilPitch() { return pitchRecoil; }
        public void setRecoilPitch(float updatedPitch) { this.pitchRecoil = updatedPitch; }
        public float getRecoilYaw() { return yawRecoil; }
        public void setRecoilYaw(float updatedYaw) { this.yawRecoil = updatedYaw; }
        public float getDamage() { return damage; }
        public void setDamage(float updatedDamage) { this.damage = updatedDamage; }
        public float getTickDelay() { return fireTickDelay; }
        public void setSecPerShot(int fireTickDelay) { this.fireTickDelay = fireTickDelay; }
    }

    @Cancelable
    public static class Pre extends WeaponFireEvent {
        private int weaponRange;

        public Pre(Player entityPlayer, ItemStack stackWeapon, ItemGun itemWeapon, int weaponRange) {
            super(entityPlayer, stackWeapon, itemWeapon);
            this.weaponRange = weaponRange;
        }

        public int getWeaponRange() { return weaponRange; }
        public void setWeaponRange(int updatedRange) { this.weaponRange = updatedRange; }
    }
}