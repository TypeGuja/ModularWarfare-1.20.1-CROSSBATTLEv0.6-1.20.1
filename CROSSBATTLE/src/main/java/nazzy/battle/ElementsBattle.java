package nazzy.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public class ElementsBattle {

    public static List<Supplier<Block>> blocks = new ArrayList<>();
    public static List<Supplier<Item>> items = new ArrayList<>();

    public static void register(IEventBus bus) {
        // Registration handled in init classes
    }
}