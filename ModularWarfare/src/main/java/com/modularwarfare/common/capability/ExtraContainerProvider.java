package com.modularwarfare.common.capability.extraslots;

import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.ExtraContainer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtraContainerProvider implements ICapabilityProvider {
    private final ExtraContainer container;
    private final LazyOptional<ExtraContainer> instance;

    public ExtraContainerProvider(ExtraContainer container) {
        this.container = container;
        this.instance = LazyOptional.of(() -> this.container);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityExtra.EXTRA_CAPABILITY.orEmpty(cap, instance.cast());
    }
}