package nazzy.battle.init;

import nazzy.battle.Battle;
import nazzy.battle.item.ItemKnife;
import nazzy.battle.item.ItemLedorub;
import nazzy.battle.item.ItemMedkit;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Battle.MOD_ID);

    // Block items
    public static final RegistryObject<Item> ORE_COAL_ITEM = ITEMS.register("orecoal",
            () -> new BlockItem(InitBlocks.ORE_COAL.get(), new Item.Properties()));
    public static final RegistryObject<Item> ORE_IRON_ITEM = ITEMS.register("oreiron",
            () -> new BlockItem(InitBlocks.ORE_IRON.get(), new Item.Properties()));
    public static final RegistryObject<Item> ORE_GOLD_ITEM = ITEMS.register("oregold",
            () -> new BlockItem(InitBlocks.ORE_GOLD.get(), new Item.Properties()));
    public static final RegistryObject<Item> ORE_DIAMOND_ITEM = ITEMS.register("orediamond",
            () -> new BlockItem(InitBlocks.ORE_DIAMOND.get(), new Item.Properties()));

    public static final RegistryObject<Item> DESTROYED_COAL_ITEM = ITEMS.register("destroyedcoal",
            () -> new BlockItem(InitBlocks.DESTROYED_COAL.get(), new Item.Properties()));
    public static final RegistryObject<Item> DESTROYED_IRON_ITEM = ITEMS.register("destroyediron",
            () -> new BlockItem(InitBlocks.DESTROYED_IRON.get(), new Item.Properties()));
    public static final RegistryObject<Item> DESTROYED_GOLD_ITEM = ITEMS.register("destroyedgold",
            () -> new BlockItem(InitBlocks.DESTROYED_GOLD.get(), new Item.Properties()));
    public static final RegistryObject<Item> DESTROYED_DIAMOND_ITEM = ITEMS.register("destroyeddiamond",
            () -> new BlockItem(InitBlocks.DESTROYED_DIAMOND.get(), new Item.Properties()));

    public static final RegistryObject<Item> CLAYMORE_ITEM = ITEMS.register("claymore",
            () -> new BlockItem(InitBlocks.CLAYMORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> SANDBAG_ITEM = ITEMS.register("sandbag",
            () -> new BlockItem(InitBlocks.SANDBAG.get(), new Item.Properties()));
    public static final RegistryObject<Item> SHOP_ITEM = ITEMS.register("shop",
            () -> new BlockItem(InitBlocks.SHOP.get(), new Item.Properties()));
    public static final RegistryObject<Item> TIMER_BLOCK_ITEM = ITEMS.register("timerblock",
            () -> new BlockItem(InitBlocks.TIMER_BLOCK.get(), new Item.Properties()));

    // Custom items
    public static final RegistryObject<Item> MEDKIT = ITEMS.register("medkit",
            () -> new ItemMedkit.ItemFoodCustom(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KNIFE = ITEMS.register("knife",
            () -> new ItemKnife.RangedItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> LEDORUB = ITEMS.register("ledorub",
            () -> new ItemLedorub.RangedItem(new Item.Properties().stacksTo(1)));
}