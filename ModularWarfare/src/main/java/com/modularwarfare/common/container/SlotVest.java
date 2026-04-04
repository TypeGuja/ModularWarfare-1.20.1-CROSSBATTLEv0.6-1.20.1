package com.modularwarfare.common.container;

import com.modularwarfare.api.MWArmorType;
import com.modularwarfare.common.armor.ItemSpecialArmor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotVest extends SlotItemHandler {
    public SlotVest(IItemHandler inv, int index, int x, int y) {
        super(inv, index, x, y);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.getItem() instanceof ItemSpecialArmor armor) {
            return armor.armorType == MWArmorType.Vest;
        }
        return false;
    }
}