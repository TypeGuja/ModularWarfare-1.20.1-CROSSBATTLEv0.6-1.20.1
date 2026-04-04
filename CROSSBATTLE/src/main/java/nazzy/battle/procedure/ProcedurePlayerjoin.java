package nazzy.battle.procedure;

import nazzy.battle.init.InitSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber
public class ProcedurePlayerjoin {

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("entity") == null || dependencies.get("world") == null) {
            System.err.println("Failed to load dependencies for procedure Playerjoin!");
            return;
        }

        Entity entity = (Entity) dependencies.get("entity");

        // Set empty scoreboard value (handled by team assignment logic)
        // This would normally set a scoreboard objective

        if (entity instanceof Player player && !player.level().isClientSide()) {
            player.displayClientMessage(Component.literal("Добро Пожаловать."), true);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        HashMap<String, Object> dependencies = new HashMap<>();
        dependencies.put("x", player.getX());
        dependencies.put("y", player.getY());
        dependencies.put("z", player.getZ());
        dependencies.put("world", player.level());
        dependencies.put("entity", player);
        dependencies.put("event", event);
        executeProcedure(dependencies);
    }
}