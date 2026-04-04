package nazzy.battle.block;

import java.util.HashMap;

import nazzy.battle.block.entity.DestroyedOreBlockEntity;
import nazzy.battle.init.InitBlockEntities;
import nazzy.battle.procedure.ProcedureDestroyedIronTick;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import javax.annotation.Nullable;

public class BlockDestroyedIron {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    public static class BlockCustom extends BaseEntityBlock {

        public BlockCustom(Properties properties) {
            super(properties);
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return SHAPE;
        }

        @Override
        public RenderShape getRenderShape(BlockState state) {
            return RenderShape.MODEL;
        }

        @Nullable
        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new DestroyedOreBlockEntity(pos, state);
        }

        @Override
        public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
            super.onPlace(state, level, pos, oldState, isMoving);
            if (!level.isClientSide()) {
                level.scheduleTick(pos, this, 20);
            }
        }

        @Override
        public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
            if (!level.isClientSide()) {
                HashMap<String, Object> dependencies = new HashMap<>();
                dependencies.put("x", pos.getX());
                dependencies.put("y", pos.getY());
                dependencies.put("z", pos.getZ());
                dependencies.put("world", level);
                ProcedureDestroyedIronTick.executeProcedure(dependencies);
                level.scheduleTick(pos, this, 20);
            }
        }

        @Nullable
        @Override
        public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
            return null;
        }
    }
}