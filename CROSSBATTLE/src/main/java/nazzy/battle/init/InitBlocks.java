package nazzy.battle.init;

import nazzy.battle.Battle;
import nazzy.battle.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Battle.MOD_ID);

    // Ores
    public static final RegistryObject<Block> ORE_COAL = BLOCKS.register("orecoal",
            () -> new BlockOreCoal.BlockCustom(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.0f, 10.0f).noOcclusion()));
    public static final RegistryObject<Block> ORE_IRON = BLOCKS.register("oreiron",
            () -> new BlockOreIron.BlockCustom(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5f, 10.0f).noOcclusion()));
    public static final RegistryObject<Block> ORE_GOLD = BLOCKS.register("oregold",
            () -> new BlockOreGold.BlockCustom(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f, 10.0f).noOcclusion()));
    public static final RegistryObject<Block> ORE_DIAMOND = BLOCKS.register("orediamond",
            () -> new BlockOreDiamond.BlockCustom(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.5f, 10.0f).noOcclusion()));

    // Destroyed ores
    public static final RegistryObject<Block> DESTROYED_COAL = BLOCKS.register("destroyedcoal",
            () -> new BlockDestroyedCoal.BlockCustom(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.0f, 10.0f).noOcclusion()));
    public static final RegistryObject<Block> DESTROYED_IRON = BLOCKS.register("destroyediron",
            () -> new BlockDestroyedIron.BlockCustom(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.0f, 10.0f).noOcclusion()));
    public static final RegistryObject<Block> DESTROYED_GOLD = BLOCKS.register("destroyedgold",
            () -> new BlockDestroyedGold.BlockCustom(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.0f, 10.0f).noOcclusion()));
    public static final RegistryObject<Block> DESTROYED_DIAMOND = BLOCKS.register("destroyeddiamond",
            () -> new BlockDestroyedDiamond.BlockCustom(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.0f, 10.0f).noOcclusion()));

    // Other blocks
    public static final RegistryObject<Block> CLAYMORE = BLOCKS.register("claymore",
            () -> new BlockClaymore.BlockCustom(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(1.0f, 10.0f).noOcclusion()));
    public static final RegistryObject<Block> SANDBAG = BLOCKS.register("sandbag",
            () -> new BlockSandbag.BlockCustom(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(1.0f, 10.0f).noOcclusion()));
    public static final RegistryObject<Block> SHOP = BLOCKS.register("shop",
            () -> new BlockSHOP.BlockCustom(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.0f, 10.0f).noOcclusion()));
    public static final RegistryObject<Block> TIMER_BLOCK = BLOCKS.register("timerblock",
            () -> new BlockTimerBlock.BlockCustom(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.0f, 10.0f).lightLevel(state -> 15)));
}