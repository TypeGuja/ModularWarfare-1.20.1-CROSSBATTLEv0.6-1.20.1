package nazzy.battle.procedure;

import nazzy.battle.BattleVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class ProcedureTimerAdd {

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("x") == null || dependencies.get("y") == null ||
                dependencies.get("z") == null || dependencies.get("world") == null) {
            System.err.println("Failed to load dependencies for procedure TimerAdd!");
            return;
        }

        int x = (int) dependencies.get("x");
        int y = (int) dependencies.get("y");
        int z = (int) dependencies.get("z");
        Level world = (Level) dependencies.get("world");

        BattleVariables.MapVariables vars = BattleVariables.MapVariables.get(world);

        // Set game start countdown to 40 seconds
        vars.GameStart = 40.0;
        vars.syncData(world);

        // Start game
        vars.isPlaying = true;
        vars.syncData(world);

        // Reset team counts
        vars.red_count = 0.0;
        vars.blue_count = 0.0;
        vars.green_count = 0.0;
        vars.yellow_count = 0.0;
        vars.syncData(world);

        // Reset team death flags
        vars.red_dead = false;
        vars.blue_dead = false;
        vars.green_dead = false;
        vars.yellow_dead = false;
        vars.syncData(world);

        // Set game time to 1200 ticks (60 seconds)
        vars.game_time = 1200.0;
        vars.syncData(world);

        // Set world border to 500 blocks
        if (world instanceof ServerLevel serverLevel) {
            String command = "worldborder set 500";
            serverLevel.getServer().getCommands().performPrefixedCommand(
                    serverLevel.getServer().createCommandSourceStack(), command);
        }
    }
}