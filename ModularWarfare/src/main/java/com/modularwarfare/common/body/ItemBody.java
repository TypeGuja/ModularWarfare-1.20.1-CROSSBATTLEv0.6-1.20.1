package com.modularwarfare.common.body;

import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemBody extends BaseItem {
    public BodyType type;

    public ItemBody(BodyType type) {
        super(type);
        this.type = type;
    }

    @Override
    public void setType(BaseType type) {
        this.type = (BodyType) type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        player.getCapability(CapabilityExtra.EXTRA_CAPABILITY).ifPresent(extra -> {
            if (extra.getStackInSlot(1).isEmpty()) {
                extra.setStackInSlot(1, stack.copy());
                stack.setCount(0);
            }
        });

        return InteractionResultHolder.pass(stack);
    }
}