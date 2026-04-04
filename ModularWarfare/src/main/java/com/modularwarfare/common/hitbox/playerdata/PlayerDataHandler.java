package com.modularwarfare.common.hitbox.playerdata;

import com.modularwarfare.ModularWarfare;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataHandler {
    public static Map<String, PlayerData> serverSideData = new HashMap<>();
    public static Map<String, PlayerData> clientSideData = new HashMap<>();

    public void serverTick(MinecraftServer server) {
        if (server == null) return;

        for (var world : server.getAllLevels()) {
            for (Player player : world.players()) {
                getPlayerData(player).tick(player);
            }
        }
    }

    public void clientTick() {
        if (Minecraft.getInstance().level != null) {
            for (Player player : Minecraft.getInstance().level.players()) {
                getPlayerData(player).tick(player);
            }
        }
    }

    public static PlayerData getPlayerData(Player player) {
        if (player == null) return null;
        return getPlayerData(player.getName().getString(), player.level().isClientSide ? Dist.CLIENT : Dist.DEDICATED_SERVER);
    }

    public static PlayerData getPlayerData(String username, Dist side) {
        Map<String, PlayerData> dataMap = side == Dist.CLIENT ? clientSideData : serverSideData;

        if (!dataMap.containsKey(username)) {
            dataMap.put(username, new PlayerData(username));
        }
        return dataMap.get(username);
    }
}