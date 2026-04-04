package nazzy.battle.procedure;

import nazzy.battle.block.entity.ShopBlockEntity;
import nazzy.battle.init.InitSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class ProcedureBuy9mmAmmo {

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("entity") == null || dependencies.get("x") == null ||
                dependencies.get("y") == null || dependencies.get("z") == null ||
                dependencies.get("world") == null) {
            System.err.println("Failed to load dependencies for procedure Buy9mmAmmo!");
            return;
        }

        Entity entity = (Entity) dependencies.get("entity");
        int x = (int) dependencies.get("x");
        int y = (int) dependencies.get("y");
        int z = (int) dependencies.get("z");
        Level world = (Level) dependencies.get("world");
        BlockPos pos = new BlockPos(x, y, z);

        var blockEntity = world.getBlockEntity(pos);
        double currentMoney = 0;

        if (blockEntity instanceof ShopBlockEntity shop) {
            currentMoney = shop.getMoney();
        }

        int price = 99;

        if (currentMoney >= price) {
            // Deduct money
            if (blockEntity instanceof ShopBlockEntity shop) {
                shop.setMoney(currentMoney - price);
            }

            // Play success sound
            world.playSound(null, pos, InitSounds.SHOP_SUCC.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);

            // Give item via command (or directly)
            if (world instanceof ServerLevel serverLevel && entity instanceof Player player) {
                String command = "give @p modularwarfare:prototype.9x19 16";
                serverLevel.getServer().getCommands().performPrefixedCommand(
                        serverLevel.getServer().createCommandSourceStack(), command);
            }
        } else {
            // Insufficient funds
            if (entity instanceof Player player) {
                player.displayClientMessage(Component.literal("Недостаточно средств."), true);
                player.closeContainer();
            }
            world.playSound(null, pos, InitSounds.SHOP_ERROR.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
    }
}