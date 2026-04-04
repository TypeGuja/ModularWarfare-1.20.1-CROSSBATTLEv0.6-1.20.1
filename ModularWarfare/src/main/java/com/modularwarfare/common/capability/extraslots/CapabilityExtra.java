package com.modularwarfare.common.capability.extraslots;


import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.network.PacketSyncExtraSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = ModularWarfare.MOD_ID)
public class CapabilityExtra {
    public static final Capability<com.modularwarfare.common.capability.extraslots.IExtraItemHandler> EXTRA_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation("modularwarfare", "extraslots"),
                    new com.modularwarfare.common.capability.extraslots.ExtraContainerProvider(new com.modularwarfare.common.capability.extraslots.ExtraContainer((Player) event.getObject())));
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide && event.getEntity() instanceof Player player) {
            sync(player, Collections.singletonList(player));
        }
    }

    @SubscribeEvent
    public static void playerDeath(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Level level = player.level();

        if (!level.isClientSide && !level.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_KEEPINVENTORY)
                && ModularWarfare.ModConfig.INSTANCE != null && ModularWarfare.ModConfig.INSTANCE.dropExtraSlotsOnDeath) {

            player.getCapability(EXTRA_CAPABILITY).ifPresent(extra -> {
                for (int i = 0; i < extra.getSlots(); i++) {
                    ItemStack stack = extra.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        ItemEntity item = new ItemEntity(level, player.getX(), player.getY() + player.getEyeHeight(), player.getZ(), stack);
                        item.setDefaultPickUpDelay();
                        event.getDrops().add(item);
                        extra.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            });
        }
    }

    public static void sync(Player player, Collection<? extends Player> receivers) {
        player.getCapability(EXTRA_CAPABILITY).ifPresent(extra -> {
            for (int i = 0; i < extra.getSlots(); i++) {
                PacketSyncExtraSlot packet = new PacketSyncExtraSlot(player, i, extra.getStackInSlot(i));
                for (Player receiver : receivers) {
                    if (receiver instanceof ServerPlayer serverPlayer) {
                        ModularWarfare.NETWORK.sendTo(packet, serverPlayer);
                    }
                }
            }
        });
    }
}