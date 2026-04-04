package nazzy.battle;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

public interface IProxyBattle {
    default void clientSetup(FMLClientSetupEvent event) {}
    default void serverSetup(FMLDedicatedServerSetupEvent event) {}
}