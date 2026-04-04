package com.modularwarfare.common.type;

import com.modularwarfare.ModularWarfare;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;

public class BaseItem extends Item {
    public BaseType baseType;
    public boolean render3d = true;

    public BaseItem(BaseType type) {
        super(new Properties().stacksTo(type.maxStackSize));
        type.loadExtraValues();
        this.setRegistryName(type.internalName);
        this.baseType = type;
    }

    public void setType(BaseType type) {
        this.baseType = type;
    }

    protected void setRegistryName(String name) {
        // Исправлено: использование tryBuild для ResourceLocation
        ResourceLocation registryName = ResourceLocation.tryBuild(ModularWarfare.MOD_ID, name);
        if (registryName != null) {
            ForgeRegistries.ITEMS.register(registryName, this);
        }
    }

    public Component generateLoreLine(String prefix, String value) {
        String baseDisplayLine = ChatFormatting.BLUE + "%s: " + ChatFormatting.GRAY + "%s";
        return Component.literal(String.format(baseDisplayLine, prefix, value));
    }

    public Component generateLoreHeader(String prefix) {
        return Component.literal(ChatFormatting.BLUE + prefix);
    }

    public Component generateLoreListEntry(String prefix, String value) {
        return Component.literal(" - " + value + " " + ChatFormatting.GRAY + prefix);
    }

    public Component generateLoreLineAlt(String prefix, String current, String max) {
        String baseDisplayLine = ChatFormatting.BLUE + "%s: " + ChatFormatting.GRAY + "%s" + ChatFormatting.DARK_GRAY + "/" + ChatFormatting.GRAY + "%s";
        return Component.literal(String.format(baseDisplayLine, prefix, current, max));
    }
}