package com.modularwarfare.common.handler;

import com.modularwarfare.client.gui.GuiInventoryModified;
import com.modularwarfare.common.container.ContainerInventoryModified;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class GuiHandler {

    public static void openInventory(Player player) {
        NetworkHooks.openScreen((net.minecraft.server.level.ServerPlayer) player, new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return Component.literal("ModularWarfare Inventory");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
                return new ContainerInventoryModified(id, inv, false, player);
            }
        });
    }
}