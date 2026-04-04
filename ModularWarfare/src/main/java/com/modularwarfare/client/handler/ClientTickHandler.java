package com.modularwarfare.client.handler;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.ClientRenderHooks;
import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.anim.StateEntry;
import com.modularwarfare.client.hud.GunUI;
import com.modularwarfare.client.model.InstantBulletRenderer;
import com.modularwarfare.client.model.renders.RenderParameters;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.guns.ItemSpray;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClientTickHandler {
    private static ItemStack oldItem;
    public static ConcurrentHashMap<UUID, Integer> playerShootCooldown = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, Integer> playerReloadCooldown = new ConcurrentHashMap<>();
    private int i = 0;

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            onClientTickStart(Minecraft.getInstance());
            ModularWarfare.NETWORK.handleClientPackets();

            Iterator<UUID> iterator = playerShootCooldown.keySet().iterator();
            while (iterator.hasNext()) {
                UUID uuid = iterator.next();
                int value = playerShootCooldown.get(uuid) - 1;
                if (value <= 0) {
                    iterator.remove();
                } else {
                    playerShootCooldown.put(uuid, value);
                }
            }

            iterator = playerReloadCooldown.keySet().iterator();
            while (iterator.hasNext()) {
                UUID uuid = iterator.next();
                int value = playerReloadCooldown.get(uuid) - 1;
                if (value <= 0) {
                    iterator.remove();
                } else {
                    playerReloadCooldown.put(uuid, value);
                }
            }
        } else if (event.phase == TickEvent.Phase.END) {
            onClientTickEnd(Minecraft.getInstance());
        }
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            float fps = Minecraft.getInstance().getFps();
            if (fps <= 0 || fps > 240) fps = 60.0f;
            StateEntry.smoothing = (float) (event.renderTickTime * (60.0 / fps));
            onRenderTickStart(Minecraft.getInstance(), event.renderTickTime);
        }
    }

    public void onRenderTickStart(Minecraft mc, float renderTick) {
        if (mc.player == null || mc.level == null) return;

        Player player = mc.player;
        ItemStack stack = player.getMainHandItem();

        if (!stack.isEmpty() && stack.getItem() instanceof ItemGun) {
            for (AnimStateMachine stateMachine : ClientRenderHooks.weaponAnimations.values()) {
                stateMachine.onRenderTickUpdate();
            }
        } else {
            RenderParameters.resetRenderMods();
        }
    }

    public void onClientTickStart(Minecraft mc) {
        if (mc.player == null || mc.level == null) return;

        if (mc.player != null) {
            float deltaYaw = mc.player.getYRot() - mc.player.yRotO;
            float deltaPitch = mc.player.getXRot() - mc.player.xRotO;

            RenderParameters.GUN_ROT_X_LAST = RenderParameters.GUN_ROT_X;
            RenderParameters.GUN_ROT_Y_LAST = RenderParameters.GUN_ROT_Y;

            RenderParameters.GUN_ROT_X += deltaYaw / 1.5f;
            RenderParameters.GUN_ROT_Y += deltaPitch / 1.5f;

            RenderParameters.GUN_ROT_X *= 0.2f;
            RenderParameters.GUN_ROT_Y *= 0.2f;

            RenderParameters.GUN_ROT_X = Math.max(-20.0f, Math.min(20.0f, RenderParameters.GUN_ROT_X));
            RenderParameters.GUN_ROT_Y = Math.max(-20.0f, Math.min(20.0f, RenderParameters.GUN_ROT_Y));
        }

        if (GunUI.bulletSnapFade > 0) {
            GunUI.bulletSnapFade -= 0.01f;
        }

        processGunChange();
        ItemGun.fireButtonHeld = mc.options.keyAttack.isDown();
    }

    public void onClientTickEnd(Minecraft mc) {
        if (mc.player == null || mc.level == null) return;

        Player player = mc.player;

        if (Math.abs(RenderParameters.playerRecoilPitch) > 0.01f) {
            RenderParameters.playerRecoilPitch *= 0.8f;
        }
        if (Math.abs(RenderParameters.playerRecoilYaw) > 0.01f) {
            RenderParameters.playerRecoilYaw *= 0.8f;
        }

        player.setXRot(player.getXRot() - RenderParameters.playerRecoilPitch);
        player.setYRot(player.getYRot() - RenderParameters.playerRecoilYaw);

        RenderParameters.antiRecoilPitch += RenderParameters.playerRecoilPitch;
        RenderParameters.antiRecoilYaw += RenderParameters.playerRecoilYaw;
        player.setXRot(player.getXRot() + RenderParameters.antiRecoilPitch * 0.2f);
        player.setYRot(player.getYRot() + RenderParameters.antiRecoilYaw * 0.2f);

        RenderParameters.antiRecoilPitch *= 0.8f;
        RenderParameters.antiRecoilYaw *= 0.8f;

        for (AnimStateMachine stateMachine : ClientRenderHooks.weaponAnimations.values()) {
            stateMachine.onTickUpdate();
        }

        AnimStateMachine anim = ClientRenderHooks.getAnimMachine(player);
        if (anim.reloading) {
            mc.player.setSprinting(false);
        }

        InstantBulletRenderer.updateAllTrails();
    }

    private void processGunChange() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack currentItem = player.getMainHandItem();

        if (oldItem != null && currentItem.getItem() != oldItem.getItem()) {
            if (currentItem.getItem() instanceof ItemGun) {
                player.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0f, 1.0f);
                RenderParameters.switchDelay = 20;
            } else if (currentItem.getItem() instanceof ItemSpray) {
                player.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0f, 1.0f);
                RenderParameters.switchDelay = 20;
            }
        }

        if (oldItem != currentItem) {
            if (player != null) {
                ClientRenderHooks.getAnimMachine(player).attachmentMode = false;
            }
            oldItem = currentItem;
        }
    }
}