package com.modularwarfare.common.network;

import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.guns.WeaponFireMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PacketGunSwitchMode extends PacketBase {

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {}

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {}

    @Override
    public void handleServerSide(Player player) {
        if (player instanceof ServerPlayer) {
            ItemStack gunStack = player.getMainHandItem();
            if (!gunStack.isEmpty() && gunStack.getItem() instanceof ItemGun itemGun) {
                WeaponFireMode current = GunType.getFireMode(gunStack);
                WeaponFireMode[] modes = itemGun.type.fireModes;

                if (current == null || modes.length <= 1) return;

                int index = 0;
                for (int i = 0; i < modes.length; i++) {
                    if (modes[i] == current) {
                        index = (i + 1) % modes.length;
                        break;
                    }
                }

                itemGun.onGunSwitchMode(player, player.level(), gunStack, itemGun, modes[index]);
            }
        }
    }

    @Override
    public void handleClientSide(Player player) {}
}