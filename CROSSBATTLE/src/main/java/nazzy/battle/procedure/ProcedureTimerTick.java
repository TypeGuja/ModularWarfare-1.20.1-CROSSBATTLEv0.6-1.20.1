package nazzy.battle.procedure;

import nazzy.battle.BattleVariables;
import nazzy.battle.init.InitSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;

public class ProcedureTimerTick {

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("x") == null || dependencies.get("y") == null ||
                dependencies.get("z") == null || dependencies.get("world") == null) {
            System.err.println("Failed to load dependencies for procedure TimerTick!");
            return;
        }

        int x = (int) dependencies.get("x");
        int y = (int) dependencies.get("y");
        int z = (int) dependencies.get("z");
        Level world = (Level) dependencies.get("world");
        BlockPos pos = new BlockPos(x, y, z);

        BattleVariables.MapVariables vars = BattleVariables.MapVariables.get(world);

        // Countdown timer
        if (vars.GameStart > 0) {
            vars.GameStart -= 1;
            vars.syncData(world);
        }

        // Game start
        if (vars.GameStart == 0) {
            vars.GameStart = -1;
            vars.syncData(world);

            if (world instanceof ServerLevel serverLevel) {
                // Stop sounds
                serverLevel.getServer().getCommands().performPrefixedCommand(
                        serverLevel.getServer().createCommandSourceStack(),
                        "execute as @a at @s run stopsound @s battle:team_all_dead");

                // Play match start sound
                serverLevel.playSound(null, pos, InitSounds.MATCH_STARTS.get(),
                        SoundSource.MASTER, 1.0F, 1.0F);

                // Title
                serverLevel.getServer().getCommands().performPrefixedCommand(
                        serverLevel.getServer().createCommandSourceStack(),
                        "title @a title {\"text\":\"Начали!\"}");
            }
        }

        // Team elimination checks
        if (vars.red_count < 1) {
            vars.red_dead = true;
            vars.red_count = 1;
            vars.syncData(world);

            if (world instanceof ServerLevel serverLevel) {
                serverLevel.getServer().getCommands().performPrefixedCommand(
                        serverLevel.getServer().createCommandSourceStack(),
                        "title @a actionbar {\"text\":\"Красные выбывают.\",\"color\":\"red\",\"bold\":true}");
                serverLevel.getServer().getCommands().performPrefixedCommand(
                        serverLevel.getServer().createCommandSourceStack(),
                        "execute as @a at @s run playsound battle:team_all_dead master @s ~ ~ ~");
            }
        }

        if (vars.blue_count < 1) {
            vars.blue_dead = true;
            vars.blue_count = 1;
            vars.syncData(world);

            if (world instanceof ServerLevel serverLevel) {
                serverLevel.getServer().getCommands().performPrefixedCommand(
                        serverLevel.getServer().createCommandSourceStack(),
                        "title @a actionbar {\"text\":\"Синие выбывают.\",\"color\":\"blue\",\"bold\":true}");
                serverLevel.getServer().getCommands().performPrefixedCommand(
                        serverLevel.getServer().createCommandSourceStack(),
                        "execute as @a at @s run playsound battle:team_all_dead master @s ~ ~ ~");
            }
        }

        if (vars.green_count < 1) {
            vars.green_dead = true;
            vars.green_count = 1;
            vars.syncData(world);

            if (world instanceof ServerLevel serverLevel) {
                serverLevel.getServer().getCommands().performPrefixedCommand(
                        serverLevel.getServer().createCommandSourceStack(),
                        "title @a actionbar {\"text\":\"Зелёные выбывают.\",\"color\":\"green\",\"bold\":true}");
                serverLevel.getServer().getCommands().performPrefixedCommand(
                        serverLevel.getServer().createCommandSourceStack(),
                        "execute as @a at @s run playsound battle:team_all_dead master @s ~ ~ ~");
            }
        }

        if (vars.yellow_count < 1) {
            vars.yellow_dead = true;
            vars.yellow_count = 1;
            vars.syncData(world);

            if (world instanceof ServerLevel serverLevel) {
                serverLevel.getServer().getCommands().performPrefixedCommand(
                        serverLevel.getServer().createCommandSourceStack(),
                        "title @a actionbar {\"text\":\"Жёлтые выбывают.\",\"color\":\"yellow\",\"bold\":true}");
                serverLevel.getServer().getCommands().performPrefixedCommand(
                        serverLevel.getServer().createCommandSourceStack(),
                        "execute as @a at @s run playsound battle:team_all_dead master @s ~ ~ ~");
            }
        }

        // Win condition checks
        if (!vars.red_dead && vars.blue_dead && vars.green_dead && vars.yellow_dead) {
            winGame(world, pos, "Красные победили!", "red");
        }
        if (vars.red_dead && !vars.blue_dead && vars.green_dead && vars.yellow_dead) {
            winGame(world, pos, "Синие победили!", "blue");
        }
        if (vars.red_dead && vars.blue_dead && !vars.green_dead && vars.yellow_dead) {
            winGame(world, pos, "Зелёные победили!", "green");
        }
        if (vars.red_dead && vars.blue_dead && vars.green_dead && !vars.yellow_dead) {
            winGame(world, pos, "Жёлтые победили!", "yellow");
        }

        // Game timer
        if (vars.game_time > -1) {
            vars.game_time -= 1;
            vars.syncData(world);
        }

        // Zone shrinking warnings
        if (vars.game_time == 60 && world instanceof ServerLevel serverLevel) {
            serverLevel.getServer().getCommands().performPrefixedCommand(
                    serverLevel.getServer().createCommandSourceStack(),
                    "title @a title {\"text\":\"Уменьшение зоны через 60 сек.\",\"color\":\"aqua\"}");
            serverLevel.playSound(null, pos, InitSounds.MATCH_STARTS.get(), SoundSource.MASTER, 1.0F, 1.0F);
        }

        if (vars.game_time == 30 && world instanceof ServerLevel serverLevel) {
            serverLevel.getServer().getCommands().performPrefixedCommand(
                    serverLevel.getServer().createCommandSourceStack(),
                    "title @a title {\"text\":\"Уменьшение зоны через 30 сек.\",\"color\":\"aqua\"}");
        }

        if (vars.game_time == 10 && world instanceof ServerLevel serverLevel) {
            serverLevel.getServer().getCommands().performPrefixedCommand(
                    serverLevel.getServer().createCommandSourceStack(),
                    "title @a title {\"text\":\"Уменьшение зоны через 10 сек.\",\"color\":\"aqua\"}");
        }

        if (vars.game_time == 0 && world instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, pos, InitSounds.MATCH_STARTS.get(), SoundSource.MASTER, 1.0F, 1.0F);
            serverLevel.getServer().getCommands().performPrefixedCommand(
                    serverLevel.getServer().createCommandSourceStack(),
                    "title @a title {\"text\":\"До последней капли...\",\"color\":\"red\"}");
            serverLevel.getServer().getCommands().performPrefixedCommand(
                    serverLevel.getServer().createCommandSourceStack(),
                    "worldborder set 50");
        }
    }

    private static void winGame(Level world, BlockPos pos, String message, String color) {
        if (world instanceof ServerLevel serverLevel) {
            serverLevel.getServer().getCommands().performPrefixedCommand(
                    serverLevel.getServer().createCommandSourceStack(),
                    "title @a title {\"text\":\"" + message + "\",\"color\":\"" + color + "\"}");
            serverLevel.getServer().getCommands().performPrefixedCommand(
                    serverLevel.getServer().createCommandSourceStack(),
                    "setblock 927 2 525 redstone_torch");
        }

        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

        BattleVariables.MapVariables vars = BattleVariables.MapVariables.get(world);
        vars.isPlaying = false;
        vars.syncData(world);
    }
}