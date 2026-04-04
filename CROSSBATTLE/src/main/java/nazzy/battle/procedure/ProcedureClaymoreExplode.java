package nazzy.battle.procedure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;

public class ProcedureClaymoreExplode {

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("x") == null || dependencies.get("y") == null ||
                dependencies.get("z") == null || dependencies.get("world") == null) {
            System.err.println("Failed to load dependencies for procedure ClaymoreExplode!");
            return;
        }

        int x = (int) dependencies.get("x");
        int y = (int) dependencies.get("y");
        int z = (int) dependencies.get("z");
        Level world = (Level) dependencies.get("world");

        world.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);

        if (!world.isClientSide()) {
            world.explode(null, x, y, z, 4.0F, Level.ExplosionInteraction.TNT);
        }
    }
}