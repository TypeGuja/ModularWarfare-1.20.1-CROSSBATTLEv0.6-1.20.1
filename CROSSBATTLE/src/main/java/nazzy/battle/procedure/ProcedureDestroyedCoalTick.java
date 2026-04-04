package nazzy.battle.procedure;

import nazzy.battle.block.entity.DestroyedOreBlockEntity;
import nazzy.battle.init.InitBlocks;
import nazzy.battle.init.InitSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class ProcedureDestroyedCoalTick {

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("x") == null || dependencies.get("y") == null ||
                dependencies.get("z") == null || dependencies.get("world") == null) {
            return;
        }

        int x = (int) dependencies.get("x");
        int y = (int) dependencies.get("y");
        int z = (int) dependencies.get("z");
        Level world = (Level) dependencies.get("world");
        BlockPos pos = new BlockPos(x, y, z);

        if (world.getBlockEntity(pos) instanceof DestroyedOreBlockEntity destroyed) {
            double time = destroyed.getTime();

            if (time > 0) {
                destroyed.setTime(time - 1);
            }

            if (time <= 1) {
                world.setBlock(pos, InitBlocks.ORE_COAL.get().defaultBlockState(), 3);
                world.playSound(null, pos, InitSounds.GOT.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
        }
    }
}