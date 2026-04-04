package com.modularwarfare.common.guns;

import com.modularwarfare.common.type.BaseItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemAttachment extends BaseItem {
    public AttachmentType type;

    public ItemAttachment(AttachmentType type) {
        super(type);
        this.type = type;
        this.render3d = true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player && stack.getTag() == null) {
            var tag = stack.getOrCreateTag();
            tag.putInt("skinId", 1);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}