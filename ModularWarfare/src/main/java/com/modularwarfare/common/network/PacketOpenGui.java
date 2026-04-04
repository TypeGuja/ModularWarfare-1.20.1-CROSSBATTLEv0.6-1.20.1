package com.modularwarfare.common.network;

import com.modularwarfare.common.handler.GuiHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketOpenGui extends PacketBase {
    public int guiID;

    public PacketOpenGui() {}

    public PacketOpenGui(int guiID) {
        this.guiID = guiID;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeInt(guiID);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.guiID = buffer.readInt();
    }

    @Override
    public void handleServerSide(Player player) {
        if (player instanceof ServerPlayer) {
            if (guiID == 0) {
                GuiHandler.openInventory(player);
            }
        }
    }

    @Override
    public void handleClientSide(Player player) {}
}