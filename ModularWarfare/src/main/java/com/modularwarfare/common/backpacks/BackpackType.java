package com.modularwarfare.common.backpacks;

import com.google.gson.Gson;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.config.BackpackRenderConfig;
import com.modularwarfare.client.model.ModelBackpack;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackpackType extends BaseType {
    public int size = 16;
    public boolean allowSmallerBackpackStorage = false;
    public Integer maxWeaponStorage = null;

    @Override
    public void loadExtraValues() {
        if (maxStackSize == null) {
            maxStackSize = 1;
        }
        loadBaseValues();
    }

    @Override
    public void reloadModel() {
        this.model = new ModelBackpack(ModularWarfare.getRenderConfig(this, new Gson(), BackpackRenderConfig.class), this);
    }

    @Override
    public String getAssetDir() {
        return "backpacks";
    }

    public static class Provider implements ICapabilityProvider {
        final IItemHandlerModifiable items;
        private final LazyOptional<IItemHandler> holder;

        public Provider(BackpackType type) {
            this.items = new ItemStackHandler(type.size);
            this.holder = LazyOptional.of(() -> this.items);
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, holder.cast());
        }
    }
}