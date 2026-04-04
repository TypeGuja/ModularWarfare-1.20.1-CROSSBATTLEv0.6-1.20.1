package com.modularwarfare.client.input;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KeyBindingDisable extends KeyMapping {
    public KeyBindingDisable(KeyMapping keybinding) {
        super(keybinding.getName(), keybinding.getKey().getValue(), keybinding.getCategory());
    }

    @Override
    public boolean isDown() {
        return false;
    }

    @Override
    public boolean consumeClick() {
        return false;
    }
}