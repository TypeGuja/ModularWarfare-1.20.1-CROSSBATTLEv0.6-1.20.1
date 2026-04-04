package com.modularwarfare.common.network;

import com.modularwarfare.client.model.InstantBulletRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PacketGunTrail extends PacketBase {
    double posX, posY, posZ;
    double motionX, motionZ;
    double dirX, dirY, dirZ;
    double range;
    float bulletspeed;
    boolean isPunched;

    public PacketGunTrail() {}

    public PacketGunTrail(double x, double y, double z, double motionX, double motionZ,
                          double dirX, double dirY, double dirZ, double range, float bulletspeed, boolean isPunched) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.motionX = motionX;
        this.motionZ = motionZ;
        this.dirX = dirX;
        this.dirY = dirY;
        this.dirZ = dirZ;
        this.range = range;
        this.bulletspeed = bulletspeed;
        this.isPunched = isPunched;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeDouble(posX);
        buffer.writeDouble(posY);
        buffer.writeDouble(posZ);
        buffer.writeDouble(motionX);
        buffer.writeDouble(motionZ);
        buffer.writeDouble(dirX);
        buffer.writeDouble(dirY);
        buffer.writeDouble(dirZ);
        buffer.writeDouble(range);
        buffer.writeFloat(bulletspeed);
        buffer.writeBoolean(isPunched);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.posX = buffer.readDouble();
        this.posY = buffer.readDouble();
        this.posZ = buffer.readDouble();
        this.motionX = buffer.readDouble();
        this.motionZ = buffer.readDouble();
        this.dirX = buffer.readDouble();
        this.dirY = buffer.readDouble();
        this.dirZ = buffer.readDouble();
        this.range = buffer.readDouble();
        this.bulletspeed = buffer.readFloat();
        this.isPunched = buffer.readBoolean();
    }

    @Override
    public void handleServerSide(Player player) {}

    @Override
    public void handleClientSide(Player player) {
        double dx = dirX * range;
        double dy = dirY * range;
        double dz = dirZ * range;
        Vec3 origin = new Vec3(posX, posY, posZ);
        Vec3 hit = new Vec3(origin.x + dx + motionX, origin.y + dy, origin.z + dz + motionZ);
        InstantBulletRenderer.addTrail(new InstantBulletRenderer.InstantShotTrail(origin, hit, bulletspeed, isPunched));
    }
}