package com.modularwarfare.common.container;

import com.modularwarfare.common.backpacks.ItemBackpack;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBackpack extends SlotItemHandler {
    public SlotBackpack(IItemHandler inv, int index, int x, int y) {
        super(inv, index, x, y);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof ItemBackpack;
    }
}