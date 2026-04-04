package com.modularwarfare.utility;

import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.anim.StateType;
import com.modularwarfare.client.model.ModelGun;
import com.modularwarfare.client.model.renders.RenderGunStatic;
import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class DevGui {

    public DevGui(Minecraft mc, ItemStack itemStack, ItemGun itemGun, RenderGunStatic renderGun, AnimStateMachine anim) {
        GunType gunType = itemGun.type;
        ModelGun gunModel = (ModelGun) gunType.model;

        if (mc.player.getInventory().getItem(3).getItem() == net.minecraft.world.item.Items.STICK) {
            boolean hasAmmo = ItemGun.hasAmmoLoaded(itemStack);
            String displayName = "Display Name- " + gunType.displayName;
            String internalName = "Internal Name - " + gunType.internalName;
            String modelScale = "Model Scale - " + gunModel.config.extra.modelScale;
            String iconName = "Icon Name - " + gunType.iconName;
            String skinNames = "Skin Name(s) - " + Arrays.toString(gunType.modelSkins).replace("[", "").replace("]", "");
            String dynamicAmmo = "Dynamic Ammo Model - " + gunType.dynamicAmmo;
            String reloadAnim = "Reload Anim - " + gunModel.config.extra.reloadAnimation;

            if (hasAmmo) {
                CompoundTag ammoTag = itemStack.getOrCreateTag().getCompound("ammo");
                ItemStack ammoStack = ItemStack.of(ammoTag);
            }
        }
    }
}