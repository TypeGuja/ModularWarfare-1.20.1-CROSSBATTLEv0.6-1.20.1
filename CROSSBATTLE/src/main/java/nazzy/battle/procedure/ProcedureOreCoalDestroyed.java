package nazzy.battle.procedure;

import nazzy.battle.block.entity.DestroyedOreBlockEntity;
import nazzy.battle.init.InitBlocks;
import nazzy.battle.init.InitSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Random;

public class ProcedureOreCoalDestroyed {

    private static final Random RANDOM = new Random();

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("entity") == null || dependencies.get("x") == null ||
                dependencies.get("y") == null || dependencies.get("z") == null ||
                dependencies.get("world") == null) {
            return;
        }

        Entity entity = (Entity) dependencies.get("entity");
        int x = (int) dependencies.get("x");
        int y = (int) dependencies.get("y");
        int z = (int) dependencies.get("z");
        Level world = (Level) dependencies.get("world");
        BlockPos pos = new BlockPos(x, y, z);

        // Меняем на разрушенную руду
        world.setBlock(pos, InitBlocks.DESTROYED_COAL.get().defaultBlockState(), 3);

        // Устанавливаем время восстановления (30 тиков = 1.5 секунды)
        if (!world.isClientSide()) {
            if (world.getBlockEntity(pos) instanceof DestroyedOreBlockEntity destroyed) {
                destroyed.setTime(30.0);
            }
        }

        // Выпадение руды (1-3 штуки)
        int dropCount = 1;
        double random = RANDOM.nextDouble();
        if (random < 0.6) dropCount = 1;
        else if (random < 0.8) dropCount = 2;
        else dropCount = 3;

        if (entity instanceof Player player) {
            ItemStack stack = new ItemStack(InitBlocks.ORE_COAL.get().asItem(), dropCount);
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
            if (!world.isClientSide()) {
                player.displayClientMessage(Component.literal("§l§a+" + dropCount + " §7Уголь"), true);
            }
        } else if (!world.isClientSide()) {
            world.addFreshEntity(new ItemEntity(world, x + 0.5, y + 0.5, z + 0.5,
                    new ItemStack(InitBlocks.ORE_COAL.get().asItem(), dropCount)));
        }

        world.playSound(null, pos, InitSounds.GOT.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
    }
}