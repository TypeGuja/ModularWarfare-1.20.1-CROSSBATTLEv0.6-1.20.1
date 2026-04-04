package com.modularwarfare.common.backpacks;

import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

public class ItemBackpack extends BaseItem {
    public BackpackType type;

    public ItemBackpack(BackpackType type) {
        super(type);
        this.type = type;
        this.render3d = false;
    }

    @Override
    public void setType(BaseType type) {
        this.type = (BackpackType) type;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new BackpackType.Provider(this.type);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        player.getCapability(CapabilityExtra.EXTRA_CAPABILITY).ifPresent(extra -> {
            if (extra.getStackInSlot(0).isEmpty()) {
                extra.setStackInSlot(0, stack.copy());
                stack.setCount(0);
            }
        });

        return InteractionResultHolder.pass(stack);
    }

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag tags = super.getShareTag(stack);
        if (tags == null) tags = new CompoundTag();

        final CompoundTag finalTags = tags;
        stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(items -> {
            if (items instanceof IItemHandlerModifiable) {
                CompoundTag itemsTag = new CompoundTag();
                for (int i = 0; i < items.getSlots(); i++) {
                    itemsTag.put("slot_" + i, items.getStackInSlot(i).save(new CompoundTag()));
                }
                finalTags.put("_items", itemsTag);
            }
        });

        return finalTags;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);

        if (nbt != null && nbt.contains("_items")) {
            final CompoundTag itemsTag = nbt.getCompound("_items");
            stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(items -> {
                if (items instanceof IItemHandlerModifiable) {
                    for (int i = 0; i < items.getSlots(); i++) {
                        String key = "slot_" + i;
                        if (itemsTag.contains(key)) {
                            ((IItemHandlerModifiable) items).setStackInSlot(i, ItemStack.of(itemsTag.getCompound(key)));
                        }
                    }
                }
            });
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}