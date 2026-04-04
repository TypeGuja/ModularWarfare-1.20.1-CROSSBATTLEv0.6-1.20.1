package nazzy.battle.procedure;

import nazzy.battle.BattleVariables;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class ProcedureTimerBlockOnBlockRightClicked {

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("world") == null) {
            System.err.println("Failed to load dependencies for procedure TimerBlockOnBlockRightClicked!");
            return;
        }

        Level world = (Level) dependencies.get("world");

        BattleVariables.MapVariables vars = BattleVariables.MapVariables.get(world);
        vars.game_time = 61.0;
        vars.syncData(world);
    }
}