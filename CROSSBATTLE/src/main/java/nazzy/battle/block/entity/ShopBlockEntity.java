package nazzy.battle.block.entity;

import nazzy.battle.init.InitBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public class ShopBlockEntity extends BlockEntity {

    private double money = 0.0;
    private UUID owner = null;

    public ShopBlockEntity(BlockPos pos, BlockState state) {
        super(InitBlockEntities.SHOP_BLOCK_ENTITY.get(), pos, state);
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
        setChanged();
        if (hasLevel() && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void addMoney(double amount) {
        this.money += amount;
        setChanged();
        if (hasLevel() && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean subtractMoney(double amount) {
        if (money >= amount) {
            money -= amount;
            setChanged();
            if (hasLevel() && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
            return true;
        }
        return false;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
        setChanged();
        if (hasLevel() && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putDouble("money", money);
        if (owner != null) {
            tag.putUUID("owner", owner);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        money = tag.getDouble("money");
        if (tag.hasUUID("owner")) {
            owner = tag.getUUID("owner");
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putDouble("money", money);
        if (owner != null) {
            tag.putUUID("owner", owner);
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        money = tag.getDouble("money");
        if (tag.hasUUID("owner")) {
            owner = tag.getUUID("owner");
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}