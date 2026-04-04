package nazzy.battle;

import nazzy.battle.gui.*;
import nazzy.battle.init.InitMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Battle.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientProxyBattle {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(InitMenus.SHOP_MAIN_MENU.get(), GuiSHOPGui1.GuiWindow::new);
            MenuScreens.register(InitMenus.SHOP_SELL_MENU.get(), GuiSHOP_Gui_Sell.GuiWindow::new);
            MenuScreens.register(InitMenus.SHOP_BUY1_MENU.get(), GuiSHOP_Gui_Buy.GuiWindow::new);
            MenuScreens.register(InitMenus.SHOP_BUY2_MENU.get(), GuiSHOP_Gui_Buy2.GuiWindow::new);
            MenuScreens.register(InitMenus.SHOP_BUY3_MENU.get(), GuiSHOP_Gui_Buy3.GuiWindow::new);
        });
    }
}