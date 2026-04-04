package com.modularwarfare.common.network;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.api.WeaponReloadEvent;
import com.modularwarfare.client.anim.ReloadType;
import com.modularwarfare.common.guns.AmmoType;
import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemAmmo;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.guns.WeaponSoundType;
import com.modularwarfare.common.handler.ServerTickHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class PacketGunReload extends PacketBase {
    public boolean unload = false;

    public PacketGunReload() {}

    public PacketGunReload(boolean unload) {
        this.unload = unload;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeBoolean(unload);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.unload = buffer.readBoolean();
    }

    @Override
    public void handleServerSide(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack gunStack = player.getMainHandItem();

            if (gunStack.getItem() instanceof ItemGun itemGun) {
                GunType gunType = itemGun.type;

                if (gunType.acceptedAmmo != null) {
                    handleMagReload(serverPlayer, gunStack, itemGun, gunType);
                } else {
                    handleBulletReload(serverPlayer, gunStack, itemGun, gunType);
                }
            }
        }
    }

    private void handleMagReload(ServerPlayer player, ItemStack gunStack, ItemGun itemGun, GunType gunType) {
        if (ServerTickHandler.playerReloadCooldown.containsKey(player.getUUID())) return;

        ItemStack ammoToLoad = null;
        int ammoSlot = -1;

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof ItemAmmo ammo) {
                for (String accepted : gunType.acceptedAmmo) {
                    if (accepted.equals(ammo.baseType.internalName)) {
                        ammoToLoad = stack;
                        ammoSlot = i;
                        break;
                    }
                }
            }
        }

        if (ammoToLoad == null) return;

        WeaponReloadEvent.Pre preEvent = new WeaponReloadEvent.Pre(player, gunStack, itemGun, false, false);
        MinecraftForge.EVENT_BUS.post(preEvent);
        if (preEvent.isCanceled()) return;

        if (ItemGun.hasAmmoLoaded(gunStack)) {
            ItemStack oldAmmo = ItemStack.of(gunStack.getOrCreateTag().getCompound("ammo"));
            if (!oldAmmo.isEmpty()) {
                player.getInventory().add(oldAmmo);
            }
            gunStack.getTag().remove("ammo");
        }

        ItemStack newAmmo = ammoToLoad.copy();
        newAmmo.setCount(1);
        gunStack.getOrCreateTag().put("ammo", newAmmo.save(new CompoundTag()));
        ammoToLoad.shrink(1);

        int reloadTime = (int) (preEvent.getReloadTime() * ((ItemAmmo) ammoToLoad.getItem()).type.reloadTimeFactor);

        WeaponReloadEvent.Post postEvent = new WeaponReloadEvent.Post(player, gunStack, itemGun, false, false, true, false, reloadTime, 1);
        MinecraftForge.EVENT_BUS.post(postEvent);

        gunType.playSound(player, WeaponSoundType.Reload, gunStack);

        int reloadType = ReloadType.Full.i;
        ModularWarfare.NETWORK.sendTo(new PacketClientAnimation(gunType.internalName, reloadTime, 1, reloadType), player);
        ServerTickHandler.playerReloadCooldown.put(player.getUUID(), reloadTime);
    }

    private void handleBulletReload(ServerPlayer player, ItemStack gunStack, ItemGun itemGun, GunType gunType) {
        // Simplified bullet reload logic
        if (ServerTickHandler.playerReloadCooldown.containsKey(player.getUUID())) return;

        WeaponReloadEvent.Pre preEvent = new WeaponReloadEvent.Pre(player, gunStack, itemGun, false, false);
        MinecraftForge.EVENT_BUS.post(preEvent);
        if (preEvent.isCanceled()) return;

        int reloadTime = preEvent.getReloadTime();
        WeaponReloadEvent.Post postEvent = new WeaponReloadEvent.Post(player, gunStack, itemGun, false, false, true, false, reloadTime, 1);
        MinecraftForge.EVENT_BUS.post(postEvent);

        ModularWarfare.NETWORK.sendTo(new PacketClientAnimation(gunType.internalName, reloadTime, 1, ReloadType.Load.i), player);
        ServerTickHandler.playerReloadCooldown.put(player.getUUID(), reloadTime);
    }

    @Override
    public void handleClientSide(Player player) {}
}