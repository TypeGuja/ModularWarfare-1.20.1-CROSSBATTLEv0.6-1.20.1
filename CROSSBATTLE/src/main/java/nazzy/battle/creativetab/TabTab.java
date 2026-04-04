package nazzy.battle.creativetab;

import nazzy.battle.Battle;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TabTab {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Battle.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BATTLE_TAB = TABS.register("assets/battle",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.battle"))
                    .icon(() -> new ItemStack(Items.DIAMOND))
                    .displayItems((parameters, output) -> {
                        // Предметы будут добавлены позже
                    })
                    .build());
}