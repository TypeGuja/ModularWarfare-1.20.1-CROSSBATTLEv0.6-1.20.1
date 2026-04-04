package nazzy.battle.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockSandbag {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static class BlockCustom extends FallingBlock {

        public BlockCustom(Properties properties) {
            super(properties);
            this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            Direction facing = state.getValue(FACING);
            return switch (facing) {
                case NORTH -> Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 12.0);
                case SOUTH -> Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 12.0);
                case WEST -> Block.box(4.0, 0.0, 0.0, 12.0, 16.0, 16.0);
                case EAST -> Block.box(4.0, 0.0, 0.0, 12.0, 16.0, 16.0);
                default -> Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 12.0);
            };
        }

        @Override
        public BlockState getStateForPlacement(BlockPlaceContext context) {
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(FACING);
        }
    }
}