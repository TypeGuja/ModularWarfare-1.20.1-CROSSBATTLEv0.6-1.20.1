package nazzy.battle.block.entity;

import nazzy.battle.init.InitBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DestroyedOreBlockEntity extends BlockEntity {

    private double time = 0.0;

    public DestroyedOreBlockEntity(BlockPos pos, BlockState state) {
        super(InitBlockEntities.DESTROYED_ORE_ENTITY.get(), pos, state);
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putDouble("time", time);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        time = tag.getDouble("time");
    }
}