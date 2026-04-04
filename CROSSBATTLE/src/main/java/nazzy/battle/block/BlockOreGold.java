package nazzy.battle.block;

import nazzy.battle.procedure.ProcedureOreGoldDestroyed;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public class BlockOreGold {

    public static class BlockCustom extends Block {

        public BlockCustom(Properties properties) {
            super(properties);
        }

        @Override
        public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
            super.playerDestroy(level, player, pos, state, blockEntity, tool);
            if (!level.isClientSide()) {
                HashMap<String, Object> dependencies = new HashMap<>();
                dependencies.put("entity", player);
                dependencies.put("x", pos.getX());
                dependencies.put("y", pos.getY());
                dependencies.put("z", pos.getZ());
                dependencies.put("world", level);
                ProcedureOreGoldDestroyed.executeProcedure(dependencies);
            }
        }
    }
}