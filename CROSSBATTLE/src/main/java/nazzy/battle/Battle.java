package nazzy.battle;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import nazzy.battle.init.*;

@Mod(Battle.MOD_ID)
public class Battle {
    public static final String MOD_ID = "battle";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static Battle instance;

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<CreativeModeTab> BATTLE_TAB = TABS.register("battle",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.battle"))
                    .icon(() -> new ItemStack(InitItems.LEDORUB.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(InitItems.ORE_COAL_ITEM.get());
                        output.accept(InitItems.ORE_IRON_ITEM.get());
                        output.accept(InitItems.ORE_GOLD_ITEM.get());
                        output.accept(InitItems.ORE_DIAMOND_ITEM.get());
                        output.accept(InitItems.DESTROYED_COAL_ITEM.get());
                        output.accept(InitItems.DESTROYED_IRON_ITEM.get());
                        output.accept(InitItems.DESTROYED_GOLD_ITEM.get());
                        output.accept(InitItems.DESTROYED_DIAMOND_ITEM.get());
                        output.accept(InitItems.CLAYMORE_ITEM.get());
                        output.accept(InitItems.SANDBAG_ITEM.get());
                        output.accept(InitItems.SHOP_ITEM.get());
                        output.accept(InitItems.TIMER_BLOCK_ITEM.get());
                        output.accept(InitItems.MEDKIT.get());
                        output.accept(InitItems.KNIFE.get());
                        output.accept(InitItems.LEDORUB.get());
                    })
                    .build());

    public Battle() {
        instance = this;
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        InitBlocks.BLOCKS.register(bus);
        InitItems.ITEMS.register(bus);
        InitBlockEntities.BLOCK_ENTITIES.register(bus);
        InitSounds.SOUNDS.register(bus);
        InitMenus.MENUS.register(bus);
        TABS.register(bus);

        bus.addListener(this::commonSetup);
        // НЕ добавляем clientSetup здесь, так как ClientProxyBattle уже подписан на событие

        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("Battle mod initialized!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BattleVariables.init();
        });
        LOGGER.info("Common setup complete");
    }
}