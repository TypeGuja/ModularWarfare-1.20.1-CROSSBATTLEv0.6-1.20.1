package nazzy.battle.procedure;

import nazzy.battle.gui.GuiSHOPGui1;
import nazzy.battle.init.InitMenus;
import nazzy.battle.init.InitSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class ProcedureSHOPgui {

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("entity") == null || dependencies.get("x") == null ||
                dependencies.get("y") == null || dependencies.get("z") == null ||
                dependencies.get("world") == null) {
            System.err.println("Failed to load dependencies for procedure SHOPgui!");
            return;
        }

        Entity entity = (Entity) dependencies.get("entity");
        int x = (int) dependencies.get("x");
        int y = (int) dependencies.get("y");
        int z = (int) dependencies.get("z");
        Level world = (Level) dependencies.get("world");

        if (entity instanceof ServerPlayer player) {
            player.openMenu(new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("battle.shop");
                }

                @Override
                public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
                    return new GuiSHOPGui1.GuiContainerMod(id, world, x, y, z, player);
                }
            });
        }

        world.playSound(null, new BlockPos(x, y, z), InitSounds.SHOP_START.get(),
                SoundSource.NEUTRAL, 1.0F, 1.0F);
    }
}