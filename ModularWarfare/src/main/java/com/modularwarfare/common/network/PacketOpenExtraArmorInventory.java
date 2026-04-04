package com.modularwarfare.common.network;

import com.modularwarfare.common.handler.GuiHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketOpenExtraArmorInventory extends PacketBase {

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {}

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {}

    @Override
    public void handleServerSide(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            GuiHandler.openInventory(serverPlayer);
        }
    }

    @Override
    public void handleClientSide(Player player) {}
}