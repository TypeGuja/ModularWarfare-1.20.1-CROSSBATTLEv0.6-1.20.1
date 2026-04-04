package com.modularwarfare.common.hitbox;

import com.modularwarfare.api.AnimationUtils;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.handler.ServerTickHandler;
import com.modularwarfare.common.hitbox.maths.EnumHitboxType;
import com.modularwarfare.common.hitbox.maths.RotatedAxes;
import com.modularwarfare.common.vector.Vector3f;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class PlayerSnapshot {
    public Player player;
    public Vector3f pos;
    public List<PlayerHitbox> hitboxes;
    public long time;

    public PlayerSnapshot(Player p) {
        this.player = p;
        this.pos = new Vector3f((float) p.getX(), (float) p.getY(), (float) p.getZ());
        this.hitboxes = new ArrayList<>();

        RotatedAxes bodyAxes = new RotatedAxes(p.getYRot(), 0.0f, 0.0f);
        RotatedAxes headAxes = new RotatedAxes(p.getYHeadRot() - p.getYRot(), p.getXRot(), 0.0f);

        if (p.isCrouching()) {
            hitboxes.add(new PlayerHitbox(this.player, bodyAxes, new Vector3f(0, 0, 0),
                    new Vector3f(-0.25f, 0, -0.15f), new Vector3f(0.5f, 1.0f, 0.3f), EnumHitboxType.BODY));
            hitboxes.add(new PlayerHitbox(this.player, bodyAxes.findLocalAxesGlobally(headAxes), new Vector3f(0, 1.0f, 0),
                    new Vector3f(-0.25f, 0, -0.25f), new Vector3f(0.5f, 0.5f, 0.5f), EnumHitboxType.HEAD));
        } else {
            hitboxes.add(new PlayerHitbox(this.player, bodyAxes, new Vector3f(0, 0, 0),
                    new Vector3f(-0.25f, 0, -0.15f), new Vector3f(0.5f, 1.4f, 0.3f), EnumHitboxType.BODY));
            hitboxes.add(new PlayerHitbox(this.player, bodyAxes.findLocalAxesGlobally(headAxes), new Vector3f(0, 1.4f, 0),
                    new Vector3f(-0.25f, 0, -0.25f), new Vector3f(0.5f, 0.5f, 0.5f), EnumHitboxType.HEAD));
        }

        float yHead = (p.getYHeadRot() - p.getYRot()) / 57.295776f;
        float xHead = p.getXRot() / 57.295776f;
        float yRight, yLeft, xRight, xLeft;

        if (!p.getMainHandItem().isEmpty() && p.getMainHandItem().getItem() instanceof ItemGun) {
            if (p.level().isClientSide) {
                if (AnimationUtils.isAiming.getOrDefault(p.getName().getString(), false)) {
                    yRight = -0.1f + yHead - 1.5707964f;
                    yLeft = 0.1f + yHead + 0.4f - 1.5707964f;
                    xRight = -1.5707964f + xHead;
                    xLeft = -1.5707964f + xHead;
                } else {
                    yRight = -1.8407964f;
                    yLeft = -1.2407963f;
                    xRight = -0.8717918f;
                    xLeft = -0.8717918f;
                }
            } else {
                if (ServerTickHandler.playerAimShootCooldown.containsKey(p.getName().getString())) {
                    yRight = -0.1f + yHead - 1.5707964f;
                    yLeft = 0.1f + yHead + 0.4f - 1.5707964f;
                    xRight = -1.5707964f + xHead;
                    xLeft = -1.5707964f + xHead;
                } else {
                    yRight = -1.8407964f;
                    yLeft = -1.2407963f;
                    xRight = -0.8717918f;
                    xLeft = -0.8717918f;
                }
            }
        } else {
            yRight = -1.8407964f;
            yLeft = -1.2407963f;
            xRight = -0.8717918f;
            xLeft = -0.8717918f;
        }

        RotatedAxes leftArmAxes = new RotatedAxes()
                .rotateGlobalPitchInRads(xLeft)
                .rotateGlobalYawInRads((float) Math.PI + yLeft);
        RotatedAxes rightArmAxes = new RotatedAxes()
                .rotateGlobalPitchInRads(xRight)
                .rotateGlobalYawInRads((float) Math.PI + yRight);

        float originZRight = (float) Math.sin(-p.getYRot() * Math.PI / 180) * 5.0f / 16.0f;
        float originXRight = (float) -Math.cos(-p.getYRot() * Math.PI / 180) * 5.0f / 16.0f;
        float originZLeft = (float) -Math.sin(-p.getYRot() * Math.PI / 180) * 5.0f / 16.0f;
        float originXLeft = (float) Math.cos(-p.getYRot() * Math.PI / 180) * 5.0f / 16.0f;

        if (p.isCrouching()) {
            hitboxes.add(new PlayerHitbox(this.player, bodyAxes.findLocalAxesGlobally(leftArmAxes),
                    new Vector3f(originXLeft, 0.9f, originZLeft),
                    new Vector3f(-0.125f, -0.6f, -0.125f), new Vector3f(0.25f, 0.7f, 0.25f), EnumHitboxType.LEFTARM));
            hitboxes.add(new PlayerHitbox(this.player, bodyAxes.findLocalAxesGlobally(rightArmAxes),
                    new Vector3f(originXRight, 0.9f, originZRight),
                    new Vector3f(-0.125f, -0.6f, -0.125f), new Vector3f(0.25f, 0.7f, 0.25f), EnumHitboxType.RIGHTARM));
        } else {
            hitboxes.add(new PlayerHitbox(this.player, bodyAxes.findLocalAxesGlobally(leftArmAxes),
                    new Vector3f(originXLeft, 1.3f, originZLeft),
                    new Vector3f(-0.125f, -0.6f, -0.125f), new Vector3f(0.25f, 0.7f, 0.25f), EnumHitboxType.LEFTARM));
            hitboxes.add(new PlayerHitbox(this.player, bodyAxes.findLocalAxesGlobally(rightArmAxes),
                    new Vector3f(originXRight, 1.3f, originZRight),
                    new Vector3f(-0.125f, -0.6f, -0.125f), new Vector3f(0.25f, 0.7f, 0.25f), EnumHitboxType.RIGHTARM));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void renderSnapshot() {
        for (PlayerHitbox hitbox : hitboxes) {
            hitbox.renderHitbox(player.level(), pos);
        }
    }

    public PlayerHitbox getHitbox(EnumHitboxType type) {
        for (PlayerHitbox hitbox : hitboxes) {
            if (hitbox.type == type) {
                return hitbox;
            }
        }
        return null;
    }
}