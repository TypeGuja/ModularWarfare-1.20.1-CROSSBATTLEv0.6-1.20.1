package com.modularwarfare.common.container;

import com.modularwarfare.common.backpacks.ItemBackpack;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import com.modularwarfare.common.guns.ItemGun;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ContainerInventoryModified extends RecipeBookMenu<CraftingContainer> {
    public final CraftingContainer craftMatrix;
    public final ResultContainer craftResult = new ResultContainer();
    public IExtraItemHandler extra;
    private final Player player;
    private static final EquipmentSlot[] EQUIPMENT_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    public ContainerInventoryModified(int id, Inventory playerInv, boolean isLocalWorld, Player player) {
        super(MenuType.CRAFTING, id);
        this.player = player;
        // Убрано лишнее приведение типа
        this.extra = player.getCapability(CapabilityExtra.EXTRA_CAPABILITY).orElse(null);
        this.craftMatrix = new TransientCraftingContainer(this, 2, 2);

        addSlots(playerInv, player);
    }

    private List<Slot> backpackSlots = new ArrayList<>();
    private void addSlots(Inventory playerInv, Player player) {
        // Crafting result slot
        addSlot(new ResultSlot(playerInv.player, this.craftMatrix, this.craftResult, 0, 154, 28));

        // Crafting grid (2x2)
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                addSlot(new Slot(this.craftMatrix, j + i * 2, 116 + j * 18, 18 + i * 18));
            }
        }

        // Armor slots
        for (int k = 0; k < 4; k++) {
            final EquipmentSlot slot = EQUIPMENT_SLOTS[k];
            addSlot(new ArmorSlot(playerInv, playerInv.player, slot, 8, 8 + k * 18));
        }

        // Main inventory (3 rows)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInv, j + (i + 1) * 9, 8 + j * 18, 90 + i * 18));
            }
        }

        // Hotbar
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInv, i, 8 + i * 18, 154));
        }

        // Offhand slot
        addSlot(new Slot(playerInv, 40, 76, 62));

        // Backpack slot
        if (extra != null) {
            addSlot(new SlotBackpack(extra, 0, 76, 8) {
                @Override
                public void setChanged() {
                    updateBackpack();
                    addSlots(playerInv, player);
                    super.setChanged();
                }
            });

            // Vest slot
            addSlot(new SlotVest(extra, 1, 76, 26) {
                @Override
                public void setChanged() {
                    addSlots(playerInv, player);
                    super.setChanged();
                }
            });
        }

        updateBackpack();
    }

    private void updateBackpack() {
        if (extra == null) return;

        // Удаляем старые слоты рюкзака
        for (Slot slot : backpackSlots) {
            this.slots.remove(slot);
        }
        backpackSlots.clear();

        ItemStack backpack = extra.getStackInSlot(0);
        if (backpack.isEmpty()) return;

        backpack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(backpackInv -> {
            int xP = 0;
            int yP = 0;

            for (int i = 0; i < backpackInv.getSlots(); i++) {
                final int slotIndex = i;
                SlotItemHandler slot = new SlotItemHandler(backpackInv, i, 181 + xP * 18, 18 + yP * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        // Ваши проверки
                        return true;
                    }
                };

                this.addSlot(slot);
                backpackSlots.add(slot);

                if (++xP % 4 == 0) {
                    xP = 0;
                    yP++;
                }
            }
        });
    }

    @Override
    public void slotsChanged(Container inventory) {
        this.slotsChanged(this.craftMatrix);
        super.slotsChanged(inventory);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.craftResult.clearContent();
        this.clearContainer(player, this.craftMatrix);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents stackedContents) {
        this.craftMatrix.fillStackedContents(stackedContents);
    }

    @Override
    public void clearCraftingContent() {
        this.craftMatrix.clearContent();
        this.craftResult.clearContent();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipe) {
        return recipe.matches(this.craftMatrix, this.player.level());
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return 2;
    }

    @Override
    public int getGridHeight() {
        return 2;
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == 0) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 1 && index < 5) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5 && index < 9) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 9 && index < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 36 && index < 45) {
                if (!this.moveItemStackTo(itemstack1, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.craftResult && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int index) {
        return index != 0;
    }
}