package nazzy.battle.procedure;

import nazzy.battle.BattleVariables;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class ProcedurePlayerTick {

    private static final ConcurrentHashMap<UUID, Boolean> initializedPlayers = new ConcurrentHashMap<>();

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("entity") == null || dependencies.get("world") == null) {
            return;
        }

        Entity entity = (Entity) dependencies.get("entity");
        if (!(entity instanceof Player player)) return;

        UUID playerId = player.getUUID();

        // Проверяем, был ли игрок уже инициализирован
        if (initializedPlayers.containsKey(playerId)) {
            return;
        }

        var persistentData = player.getPersistentData();
        boolean empty = persistentData.getBoolean("empty");

        BattleVariables.MapVariables vars = BattleVariables.MapVariables.get(player.level());

        if (!empty) {
            if (persistentData.getBoolean("red")) {
                vars.red_count += 1;
                vars.syncData(player.level());
                persistentData.putBoolean("empty", true);
                player.displayClientMessage(Component.literal("Красная команда."), true);
            } else if (persistentData.getBoolean("blue")) {
                vars.blue_count += 1;
                vars.syncData(player.level());
                persistentData.putBoolean("empty", true);
                player.displayClientMessage(Component.literal("Синяя команда."), true);
            } else if (persistentData.getBoolean("green")) {
                vars.green_count += 1;
                vars.syncData(player.level());
                persistentData.putBoolean("empty", true);
                player.displayClientMessage(Component.literal("Зелёная команда."), true);
            } else if (persistentData.getBoolean("yellow")) {
                vars.yellow_count += 1;
                vars.syncData(player.level());
                persistentData.putBoolean("empty", true);
                player.displayClientMessage(Component.literal("Жёлтая команда."), true);
            }
            initializedPlayers.put(playerId, true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player.tickCount % 20 == 0) {
            Player player = event.player;
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
}