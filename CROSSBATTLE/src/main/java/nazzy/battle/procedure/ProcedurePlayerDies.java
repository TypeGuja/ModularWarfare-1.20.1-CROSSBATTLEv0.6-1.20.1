package nazzy.battle.procedure;

import nazzy.battle.init.InitItems;
import nazzy.battle.init.InitSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber
public class ProcedurePlayerDies {

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("entity") == null) {
            System.err.println("Failed to load dependencies for procedure PlayerDies!");
            return;
        }

        Entity entity = (Entity) dependencies.get("entity");

        if (entity instanceof Player player) {
            // Remove all ledorub pickaxes
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (!stack.isEmpty() && stack.getItem() == InitItems.LEDORUB.get()) {
                    player.getInventory().setItem(i, ItemStack.EMPTY);
                }
            }

            // Play death sound
            if (player.level() instanceof ServerLevel serverLevel) {
                serverLevel.playSound(null, player.blockPosition(), InitSounds.TEAM_DEATH.get(),
                        SoundSource.MASTER, 1.0F, 1.0F);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        HashMap<String, Object> dependencies = new HashMap<>();
        dependencies.put("x", entity.getX());
        dependencies.put("y", entity.getY());
        dependencies.put("z", entity.getZ());
        dependencies.put("world", entity.level());
        dependencies.put("entity", entity);
        dependencies.put("event", event);
        executeProcedure(dependencies);
    }
}