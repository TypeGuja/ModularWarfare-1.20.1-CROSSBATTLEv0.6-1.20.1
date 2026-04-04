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

public class ProcedureOreGoldDestroyed {

    private static final Random RANDOM = new Random();

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("entity") == null || dependencies.get("x") == null ||
                dependencies.get("y") == null || dependencies.get("z") == null ||
                dependencies.get("world") == null) {
            System.err.println("Failed to load dependencies for procedure OreGoldDestroyed!");
            return;
        }

        Entity entity = (Entity) dependencies.get("entity");
        int x = (int) dependencies.get("x");
        int y = (int) dependencies.get("y");
        int z = (int) dependencies.get("z");
        Level world = (Level) dependencies.get("world");

        world.setBlock(new BlockPos(x, y, z), InitBlocks.DESTROYED_GOLD.get().defaultBlockState(), 3);

        if (!world.isClientSide()) {
            var blockEntity = world.getBlockEntity(new BlockPos(x, y, z));
            if (blockEntity instanceof DestroyedOreBlockEntity destroyed) {
                destroyed.setTime(90.0);
            }
        }

        int dropCount;
        double random = RANDOM.nextDouble();

        if (random < 0.7) {
            dropCount = 1;
        } else if (random < 0.8) {
            dropCount = 2;
        } else {
            dropCount = 3;
        }

        if (entity instanceof Player player) {
            ItemStack stack = new ItemStack(InitBlocks.ORE_GOLD.get().asItem(), dropCount);
            player.getInventory().add(stack);

            if (!world.isClientSide()) {
                player.displayClientMessage(Component.literal("§l§a+" + dropCount + " §eЗолото"), true);
            }
        } else if (!world.isClientSide()) {
            world.addFreshEntity(new ItemEntity(world, x, y + 0.5, z,
                    new ItemStack(InitBlocks.ORE_GOLD.get().asItem(), dropCount)));
        }

        world.playSound(null, x, y, z, InitSounds.GOT.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
    }
}