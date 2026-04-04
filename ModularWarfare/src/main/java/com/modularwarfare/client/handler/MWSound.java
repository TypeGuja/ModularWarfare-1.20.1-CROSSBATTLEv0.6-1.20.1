package com.modularwarfare.client.handler;

import net.minecraft.core.BlockPos;

public class MWSound {
    public BlockPos pos;
    public String name;
    public float volume;
    public float pitch;

    public MWSound(BlockPos pos, String name, float volume, float pitch) {
        this.pos = pos;
        this.name = name;
        this.volume = volume;
        this.pitch = pitch;
    }
}