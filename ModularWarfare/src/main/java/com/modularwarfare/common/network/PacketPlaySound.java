package com.modularwarfare.common.network;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.utility.MWSound;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketPlaySound extends PacketBase {
    public int posX, posY, posZ;
    public String soundName;
    public float volume;
    public float pitch;

    public PacketPlaySound() {}

    public PacketPlaySound(BlockPos pos, String soundName, float volume, float pitch) {
        this.posX = pos.getX();
        this.posY = pos.getY();
        this.posZ = pos.getZ();
        this.soundName = soundName;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeInt(posX);
        buffer.writeInt(posY);
        buffer.writeInt(posZ);
        buffer.writeUtf(soundName);
        buffer.writeFloat(volume);
        buffer.writeFloat(pitch);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.posX = buffer.readInt();
        this.posY = buffer.readInt();
        this.posZ = buffer.readInt();
        this.soundName = buffer.readUtf();
        this.volume = buffer.readFloat();
        this.pitch = buffer.readFloat();
    }

    @Override
    public void handleServerSide(Player player) {}

    @Override
    public void handleClientSide(Player player) {
        ModularWarfare.PROXY.playSound(new MWSound(new BlockPos(posX, posY, posZ), soundName, volume, pitch));
    }
}