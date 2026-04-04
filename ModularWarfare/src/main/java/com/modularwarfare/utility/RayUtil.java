package com.modularwarfare.utility;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.hitbox.PlayerHitbox;
import com.modularwarfare.common.hitbox.PlayerSnapshot;
import com.modularwarfare.common.hitbox.hits.BulletHit;
import com.modularwarfare.common.hitbox.hits.PlayerHit;
import com.modularwarfare.common.hitbox.playerdata.PlayerData;
import com.modularwarfare.common.hitbox.playerdata.PlayerDataHandler;
import com.modularwarfare.common.network.PacketBulletSnap;
import com.modularwarfare.common.network.PacketGunTrail;
import com.modularwarfare.common.network.PacketPlaySound;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

public class RayUtil {

    public static Vec3 getGunAccuracy(float pitch, float yaw, float accuracy, Random rand) {
        float randAccPitch = rand.nextFloat() * accuracy;
        float randAccYaw = rand.nextFloat() * accuracy;

        pitch += rand.nextBoolean() ? randAccPitch : -randAccPitch;
        yaw += rand.nextBoolean() ? randAccYaw : -randAccYaw;

        float f = (float) Math.cos(-yaw * (Math.PI / 180) - Math.PI);
        float f1 = (float) Math.sin(-yaw * (Math.PI / 180) - Math.PI);
        float f2 = -(float) Math.cos(-pitch * (Math.PI / 180));
        float f3 = (float) Math.sin(-pitch * (Math.PI / 180));

        return new Vec3(f1 * f2, f3, f * f2);
    }

    public static float calculateAccuracyServer(ItemGun item, LivingEntity player) {
        GunType gun = item.type;
        float acc = gun.bulletSpread;

        if (player.getX() - player.xo != 0 || player.getZ() - player.zo != 0) {
            acc += 0.75f;
        }
        if (!player.onGround()) {
            acc += 1.5f;
        }
        if (player.isSprinting()) {
            acc += 0.25f;
        }
        if (player.isCrouching()) {
            acc *= gun.accuracySneakFactor;
        }
        return acc;
    }

    public static float calculateAccuracyClient(ItemGun item, Player player) {
        GunType gun = item.type;
        float acc = gun.bulletSpread;

        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.keyUp.isDown() && !mc.options.keyDown.isDown() &&
                !mc.options.keyLeft.isDown() && !mc.options.keyRight.isDown()) {
            acc += 0.75f;
        }
        if (!player.onGround()) {
            acc += 1.5f;
        }
        if (player.isSprinting()) {
            acc += 0.25f;
        }
        if (player.isCrouching()) {
            acc *= gun.accuracySneakFactor;
        }
        return acc;
    }

    public static BulletHit standardEntityRayTrace(Level level, LivingEntity shooter, double range, ItemGun item, boolean isPunched) {
        HashSet<Entity> excluded = new HashSet<>();
        excluded.add(shooter);

        float accuracy = calculateAccuracyServer(item, shooter);
        Random rand = new Random();
        Vec3 dir = getGunAccuracy(shooter.getXRot(), shooter.getYRot(), accuracy, rand);

        double dx = dir.x * range;
        double dy = dir.y * range;
        double dz = dir.z * range;

        ModularWarfare.NETWORK.sendToDimension(new PacketGunTrail(
                shooter.getX(), shooter.getY() + shooter.getEyeHeight() - 0.1, shooter.getZ(),
                shooter.getDeltaMovement().x, shooter.getDeltaMovement().z,
                dir.x, dir.y, dir.z, range, 10.0f, isPunched), level.dimension());

        int ping = 0;
        if (shooter instanceof ServerPlayer) {
            ping = 50; // Default ping value
        }

        return tracePath(level, (float) shooter.getX(), (float) (shooter.getY() + shooter.getEyeHeight() - 0.1f),
                (float) shooter.getZ(), (float) (shooter.getX() + dx + shooter.getDeltaMovement().x),
                (float) (shooter.getY() + dy + shooter.getDeltaMovement().y),
                (float) (shooter.getZ() + dz + shooter.getDeltaMovement().z), 0.001f, excluded, false, ping);
    }

    public static BulletHit tracePath(Level level, float x, float y, float z, float tx, float ty, float tz,
                                      float borderSize, HashSet<Entity> excluded, boolean collideablesOnly, int ping) {
        Vec3 startVec = new Vec3(x, y, z);
        Vec3 endVec = new Vec3(tx, ty, tz);

        BlockHitResult blockHit = rayTraceBlocks(level, startVec, endVec);

        float maxDistance = (float) endVec.distanceTo(startVec);
        Vec3 realVecEnd = endVec;

        if (blockHit != null && blockHit.getType() == HitResult.Type.BLOCK) {
            maxDistance = (float) blockHit.getLocation().distanceTo(startVec);
            realVecEnd = blockHit.getLocation();
        }

        for (Entity entity : level.getEntitiesOfClass(Player.class, new AABB(x, y, z, tx, ty, tz).inflate(2.0))) {
            if ((excluded != null && excluded.contains(entity)) || !(entity instanceof Player player)) continue;

            PlayerData data = PlayerDataHandler.getPlayerData(player);
            int snapshotToTry = ping / 50;
            if (snapshotToTry >= data.snapshots.length) {
                snapshotToTry = data.snapshots.length - 1;
            }
            PlayerSnapshot snapshot = data.snapshots[snapshotToTry];
            if (snapshot == null) {
                snapshot = data.snapshots[0];
            }
            if (snapshot == null) continue;

            for (PlayerHitbox hitbox : snapshot.hitboxes) {
                AABB hitboxAABB = hitbox.getAxisAlignedBB(snapshot.pos);
                Optional<Vec3> interceptOpt = hitboxAABB.clip(startVec, realVecEnd);
                if (interceptOpt.isPresent()) {
                    Vec3 intercept = interceptOpt.get();
                    ModularWarfare.NETWORK.sendTo(new PacketPlaySound(player.blockPosition(), "flyby", 1.0f, 1.0f),
                            (ServerPlayer) player);
                    ModularWarfare.NETWORK.sendTo(new PacketBulletSnap(), (ServerPlayer) player);
                    return new PlayerHit(hitbox, new BlockHitResult(intercept, ((BlockHitResult)blockHit).getDirection(), BlockPos.containing(intercept), false));
                }
            }
        }

        return new BulletHit(blockHit);
    }

    public static BlockHitResult rayTraceBlocks(Level level, Vec3 start, Vec3 end) {
        return level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
    }
}