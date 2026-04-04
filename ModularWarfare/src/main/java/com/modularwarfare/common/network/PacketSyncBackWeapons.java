package com.modularwarfare.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class PacketSyncBackWeapons extends PacketBase {
    private CompoundTag tag;

    public PacketSyncBackWeapons() {
        this.tag = new CompoundTag();
    }

    public PacketSyncBackWeapons(CompoundTag tag) {
        this.tag = tag;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeNbt(tag);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.tag = buffer.readNbt();
    }

    @Override
    public void handleServerSide(Player player) {}

    @Override
    public void handleClientSide(Player player) {}
}