package com.modularwarfare.common.hitbox.playerdata;

import com.modularwarfare.common.hitbox.PlayerSnapshot;
import net.minecraft.world.entity.player.Player;

public class PlayerData {
    public String username;
    public PlayerSnapshot[] snapshots;

    public PlayerData(String name) {
        this.username = name;
        this.snapshots = new PlayerSnapshot[20];
    }

    public void tick(Player player) {
        System.arraycopy(snapshots, 0, snapshots, 1, snapshots.length - 1);
        snapshots[0] = new PlayerSnapshot(player);
    }
}