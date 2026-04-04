package nazzy.battle.procedure;

import nazzy.battle.BattleVariables;
import nazzy.battle.init.InitItems;
import nazzy.battle.init.InitSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber
public class ProcedurePlayerRespawns {

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("entity") == null || dependencies.get("world") == null) {
            System.err.println("Failed to load dependencies for procedure PlayerRespawns!");
            return;
        }

        Entity entity = (Entity) dependencies.get("entity");
        if (!(entity instanceof Player player)) return;

        var persistentData = player.getPersistentData();
        int deathLimit = persistentData.getInt("death_limit");

        BattleVariables.MapVariables vars = BattleVariables.MapVariables.get(player.level());

        if (vars.isPlaying) {
            deathLimit++;
            persistentData.putInt("death_limit", deathLimit);

            ItemStack ledorub = new ItemStack(InitItems.LEDORUB.get(), 1);
            player.getInventory().add(ledorub);

            if (deathLimit == 1) {
                sendTitle(player, "Осталось: 3 возрождения.", "gold");
                playSound(player, InitSounds.TEAM_RESPAWN.get());
            } else if (deathLimit == 2) {
                sendTitle(player, "Осталось: 2 возрождения.", "gold");
                playSound(player, InitSounds.TEAM_RESPAWN.get());
            } else if (deathLimit == 3) {
                sendTitle(player, "Осталось: 1 возрождение.", "gold");
                playSound(player, InitSounds.TEAM_RESPAWN.get());
            } else if (deathLimit >= 4) {
                persistentData.putInt("death_limit", 0);

                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.setGameMode(GameType.SPECTATOR);
                }

                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.getServer().getCommands().performPrefixedCommand(
                            serverLevel.getServer().createCommandSourceStack(),
                            "title @a actionbar {\"selector\":\"@p\",\"text\":\" выбывает из игры.\"}");
                    serverLevel.playSound(null, player.blockPosition(), InitSounds.TEAM_ONE_DEAD.get(),
                            SoundSource.MASTER, 1.0F, 1.0F);
                }
            }
        }
    }

    private static void sendTitle(Player player, String message, String color) {
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().getCommands().performPrefixedCommand(
                    serverLevel.getServer().createCommandSourceStack(),
                    "title " + player.getName().getString() + " title {\"text\":\"" + message + "\",\"color\":\"" + color + "\"}");
        }
    }

    private static void playSound(Player player, net.minecraft.sounds.SoundEvent sound) {
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, player.blockPosition(), sound, SoundSource.MASTER, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        HashMap<String, Object> dependencies = new HashMap<>();
        dependencies.put("x", player.getX());
        dependencies.put("y", player.getY());
        dependencies.put("z", player.getZ());
        dependencies.put("world", player.level());
        dependencies.put("entity", player);
        dependencies.put("endconquered", event.isEndConquered());
        dependencies.put("event", event);
        executeProcedure(dependencies);
    }
}