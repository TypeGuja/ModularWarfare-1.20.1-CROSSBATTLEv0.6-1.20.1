package com.modularwarfare.common.network;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.handler.ServerTickHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketAimingRequest extends PacketBase {
    public String playername;
    public boolean aiming;

    public PacketAimingRequest() {}

    public PacketAimingRequest(String playername, boolean aiming) {
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
    public void handleServerSide(Player player) {
        if (player instanceof ServerPlayer) {
            if (!ServerTickHandler.playerAimShootCooldown.containsKey(playername)) {
                ModularWarfare.NETWORK.sendToAll(new PacketAimingReponse(playername, aiming));
            }
            ServerTickHandler.playerAimInstant.put(playername, aiming);
        }
    }

    @Override
    public void handleClientSide(Player player) {}
}