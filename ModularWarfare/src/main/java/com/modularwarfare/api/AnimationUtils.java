package com.modularwarfare.api;

import net.minecraft.world.entity.player.Player;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class AnimationUtils {
    // Используем WeakHashMap с UUID вместо String
    public static Map<UUID, Boolean> isAiming = new WeakHashMap<>();

    public static void setAiming(Player player, boolean aiming) {
        if (player != null) {
            isAiming.put(player.getUUID(), aiming);
        }
    }

    public static boolean isAiming(Player player) {
        if (player == null) return false;
        return isAiming.getOrDefault(player.getUUID(), false);
    }

    public static void removePlayer(Player player) {
        if (player != null) {
            isAiming.remove(player.getUUID());
        }
    }
}