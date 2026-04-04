package com.modularwarfare.common.guns;

import com.modularwarfare.common.type.BaseItem;
import net.minecraft.world.item.ItemStack;

public class ItemSpray extends BaseItem {
    public SprayType type;

    public ItemSpray(SprayType type) {
        super(type);
        this.type = type;
        this.render3d = false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}