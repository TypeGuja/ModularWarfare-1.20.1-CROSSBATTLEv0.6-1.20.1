package com.modularwarfare.common.extra;

import com.modularwarfare.ModularWarfare;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;

public class ItemLight extends Item {
    public ItemLight(String name) {
        super(new Properties());
        ForgeRegistries.ITEMS.register(new ResourceLocation(ModularWarfare.MOD_ID, name), this);
    }
}