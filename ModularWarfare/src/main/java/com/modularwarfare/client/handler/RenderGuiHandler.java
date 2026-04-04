package com.modularwarfare.client.handler;

import com.modularwarfare.client.ClientProxy;
import com.modularwarfare.client.ClientRenderHooks;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.utility.DevGui;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderGuiHandler {

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return;

        ItemStack stack = player.getMainHandItem();
        if (!stack.isEmpty() && stack.getItem() instanceof ItemGun) {
            new DevGui(mc, stack, (ItemGun) stack.getItem(),
                    ClientProxy.gunStaticRenderer,
                    ClientRenderHooks.getAnimMachine(player));
        }
    }
}