package com.modularwarfare.common.hitbox.hits;

import net.minecraft.world.phys.BlockHitResult;

public class BulletHit {
    public BlockHitResult rayTraceResult;

    public BulletHit(BlockHitResult result) {
        this.rayTraceResult = result;
    }
}