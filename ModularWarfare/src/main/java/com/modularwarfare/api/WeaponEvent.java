package com.modularwarfare.api;

import com.modularwarfare.common.guns.ItemGun;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class WeaponEvent extends Event {
    private final Player entityPlayer;
    private final ItemStack stackWeapon;
    private final ItemGun itemWeapon;

    public WeaponEvent(Player entityPlayer, ItemStack stackWeapon, ItemGun itemWeapon) {
        this.entityPlayer = entityPlayer;
        this.stackWeapon = stackWeapon;
        this.itemWeapon = itemWeapon;
    }

    public Player getWeaponUser() {
        return this.entityPlayer;
    }

    public ItemStack getWeaponStack() {
        return this.stackWeapon;
    }

    public ItemGun getWeaponItem() {
        return this.itemWeapon;
    }
}