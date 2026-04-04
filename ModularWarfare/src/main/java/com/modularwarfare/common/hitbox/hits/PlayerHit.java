package com.modularwarfare.common.hitbox.hits;

import com.modularwarfare.common.hitbox.PlayerHitbox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

public class PlayerHit extends BulletHit {
    public PlayerHitbox hitbox;

    public PlayerHit(PlayerHitbox box, BlockHitResult result) {
        super(result);
        this.hitbox = box;
    }

    public Player getEntity() {
        return hitbox.player;
    }
}