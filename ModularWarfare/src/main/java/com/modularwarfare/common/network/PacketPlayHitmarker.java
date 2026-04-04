package com.modularwarfare.common.network;

import com.modularwarfare.ModularWarfare;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class PacketPlayHitmarker extends PacketBase {
    public boolean headshot;

    public PacketPlayHitmarker() {}

    public PacketPlayHitmarker(boolean headshot) {
        this.headshot = headshot;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeBoolean(headshot);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.headshot = buffer.readBoolean();
    }

    @Override
    public void handleServerSide(Player player) {}

    @Override
    public void handleClientSide(Player player) {
        ModularWarfare.PROXY.playHitmarker(headshot);
    }
}