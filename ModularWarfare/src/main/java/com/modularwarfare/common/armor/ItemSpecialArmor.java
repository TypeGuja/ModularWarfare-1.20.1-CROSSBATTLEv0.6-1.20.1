package com.modularwarfare.common.armor;

import com.modularwarfare.api.MWArmorType;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemSpecialArmor extends BaseItem {
    public ArmorType type;
    public MWArmorType armorType;
    public BaseType baseType;

    public ItemSpecialArmor(ArmorType type, MWArmorType armorType) {
        super(type);
        type.loadExtraValues();
        this.baseType = type;
        this.type = type;
        this.armorType = armorType;
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

    @Override
    public void setType(BaseType type) {
        this.type = (ArmorType) type;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player && stack.getTag() == null) {
            stack.getOrCreateTag().putInt("skinId", 0);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}