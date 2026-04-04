package com.modularwarfare.common.entity.debug;

import com.modularwarfare.common.entity.ModEntities;
import com.modularwarfare.common.vector.Vector3f;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityDebugDot extends EntityDebugColor {
    public int life = 1000;

    public EntityDebugDot(EntityType<?> type, Level level) {
        super(type, level);
        this.setBoundingBox(new AABB(-0.125, -0.125, -0.125, 0.125, 0.125, 0.125));
    }

    public EntityDebugDot(Level w, Vector3f pos, int l, float r, float g, float b) {
        this(ModEntities.DEBUG_DOT.get(), w);
        this.setPos(pos.x, pos.y, pos.z);
        this.setColor(r, g, b);
        this.life = l;
    }

    @Override
    public void tick() {
        this.life--;
        if (this.life <= 0) {
            this.discard();
        }
    }
}