package com.modularwarfare.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyModifier;

public class KeyBindingEnable extends KeyMapping {

    public KeyBindingEnable(KeyMapping keybinding) {
        super(keybinding.getName(), keybinding.getKey().getValue(), keybinding.getCategory());
    }

    public void setKeyModifierAndCode(KeyModifier keyModifier, InputConstants.Key keyCode) {
        super.setKeyModifierAndCode(keyModifier, keyCode);
    }
}