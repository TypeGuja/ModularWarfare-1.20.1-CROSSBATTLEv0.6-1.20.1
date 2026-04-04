package com.modularwarfare.client.input;

import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;

public class KeyEntry {
    public KeyType keyType;
    public KeyMapping keyBinding;

    public KeyEntry(KeyType keyType) {
        this.keyType = keyType;
        this.keyBinding = new KeyMapping(
                keyType.displayName,
                InputConstants.Type.KEYSYM,
                keyType.keyCode,
                "ModularWarfare"
        );
    }
}