package nazzy.battle.init;

import nazzy.battle.Battle;
import nazzy.battle.block.entity.DestroyedOreBlockEntity;
import nazzy.battle.block.entity.ShopBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Battle.MOD_ID);

    public static final RegistryObject<BlockEntityType<ShopBlockEntity>> SHOP_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("shop", () -> BlockEntityType.Builder.of(ShopBlockEntity::new,
                    InitBlocks.SHOP.get()).build(null));

    public static final RegistryObject<BlockEntityType<DestroyedOreBlockEntity>> DESTROYED_ORE_ENTITY =
            BLOCK_ENTITIES.register("destroyed_ore", () -> BlockEntityType.Builder.of(DestroyedOreBlockEntity::new,
                    InitBlocks.DESTROYED_COAL.get(), InitBlocks.DESTROYED_IRON.get(),
                    InitBlocks.DESTROYED_GOLD.get(), InitBlocks.DESTROYED_DIAMOND.get()).build(null));
}