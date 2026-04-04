package com.modularwarfare.common.capability.extraslots;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.network.PacketSyncExtraSlot;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ExtraContainer extends ItemStackHandler implements IExtraItemHandler {
    private Player player;

    public ExtraContainer(Player player) {
        super(5);
        this.player = player;
    }

    public ExtraContainer() {
        super(5);
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (player != null && !player.level().isClientSide) {
            PacketSyncExtraSlot packet = new PacketSyncExtraSlot(player, slot, getStackInSlot(slot));
            for (ServerPlayer tracking : player.level().getServer().getPlayerList().getPlayers()) {
                ModularWarfare.NETWORK.sendTo(packet, tracking);
            }
        }
    }
}