package com.modularwarfare.common.handler;

import com.modularwarfare.ModularWarfare;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandlerEntity {

    @SubscribeEvent
    public void onLivingHurt(LivingAttackEvent event) {
        if (event.getEntity().level().isClientSide) return;

        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        ModularWarfare.PROXY.addBlood(entity, 10, true);
    }

    @SubscribeEvent
    public void onPlayerKilled(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            ModularWarfare.PROXY.resetSens();
        }
    }
}