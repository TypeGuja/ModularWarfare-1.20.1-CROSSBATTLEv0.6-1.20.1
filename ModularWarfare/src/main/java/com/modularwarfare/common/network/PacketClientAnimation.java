package com.modularwarfare.common.network;

import com.modularwarfare.ModularWarfare;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class PacketClientAnimation extends PacketBase {
    private AnimationType animType;
    public String wepType;
    public int fireDelay;
    public float recoilPitch;
    public float recoilYaw;
    public int reloadTime;
    public int reloadCount;
    public int reloadType;

    public PacketClientAnimation() {}

    public PacketClientAnimation(AnimationType animType, String wepType) {
        this.animType = animType;
        this.wepType = wepType;
    }

    public PacketClientAnimation(String wepType, int fireDelay, float recoilPitch, float recoilYaw) {
        this(AnimationType.Shoot, wepType);
        this.fireDelay = fireDelay;
        this.recoilPitch = recoilPitch;
        this.recoilYaw = recoilYaw;
    }

    public PacketClientAnimation(String wepType, int reloadTime, int reloadCount, int reloadType) {
        this(AnimationType.Reload, wepType);
        this.reloadTime = reloadTime;
        this.reloadCount = reloadCount;
        this.reloadType = reloadType;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeByte(this.animType.i);
        buffer.writeUtf(this.wepType);
        switch (this.animType) {
            case Reload:
                buffer.writeInt(this.reloadTime);
                buffer.writeInt(this.reloadCount);
                buffer.writeInt(this.reloadType);
                break;
            case Shoot:
                buffer.writeInt(this.fireDelay);
                buffer.writeFloat(this.recoilPitch);
                buffer.writeFloat(this.recoilYaw);
                break;
        }
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.animType = AnimationType.getTypeFromInt(buffer.readByte());
        this.wepType = buffer.readUtf();
        switch (this.animType) {
            case Reload:
                this.reloadTime = buffer.readInt();
                this.reloadCount = buffer.readInt();
                this.reloadType = buffer.readInt();
                break;
            case Shoot:
                this.fireDelay = buffer.readInt();
                this.recoilPitch = buffer.readFloat();
                this.recoilYaw = buffer.readFloat();
                break;
        }
    }

    @Override
    public void handleServerSide(Player player) {}

    @Override
    public void handleClientSide(Player player) {
        switch (this.animType) {
            case Reload:
                ModularWarfare.PROXY.onReloadAnimation(player, this.wepType, this.reloadTime, this.reloadCount, this.reloadType);
                break;
            case Shoot:
                ModularWarfare.PROXY.onShootAnimation(player, this.wepType, this.fireDelay, this.recoilPitch, this.recoilYaw);
                break;
        }
    }

    private enum AnimationType {
        Shoot(0),
        Reload(1);

        public int i;

        AnimationType(int i) {
            this.i = i;
        }

        public static AnimationType getTypeFromInt(int i) {
            return i == 0 ? Shoot : Reload;
        }
    }
}