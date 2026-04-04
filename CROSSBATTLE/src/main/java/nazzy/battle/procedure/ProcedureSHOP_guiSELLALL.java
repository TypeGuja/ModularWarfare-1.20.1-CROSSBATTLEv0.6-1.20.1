package nazzy.battle.procedure;

import nazzy.battle.block.entity.ShopBlockEntity;
import nazzy.battle.init.InitBlocks;
import nazzy.battle.init.InitSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class ProcedureSHOP_guiSELLALL {

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("entity") == null || dependencies.get("x") == null ||
                dependencies.get("y") == null || dependencies.get("z") == null ||
                dependencies.get("world") == null) {
            System.err.println("Failed to load dependencies for procedure SHOP_guiSELLALL!");
            return;
        }

        Entity entity = (Entity) dependencies.get("entity");
        int x = (int) dependencies.get("x");
        int y = (int) dependencies.get("y");
        int z = (int) dependencies.get("z");
        Level world = (Level) dependencies.get("world");
        BlockPos pos = new BlockPos(x, y, z);

        if (!(entity instanceof Player player)) return;

        var blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ShopBlockEntity shop)) return;

        boolean soldAnything = false;
        double totalMoney = 0;

        // Продажа угля
        while (hasItem(player, InitBlocks.ORE_COAL.get().asItem())) {
            removeItem(player, InitBlocks.ORE_COAL.get().asItem(), 1);
            totalMoney += 10;
            soldAnything = true;
        }

        // Продажа железа
        while (hasItem(player, InitBlocks.ORE_IRON.get().asItem())) {
            removeItem(player, InitBlocks.ORE_IRON.get().asItem(), 1);
            totalMoney += 25;
            soldAnything = true;
        }

        // Продажа золота
        while (hasItem(player, InitBlocks.ORE_GOLD.get().asItem())) {
            removeItem(player, InitBlocks.ORE_GOLD.get().asItem(), 1);
            totalMoney += 50;
            soldAnything = true;
        }

        // Продажа алмазов
        while (hasItem(player, InitBlocks.ORE_DIAMOND.get().asItem())) {
            removeItem(player, InitBlocks.ORE_DIAMOND.get().asItem(), 1);
            totalMoney += 100;
            soldAnything = true;
        }

        if (soldAnything) {
            shop.addMoney(totalMoney);
            world.playSound(null, pos, InitSounds.SHOP_SUCC.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            player.displayClientMessage(Component.literal("Продано на $" + (int)totalMoney), true);
        } else {
            player.displayClientMessage(Component.literal("Нечего продавать."), true);
            world.playSound(null, pos, InitSounds.SHOP_ERROR.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
    }

    private static boolean hasItem(Player player, net.minecraft.world.item.Item item) {
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && stack.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    private static void removeItem(Player player, net.minecraft.world.item.Item item, int count) {
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                stack.shrink(count);
                if (stack.isEmpty()) {
                    player.getInventory().items.set(i, ItemStack.EMPTY);
                }
                break;
            }
        }
    }
}