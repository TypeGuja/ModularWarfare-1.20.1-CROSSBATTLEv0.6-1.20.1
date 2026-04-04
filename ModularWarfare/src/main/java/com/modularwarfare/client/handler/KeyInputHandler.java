package com.modularwarfare.client.handler;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.ModularWarfare.ModConfig;
import com.modularwarfare.client.ClientProxy;
import com.modularwarfare.client.ClientRenderHooks;
import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.input.KeyEntry;
import com.modularwarfare.client.input.KeyType;
import com.modularwarfare.client.model.renders.RenderGunStatic;
import com.modularwarfare.common.guns.AttachmentEnum;
import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemAmmo;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.network.*;
import com.modularwarfare.utility.MWSound;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class KeyInputHandler extends com.modularwarfare.utility.event.ForgeEvent {
    private List<KeyEntry> keyBinds = new ArrayList<>();

    public KeyInputHandler() {
        keyBinds.add(new KeyEntry(KeyType.GunReload));
        keyBinds.add(new KeyEntry(KeyType.ClientReload));
        keyBinds.add(new KeyEntry(KeyType.FireMode));
        keyBinds.add(new KeyEntry(KeyType.GunUnload));
        keyBinds.add(new KeyEntry(KeyType.AddAttachment));
        keyBinds.add(new KeyEntry(KeyType.Flashlight));
        if (ModConfig.INSTANCE != null && ModConfig.INSTANCE.enableModifiedInventory) {
            keyBinds.add(new KeyEntry(KeyType.Backpack));
        }
        keyBinds.add(new KeyEntry(KeyType.Left));
        keyBinds.add(new KeyEntry(KeyType.Right));
        keyBinds.add(new KeyEntry(KeyType.Up));
        keyBinds.add(new KeyEntry(KeyType.Down));
        if (ModularWarfare.DEV_ENV) {
            keyBinds.add(new KeyEntry(KeyType.DebugMode));
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        for (KeyType type : KeyType.values()) {
            event.register(new KeyEntry(type).keyBinding);
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        for (KeyEntry entry : keyBinds) {
            if (entry.keyBinding.consumeClick()) {
                handleKeyInput(entry.keyType);
            }
        }
    }

    private void handleKeyInput(KeyType keyType) {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        switch (keyType) {
            case ClientReload:
                if (player.isCrouching()) {
                    ModularWarfare.PROXY.reloadModels(true);
                } else {
                    ModularWarfare.PROXY.reloadModels(false);
                }
                break;
            case FireMode:
                if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof ItemGun) {
                    ModularWarfare.NETWORK.sendToServer(new PacketGunSwitchMode());
                }
                break;
            case GunReload:
                ItemStack stack = player.getMainHandItem();
                if (!stack.isEmpty() && (stack.getItem() instanceof ItemGun || stack.getItem() instanceof ItemAmmo)) {
                    ModularWarfare.NETWORK.sendToServer(new PacketGunReload());
                }
                break;
            case GunUnload:
                if (ClientRenderHooks.getAnimMachine(player).attachmentMode) {
                    ModularWarfare.NETWORK.sendToServer(new PacketGunUnloadAttachment(ClientProxy.attachmentUI.selectedAttachEnum.getName(), false));
                } else {
                    ItemStack unloadStack = player.getMainHandItem();
                    if (!unloadStack.isEmpty() && (unloadStack.getItem() instanceof ItemGun || unloadStack.getItem() instanceof ItemAmmo)) {
                        ModularWarfare.NETWORK.sendToServer(new PacketGunReload(true));
                    }
                }
                break;
            case DebugMode:
                if (player.isCrouching()) {
                    ModularWarfare.loadContentPacks(true);
                    ModularWarfare.PROXY.reloadModels(true);
                }
                break;
            case AddAttachment:
                if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof ItemGun) {
                    AnimStateMachine stateMachine = ClientRenderHooks.getAnimMachine(player);
                    stateMachine.attachmentMode = !stateMachine.attachmentMode;
                    ModularWarfare.PROXY.playSound(new MWSound(player.blockPosition(), "attachment.open", 1.0f, 1.0f));
                }
                break;
            case Flashlight:
                if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof ItemGun) {
                    ItemStack gunStack = player.getMainHandItem();
                    if (GunType.getAttachment(gunStack, AttachmentEnum.Flashlight) != null) {
                        RenderGunStatic.isLightOn = !RenderGunStatic.isLightOn;
                        ModularWarfare.PROXY.playSound(new MWSound(player.blockPosition(), "attachment.apply", 1.0f, 1.0f));
                    }
                }
                break;
            case Backpack:
                if (ModConfig.INSTANCE != null && ModConfig.INSTANCE.enableModifiedInventory && !player.isCrouching()) {
                    ModularWarfare.NETWORK.sendToServer(new PacketOpenGui(0));
                }
                break;
            case Left:
                ClientProxy.attachmentUI.processKeyInput(KeyType.Left);
                break;
            case Right:
                ClientProxy.attachmentUI.processKeyInput(KeyType.Right);
                break;
            case Up:
                ClientProxy.attachmentUI.processKeyInput(KeyType.Up);
                break;
            case Down:
                ClientProxy.attachmentUI.processKeyInput(KeyType.Down);
                break;
        }
    }
}