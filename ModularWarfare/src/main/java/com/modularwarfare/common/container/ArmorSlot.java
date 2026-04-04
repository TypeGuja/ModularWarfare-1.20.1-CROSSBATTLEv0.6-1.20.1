package com.modularwarfare.common.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class ArmorSlot extends Slot {
    private final Player player;
    private final EquipmentSlot slot;

    public ArmorSlot(Container inventory, Player player, EquipmentSlot slot, int x, int y) {
        super(inventory, slot.getIndex(), x, y);
        this.player = player;
        this.slot = slot;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getEquipmentSlot() == slot;
    }
}