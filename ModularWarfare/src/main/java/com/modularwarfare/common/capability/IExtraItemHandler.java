package com.modularwarfare.common.capability.extraslots;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IExtraItemHandler extends IItemHandlerModifiable {
    void setPlayer(Player player);
}