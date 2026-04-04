package com.modularwarfare.common.network;

import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.guns.WeaponSoundType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PacketGunReloadSound extends PacketBase {
    public WeaponSoundType soundType;

    public PacketGunReloadSound() {}

    public PacketGunReloadSound(WeaponSoundType soundType) {
        this.soundType = soundType;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeUtf(soundType.eventName);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.soundType = WeaponSoundType.fromString(buffer.readUtf());
    }

    @Override
    public void handleServerSide(Player player) {
        if (player instanceof ServerPlayer) {
            ItemStack gunStack = player.getMainHandItem();
            if (!gunStack.isEmpty() && gunStack.getItem() instanceof ItemGun itemGun) {
                itemGun.type.playSound(player, soundType, gunStack);
            }
        }
    }

    @Override
    public void handleClientSide(Player player) {}
}