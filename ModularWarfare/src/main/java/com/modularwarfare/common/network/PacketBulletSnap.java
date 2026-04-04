package com.modularwarfare.common.network;

import com.modularwarfare.client.hud.GunUI;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class PacketBulletSnap extends PacketBase {

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {}

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {}

    @Override
    public void handleServerSide(Player player) {}

    @Override
    public void handleClientSide(Player player) {
        Minecraft.getInstance().execute(() -> {
            GunUI.bulletSnapFade += 0.25f;
            if (GunUI.bulletSnapFade > 0.8f) {
                GunUI.bulletSnapFade = 0.8f;
            }
        });
    }
}