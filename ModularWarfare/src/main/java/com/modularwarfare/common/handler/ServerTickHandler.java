package com.modularwarfare.common.handler;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.network.BackWeaponsManager;
import com.modularwarfare.common.network.PacketAimingReponse;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerTickHandler extends com.modularwarfare.utility.event.ForgeEvent {
    public static ConcurrentHashMap<UUID, Integer> playerReloadCooldown = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, ItemStack> playerReloadItemStack = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Integer> playerAimShootCooldown = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Boolean> playerAimInstant = new ConcurrentHashMap<>();
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

            // Update aim shoot cooldowns - fixed iteration
            for (String playerName : playerAimShootCooldown.keySet().toArray(new String[0])) {
                int value = playerAimShootCooldown.get(playerName) - 1;
                if (value <= 0) {
                    playerAimShootCooldown.remove(playerName);
                    ModularWarfare.NETWORK.sendToAll(new PacketAimingReponse(playerName, false));
                } else {
                    playerAimShootCooldown.replace(playerName, value);
                }
            }

            // Update reload cooldowns - fixed iteration
            for (UUID uuid : playerReloadCooldown.keySet().toArray(new UUID[0])) {
                int value = playerReloadCooldown.get(uuid) - 1;
                if (value <= 0) {
                    playerReloadCooldown.remove(uuid);
                    playerReloadItemStack.remove(uuid);
                } else {
                    playerReloadCooldown.replace(uuid, value);
                }
            }
        } else if (event.phase == TickEvent.Phase.END) {
            if (ModularWarfare.PLAYERHANDLER != null) {
                ModularWarfare.PLAYERHANDLER.serverTick();
            }
        }
    }
}