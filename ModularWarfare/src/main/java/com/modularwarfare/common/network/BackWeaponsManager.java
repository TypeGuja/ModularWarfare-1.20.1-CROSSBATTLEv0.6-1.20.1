package com.modularwarfare.common.network;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class BackWeaponsManager {
    public static final BackWeaponsManager INSTANCE = new BackWeaponsManager();

    private CompoundTag data = new CompoundTag();

    public ItemStack getItemToRender(AbstractClientPlayer player) {
        return ItemStack.EMPTY;
    }

    public BackWeaponsManager collect() {
        return this;
    }

    public void sync() {}

    public CompoundTag serializeNBT() {
        return data;
    }

    public void deserializeNBT(CompoundTag tag) {
        if (tag != null) {
            this.data = tag;
        }
    }
}