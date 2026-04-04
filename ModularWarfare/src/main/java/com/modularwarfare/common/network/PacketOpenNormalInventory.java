package com.modularwarfare.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketOpenNormalInventory extends PacketBase {

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {}

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {}

    @Override
    public void handleServerSide(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.containerMenu.removed(serverPlayer);
            serverPlayer.containerMenu = serverPlayer.inventoryMenu;
        }
    }

    @Override
    public void handleClientSide(Player player) {}
}