package nazzy.battle.init;

import nazzy.battle.Battle;
import nazzy.battle.gui.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Battle.MOD_ID);

    public static final RegistryObject<MenuType<GuiSHOPGui1.GuiContainerMod>> SHOP_MAIN_MENU =
            MENUS.register("shop_main", () -> IForgeMenuType.create((windowId, inv, data) -> {
                int x = data.readInt();
                int y = data.readInt();
                int z = data.readInt();
                return new GuiSHOPGui1.GuiContainerMod(windowId, inv.player.level(), x, y, z, inv.player);
            }));

    public static final RegistryObject<MenuType<GuiSHOP_Gui_Sell.GuiContainerMod>> SHOP_SELL_MENU =
            MENUS.register("shop_sell", () -> IForgeMenuType.create((windowId, inv, data) -> {
                int x = data.readInt();
                int y = data.readInt();
                int z = data.readInt();
                return new GuiSHOP_Gui_Sell.GuiContainerMod(windowId, inv.player.level(), x, y, z, inv.player);
            }));

    public static final RegistryObject<MenuType<GuiSHOP_Gui_Buy.GuiContainerMod>> SHOP_BUY1_MENU =
            MENUS.register("shop_buy1", () -> IForgeMenuType.create((windowId, inv, data) -> {
                int x = data.readInt();
                int y = data.readInt();
                int z = data.readInt();
                return new GuiSHOP_Gui_Buy.GuiContainerMod(windowId, inv.player.level(), x, y, z, inv.player);
            }));

    public static final RegistryObject<MenuType<GuiSHOP_Gui_Buy2.GuiContainerMod>> SHOP_BUY2_MENU =
            MENUS.register("shop_buy2", () -> IForgeMenuType.create((windowId, inv, data) -> {
                int x = data.readInt();
                int y = data.readInt();
                int z = data.readInt();
                return new GuiSHOP_Gui_Buy2.GuiContainerMod(windowId, inv.player.level(), x, y, z, inv.player);
            }));

    public static final RegistryObject<MenuType<GuiSHOP_Gui_Buy3.GuiContainerMod>> SHOP_BUY3_MENU =
            MENUS.register("shop_buy3", () -> IForgeMenuType.create((windowId, inv, data) -> {
                int x = data.readInt();
                int y = data.readInt();
                int z = data.readInt();
                return new GuiSHOP_Gui_Buy3.GuiContainerMod(windowId, inv.player.level(), x, y, z, inv.player);
            }));
}