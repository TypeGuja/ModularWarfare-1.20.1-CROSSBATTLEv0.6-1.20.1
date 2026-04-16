package com.modularwarfare.common.network;

import com.modularwarfare.api.AnimationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class PacketAimingReponse extends PacketBase {
    public UUID playerUUID;  // Изменено с String на UUID
    public boolean aiming;

    public PacketAimingReponse() {}

    public PacketAimingReponse(UUID playerUUID, boolean aiming) {  // Изменён конструктор
        this.playerUUID = playerUUID;
        this.aiming = aiming;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeUUID(playerUUID);  // Изменено с writeUtf на writeUUID
        buffer.writeBoolean(aiming);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.playerUUID = buffer.readUUID();  // Изменено с readUtf на readUUID
        this.aiming = buffer.readBoolean();
    }

    @Override
    public void handleServerSide(Player player) {}

    @Override
    public void handleClientSide(Player player) {
        Minecraft.getInstance().execute(() -> {
            if (aiming) {
                AnimationUtils.isAiming.put(playerUUID, aiming);  // Теперь playerUUID - это UUID
            } else {
                AnimationUtils.isAiming.remove(playerUUID);
            }
        });
    }
}