package com.modularwarfare.common.handler;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.network.BackWeaponsManager;
import com.modularwarfare.common.network.PacketAimingReponse;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerTickHandler extends com.modularwarfare.utility.event.ForgeEvent {
    public static ConcurrentHashMap<UUID, Integer> playerReloadCooldown = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, ItemStack> playerReloadItemStack = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, Integer> playerAimShootCooldown = new ConcurrentHashMap<>();  // Изменено с String на UUID
    public static ConcurrentHashMap<UUID, Boolean> playerAimInstant = new ConcurrentHashMap<>();  // Изменено с String на UUID
    private int i = 0;
    private long lastBackWeaponsSync = -1L;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        long currentTime = System.currentTimeMillis();
        if (lastBackWeaponsSync == -1L || currentTime - lastBackWeaponsSync >= 1000L) {
            lastBackWeaponsSync = currentTime;
            BackWeaponsManager.INSTANCE.collect().sync();
        }

        if (event.phase == TickEvent.Phase.START) {
            ModularWarfare.NETWORK.handleServerPackets();

            // Update aim shoot cooldowns - используем UUID
            Iterator<UUID> aimIterator = playerAimShootCooldown.keySet().iterator();
            while (aimIterator.hasNext()) {
                UUID uuid = aimIterator.next();
                int value = playerAimShootCooldown.get(uuid) - 1;
                if (value <= 0) {
                    aimIterator.remove();
                    ModularWarfare.NETWORK.sendToAll(new PacketAimingReponse(uuid, false));  // Передаём UUID
                } else {
                    playerAimShootCooldown.put(uuid, value);
                }
            }

            // Update reload cooldowns
            Iterator<UUID> reloadIterator = playerReloadCooldown.keySet().iterator();
            while (reloadIterator.hasNext()) {
                UUID uuid = reloadIterator.next();
                int value = playerReloadCooldown.get(uuid) - 1;
                if (value <= 0) {
                    reloadIterator.remove();
                    playerReloadItemStack.remove(uuid);
                } else {
                    playerReloadCooldown.put(uuid, value);
                }
            }
        } else if (event.phase == TickEvent.Phase.END) {
            if (ModularWarfare.PLAYERHANDLER != null) {
                ModularWarfare.PLAYERHANDLER.serverTick();
            }
        }
    }
}