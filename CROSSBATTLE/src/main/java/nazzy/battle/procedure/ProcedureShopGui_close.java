package nazzy.battle.procedure;

import nazzy.battle.init.InitSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class ProcedureShopGui_close {

    public static void executeProcedure(HashMap<String, Object> dependencies) {
        if (dependencies.get("entity") == null) {
            System.err.println("Failed to load dependencies for procedure ShopGui_close!");
            return;
        }

        Entity entity = (Entity) dependencies.get("entity");

        if (entity instanceof Player player) {
            player.closeContainer();
        }

        if (entity.level() instanceof ServerLevel level) {
            level.playSound(null, entity.blockPosition(), InitSounds.SHOP_CLICK.get(),
                    SoundSource.NEUTRAL, 1.0F, 0.2F);
        }
    }
}