package com.modularwarfare.common.network;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.handler.ServerTickHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class PacketAimingRequest extends PacketBase {
    public UUID playerUUID;
    public boolean aiming;

    public PacketAimingRequest() {}

    public PacketAimingRequest(UUID playerUUID, boolean aiming) {
        this.playerUUID = playerUUID;
        this.aiming = aiming;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeUUID(playerUUID);
        buffer.writeBoolean(aiming);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.playerUUID = buffer.readUUID();
        this.aiming = buffer.readBoolean();
    }

    @Override
    public void handleServerSide(Player player) {
        if (player instanceof ServerPlayer) {
            // Исправлено: используем playerUUID везде
            if (!ServerTickHandler.playerAimShootCooldown.containsKey(playerUUID)) {
                ModularWarfare.NETWORK.sendToAll(new PacketAimingReponse(playerUUID, aiming));
            }
            ServerTickHandler.playerAimInstant.put(playerUUID, aiming);
        }
    }

    @Override
    public void handleClientSide(Player player) {}
}