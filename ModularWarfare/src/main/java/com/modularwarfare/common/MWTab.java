package com.modularwarfare.common;

import com.modularwarfare.ModularWarfare;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MWTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModularWarfare.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MOD_TAB = TABS.register("mod_tab", () -> CreativeModeTab.builder()
            .title(Component.literal("ModularWarfare"))
            .icon(() -> new ItemStack(Items.DIAMOND_SWORD))
            .displayItems((parameters, output) -> {
                // Добавляем всё оружие
                for (var entry : ModularWarfare.gunRegistry.values()) {
                    output.accept(new ItemStack(entry.get()));
                }
                // Добавляем все патроны
                for (var entry : ModularWarfare.ammoRegistry.values()) {
                    output.accept(new ItemStack(entry.get()));
                }
                // Добавляем все обвесы
                for (var entry : ModularWarfare.attachmentRegistry.values()) {
                    output.accept(new ItemStack(entry.get()));
                }
                // Тестовые предметы
                output.accept(new ItemStack(Items.DIAMOND));
                output.accept(new ItemStack(Items.GOLD_INGOT));
                output.accept(new ItemStack(Items.IRON_INGOT));
            })
            .build());
}