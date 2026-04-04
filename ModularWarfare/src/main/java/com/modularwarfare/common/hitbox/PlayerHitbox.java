package com.modularwarfare.common.hitbox;

import com.modularwarfare.common.entity.debug.EntityDebugDot;
import com.modularwarfare.common.hitbox.maths.EnumHitboxType;
import com.modularwarfare.common.hitbox.maths.RotatedAxes;
import com.modularwarfare.common.vector.Vector3f;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PlayerHitbox {
    public Player player;
    public RotatedAxes axes;
    public Vector3f rP;
    public Vector3f o;
    public Vector3f d;
    public EnumHitboxType type;

    public PlayerHitbox(Player player, RotatedAxes axes, Vector3f rotationPoint, Vector3f origin, Vector3f dimensions, EnumHitboxType type) {
        this.player = player;
        this.axes = axes;
        this.o = origin;
        this.d = dimensions;
        this.type = type;
        this.rP = rotationPoint;
    }

    @OnlyIn(Dist.CLIENT)
    public void renderHitbox(Level level, Vector3f pos) {
        Vector3f pointMin = axes.findLocalVectorGlobally(new Vector3f(o.x, o.y, o.z));
        Vector3f pointMax = axes.findLocalVectorGlobally(new Vector3f(o.x + d.x, o.y + d.y, o.z + d.z));

        AABB hitbox = new AABB(
                player.getX() + rP.x + pointMin.x, player.getY() + rP.y + pointMin.y, player.getZ() + rP.z + pointMin.z,
                player.getX() + rP.x + pointMax.x, player.getY() + rP.y + pointMax.y, player.getZ() + rP.z + pointMax.z);

        if (!level.isClientSide) {
            level.addFreshEntity(new EntityDebugDot(level, new Vector3f(pos.x + rP.x + pointMin.x, pos.y + rP.y + pointMin.y, pos.z + rP.z + pointMin.z), 1, 0, 1, 0));
            level.addFreshEntity(new EntityDebugDot(level, new Vector3f(pos.x + rP.x + pointMax.x, pos.y + rP.y + pointMax.y, pos.z + rP.z + pointMax.z), 1, 1, 0, 0));
        }
    }

    public AABB getAxisAlignedBB(Vector3f pos) {
        Vector3f pointMin = axes.findLocalVectorGlobally(new Vector3f(o.x, o.y, o.z));
        Vector3f pointMax = axes.findLocalVectorGlobally(new Vector3f(o.x + d.x, o.y + d.y, o.z + d.z));

        return new AABB(
                pos.x + rP.x + pointMin.x, pos.y + rP.y + pointMin.y, pos.z + rP.z + pointMin.z,
                pos.x + rP.x + pointMax.x, pos.y + rP.y + pointMax.y, pos.z + rP.z + pointMax.z);
    }

    public boolean rayTrace(Vector3f origin, Vector3f motion) {
        Vector3f localOrigin = Vector3f.sub(origin, rP, new Vector3f());
        localOrigin = axes.findGlobalVectorLocally(localOrigin);
        Vector3f localMotion = axes.findGlobalVectorLocally(motion);

        if (Math.abs(localMotion.x) > 1e-6f) {
            float intersectTime;
            if (localOrigin.x < o.x) {
                intersectTime = (o.x - localOrigin.x) / localMotion.x;
                float intersectY = localOrigin.y + localMotion.y * intersectTime;
                float intersectZ = localOrigin.z + localMotion.z * intersectTime;
                if (intersectY >= o.y && intersectY <= o.y + d.y && intersectZ >= o.z && intersectZ <= o.z + d.z) {
                    return true;
                }
            } else if (localOrigin.x > o.x + d.x) {
                intersectTime = (o.x + d.x - localOrigin.x) / localMotion.x;
                float intersectY = localOrigin.y + localMotion.y * intersectTime;
                float intersectZ = localOrigin.z + localMotion.z * intersectTime;
                if (intersectY >= o.y && intersectY <= o.y + d.y && intersectZ >= o.z && intersectZ <= o.z + d.z) {
                    return true;
                }
            }
        }

        if (Math.abs(localMotion.y) > 1e-6f) {
            float intersectTime;
            if (localOrigin.y < o.y) {
                intersectTime = (o.y - localOrigin.y) / localMotion.y;
                float intersectX = localOrigin.x + localMotion.x * intersectTime;
                float intersectZ = localOrigin.z + localMotion.z * intersectTime;
                if (intersectX >= o.x && intersectX <= o.x + d.x && intersectZ >= o.z && intersectZ <= o.z + d.z) {
                    return true;
                }
            } else if (localOrigin.y > o.y + d.y) {
                intersectTime = (o.y + d.y - localOrigin.y) / localMotion.y;
                float intersectX = localOrigin.x + localMotion.x * intersectTime;
                float intersectZ = localOrigin.z + localMotion.z * intersectTime;
                if (intersectX >= o.x && intersectX <= o.x + d.x && intersectZ >= o.z && intersectZ <= o.z + d.z) {
                    return true;
                }
            }
        }

        if (Math.abs(localMotion.z) > 1e-6f) {
            float intersectTime;
            if (localOrigin.z < o.z) {
                intersectTime = (o.z - localOrigin.z) / localMotion.z;
                float intersectX = localOrigin.x + localMotion.x * intersectTime;
                float intersectY = localOrigin.y + localMotion.y * intersectTime;
                if (intersectX >= o.x && intersectX <= o.x + d.x && intersectY >= o.y && intersectY <= o.y + d.y) {
                    return true;
                }
            } else if (localOrigin.z > o.z + d.z) {
                intersectTime = (o.z + d.z - localOrigin.z) / localMotion.z;
                float intersectX = localOrigin.x + localMotion.x * intersectTime;
                float intersectY = localOrigin.y + localMotion.y * intersectTime;
                if (intersectX >= o.x && intersectX <= o.x + d.x && intersectY >= o.y && intersectY <= o.y + d.y) {
                    return true;
                }
            }
        }

        return false;
    }
}