package nazzy.battle.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nazzy.battle.init.InitMenus;

public class GuiContainerModSell extends AbstractContainerMenu {
    public final Level world;
    public final Player entity;
    public final BlockPos pos;

    public GuiContainerModSell(int id, Inventory inv, BlockPos pos) {
        super(InitMenus.SHOP_SELL_MENU.get(), id);
        this.world = inv.player.level();
        this.entity = inv.player;
        this.pos = pos;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}