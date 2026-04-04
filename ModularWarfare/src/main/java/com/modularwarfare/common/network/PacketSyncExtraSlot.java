package com.modularwarfare.common.network;

import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncExtraSlot extends PacketBase {
    int playerId;
    int slot;
    ItemStack itemStack;

    public PacketSyncExtraSlot() {}

    public PacketSyncExtraSlot(Player player, int slot, ItemStack stack) {
        this.playerId = player.getId();
        this.slot = slot;
        this.itemStack = stack.copy();
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeInt(playerId);
        buffer.writeInt(slot);
        buffer.writeItem(itemStack);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.playerId = buffer.readInt();
        this.slot = buffer.readInt();
        this.itemStack = buffer.readItem();
    }

    @Override
    public void handleServerSide(Player player) {}

    @Override
    public void handleClientSide(Player player) {
        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(playerId);
                if (entity instanceof Player targetPlayer) {
                    targetPlayer.getCapability(CapabilityExtra.EXTRA_CAPABILITY).ifPresent(extra -> {
                        if (extra instanceof IExtraItemHandler) {
                            ((IExtraItemHandler) extra).setStackInSlot(slot, itemStack);
                        }
                    });
                }
            }
        });
    }
}