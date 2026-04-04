package nazzy.battle.init;

import nazzy.battle.Battle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Battle.MOD_ID);

    public static final RegistryObject<SoundEvent> GOT = register("got");
    public static final RegistryObject<SoundEvent> KNIFE_THROW = register("knife_throw");
    public static final RegistryObject<SoundEvent> SHOP_START = register("shop_start");
    public static final RegistryObject<SoundEvent> SHOP_CLICK = register("shop_click");
    public static final RegistryObject<SoundEvent> SHOP_ERROR = register("shop_error");
    public static final RegistryObject<SoundEvent> SHOP_SUCC = register("shop_succ");
    public static final RegistryObject<SoundEvent> TEAM_ALL_DEAD = register("team_all_dead");
    public static final RegistryObject<SoundEvent> TEAM_ONE_DEAD = register("team_one_dead");
    public static final RegistryObject<SoundEvent> TEAM_RESPAWN = register("team_respawn");
    public static final RegistryObject<SoundEvent> MATCH_STARTS = register("match_starts");
    public static final RegistryObject<SoundEvent> TEAM_DEATH = register("team_death");

    private static RegistryObject<SoundEvent> register(String name) {
        ResourceLocation id = new ResourceLocation(Battle.MOD_ID, name);
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
}