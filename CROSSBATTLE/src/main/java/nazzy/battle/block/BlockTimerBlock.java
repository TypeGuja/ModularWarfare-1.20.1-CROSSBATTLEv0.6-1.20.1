package nazzy.battle.block;

import java.util.HashMap;
import java.util.Random;

import nazzy.battle.procedure.ProcedureTimerAdd;
import nazzy.battle.procedure.ProcedureTimerBlockBlockDestroyedByPlayer;
import nazzy.battle.procedure.ProcedureTimerBlockOnBlockRightClicked;
import nazzy.battle.procedure.ProcedureTimerTick;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockTimerBlock {

    public static class BlockCustom extends Block {

        public BlockCustom(Properties properties) {
            super(properties);
        }

        @Override
        public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
            super.onPlace(state, level, pos, oldState, isMoving);
            if (!level.isClientSide()) {
                level.scheduleTick(pos, this, 20);
                HashMap<String, Object> dependencies = new HashMap<>();
                dependencies.put("x", pos.getX());
                dependencies.put("y", pos.getY());
                dependencies.put("z", pos.getZ());
                dependencies.put("world", level);
                ProcedureTimerAdd.executeProcedure(dependencies);
            }
        }

        @Override
        public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
            super.tick(state, level, pos, random);
            if (!level.isClientSide()) {
                HashMap<String, Object> dependencies = new HashMap<>();
                dependencies.put("x", pos.getX());
                dependencies.put("y", pos.getY());
                dependencies.put("z", pos.getZ());
                dependencies.put("world", level);
                ProcedureTimerTick.executeProcedure(dependencies);
                level.scheduleTick(pos, this, 20);
            }
        }

        @Override
        public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
            super.onRemove(state, level, pos, newState, isMoving);
            if (!level.isClientSide()) {
                HashMap<String, Object> dependencies = new HashMap<>();
                dependencies.put("world", level);
                ProcedureTimerBlockBlockDestroyedByPlayer.executeProcedure(dependencies);
            }
        }

        @Override
        public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            if (!level.isClientSide()) {
                HashMap<String, Object> dependencies = new HashMap<>();
                dependencies.put("world", level);
                ProcedureTimerBlockOnBlockRightClicked.executeProcedure(dependencies);
            }
            return InteractionResult.SUCCESS;
        }
    }
}