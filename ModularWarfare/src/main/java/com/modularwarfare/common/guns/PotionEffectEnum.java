package com.modularwarfare.common.guns;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public enum PotionEffectEnum {
    SPEED(MobEffects.MOVEMENT_SPEED),
    SLOWNESS(MobEffects.MOVEMENT_SLOWDOWN),
    HASTE(MobEffects.DIG_SPEED),
    MINING_FATIGUE(MobEffects.DIG_SLOWDOWN),
    STRENGTH(MobEffects.DAMAGE_BOOST),
    INSTANT_HEALTH(MobEffects.HEAL),
    INSTANT_DAMAGE(MobEffects.HARM),
    JUMP_BOOST(MobEffects.JUMP),
    NAUSEA(MobEffects.CONFUSION),
    REGENERATION(MobEffects.REGENERATION),
    RESISTANCE(MobEffects.DAMAGE_RESISTANCE),
    FIRE_RESISTANCE(MobEffects.FIRE_RESISTANCE),
    WATER_BREATHING(MobEffects.WATER_BREATHING),
    INVISIBILITY(MobEffects.INVISIBILITY),
    BLINDNESS(MobEffects.BLINDNESS),
    NIGHT_VISION(MobEffects.NIGHT_VISION),
    HUNGER(MobEffects.HUNGER),
    WEAKNESS(MobEffects.WEAKNESS),
    POISON(MobEffects.POISON),
    WITHER(MobEffects.WITHER),
    HEALTH_BOOST(MobEffects.HEALTH_BOOST),
    ABSORPTION(MobEffects.ABSORPTION),
    SATURATION(MobEffects.SATURATION),
    GLOWING(MobEffects.GLOWING),
    LEVITATION(MobEffects.LEVITATION),
    LUCK(MobEffects.LUCK),
    UNLUCK(MobEffects.UNLUCK);

    private final MobEffect effect;

    PotionEffectEnum(MobEffect effect) {
        this.effect = effect;
    }

    public MobEffect getPotion() {
        return effect;
    }
}