package com.modularwarfare.common.network;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.guns.WeaponFireMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class PacketGunFire extends PacketBase {
    public String internalname;
    public int fireTickDelay;
    public float recoilPitch;
    public float recoilYaw;
    public float recoilAimReducer;
    public float bulletSpread;

    public PacketGunFire() {}

    public PacketGunFire(String internalname, int fireTickDelay, float recoilPitch, float recoilYaw,
                         float recoilAimReducer, float bulletSpread) {
        this.internalname = internalname;
        this.fireTickDelay = fireTickDelay;
        this.recoilPitch = recoilPitch;
        this.recoilYaw = recoilYaw;
        this.recoilAimReducer = recoilAimReducer;
        this.bulletSpread = bulletSpread;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.internalname);
        buffer.writeInt(this.fireTickDelay);
        buffer.writeFloat(this.recoilPitch);
        buffer.writeFloat(this.recoilYaw);
        buffer.writeFloat(this.recoilAimReducer);
        buffer.writeFloat(this.bulletSpread);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.internalname = buffer.readUtf();
        this.fireTickDelay = buffer.readInt();
        this.recoilPitch = buffer.readFloat();
        this.recoilYaw = buffer.readFloat();
        this.recoilAimReducer = buffer.readFloat();
        this.bulletSpread = buffer.readFloat();
    }

    @Override
    public void handleServerSide(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            RegistryObject<Item> registryObject = ModularWarfare.gunRegistry.get(this.internalname);
            if (registryObject == null) {
                return;
            }
            if (!(registryObject.get() instanceof ItemGun itemGun)) {
                return;
            }
            ItemStack gunStack = player.getMainHandItem();
            WeaponFireMode fireMode = GunType.getFireMode(gunStack);
            if (fireMode != null) {
                itemGun.fireServer(player, player.level(), gunStack, itemGun, fireMode,
                        this.fireTickDelay, this.recoilPitch, this.recoilYaw,
                        this.recoilAimReducer, this.bulletSpread);
            }
        }
    }

    @Override
    public void handleClientSide(Player player) {}
}