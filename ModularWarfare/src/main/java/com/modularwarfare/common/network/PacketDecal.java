package com.modularwarfare.common.network;

import com.modularwarfare.common.entity.ModEntities;
import com.modularwarfare.common.entity.decals.EntityBulletHole;
import com.modularwarfare.common.entity.decals.EntityDecal;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class PacketDecal extends PacketBase {
    private int decalIndex;
    private int decalSide;
    private double decalX;
    private double decalY;
    private double decalZ;
    private boolean flag;

    public PacketDecal() {}

    public PacketDecal(int decalIndex, EntityDecal.EnumDecalSide side, double x, double y, double z, boolean flag) {
        this.decalIndex = decalIndex;
        this.decalSide = side.getId();
        this.decalX = x;
        this.decalY = y;
        this.decalZ = z;
        this.flag = flag;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeInt(decalIndex);
        buffer.writeInt(decalSide);
        buffer.writeDouble(decalX);
        buffer.writeDouble(decalY);
        buffer.writeDouble(decalZ);
        buffer.writeBoolean(flag);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.decalIndex = buffer.readInt();
        this.decalSide = buffer.readInt();
        this.decalX = buffer.readDouble();
        this.decalY = buffer.readDouble();
        this.decalZ = buffer.readDouble();
        this.flag = buffer.readBoolean();
    }

    @Override
    public void handleServerSide(Player player) {}

    @Override
    public void handleClientSide(Player player) {
        Minecraft.getInstance().execute(() -> {
            if (decalIndex == 0 && Minecraft.getInstance().level != null) {
                EntityBulletHole decal = new EntityBulletHole(ModEntities.BULLET_HOLE.get(), Minecraft.getInstance().level);
                decal.setSide(EntityDecal.EnumDecalSide.getEnumFromId(decalSide));
                decal.setPos(decalX, decalY, decalZ);
                Minecraft.getInstance().level.addFreshEntity(decal);
            }
        });
    }
}