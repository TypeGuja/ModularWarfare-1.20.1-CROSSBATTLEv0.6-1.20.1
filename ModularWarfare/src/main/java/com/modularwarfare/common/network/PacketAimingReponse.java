package com.modularwarfare.common.network;

import com.modularwarfare.api.AnimationUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class PacketAimingReponse extends PacketBase {
    public String playername;
    public boolean aiming;

    public PacketAimingReponse() {}

    public PacketAimingReponse(String playername, boolean aiming) {
        this.playername = playername;
        this.aiming = aiming;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeUtf(playername);
        buffer.writeBoolean(aiming);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.playername = buffer.readUtf();
        this.aiming = buffer.readBoolean();
    }

    @Override
    public void handleServerSide(Player player) {}

    @Override
    public void handleClientSide(Player player) {
        if (aiming) {
            AnimationUtils.isAiming.put(playername, aiming);
        } else {
            AnimationUtils.isAiming.remove(playername);
        }
    }
}