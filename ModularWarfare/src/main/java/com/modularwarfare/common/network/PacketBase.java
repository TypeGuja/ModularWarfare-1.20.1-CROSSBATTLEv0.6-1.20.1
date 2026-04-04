package com.modularwarfare.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class PacketBase {

    public void encodeInto(FriendlyByteBuf buffer) {}

    public void decodeInto(FriendlyByteBuf buffer) {}

    public void handleServerSide(Player player) {}

    public void handleClientSide(Player player) {}

    public static void handle(PacketBase packet, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isServer()) {
                packet.handleServerSide(ctx.getSender());
            } else {
                packet.handleClientSide(net.minecraft.client.Minecraft.getInstance().player);
            }
        });
        ctx.setPacketHandled(true);
    }
}