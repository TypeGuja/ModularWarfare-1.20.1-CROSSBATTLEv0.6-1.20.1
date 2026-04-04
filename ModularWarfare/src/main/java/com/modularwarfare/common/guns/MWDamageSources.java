package com.modularwarfare.common.guns;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import com.modularwarfare.ModularWarfare;

public class MWDamageSources {
    public static final ResourceKey<DamageType> WEAPON_GENERIC_KEY = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModularWarfare.MOD_ID, "generic_weapon"));
    public static final ResourceKey<DamageType> WEAPON_HEADSHOT_KEY = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModularWarfare.MOD_ID, "generic_weapon_hs"));
    public static final ResourceKey<DamageType> VEHICLE_KEY = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ModularWarfare.MOD_ID, "vehicle"));

    public static DamageSource causeWeaponDamage(Player player, boolean headshot) {
        return new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(headshot ? WEAPON_HEADSHOT_KEY : WEAPON_GENERIC_KEY));
    }

    public static DamageSource causeVehicleDamage(Entity vehicle, Entity attacker) {
        return new DamageSource(vehicle.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(VEHICLE_KEY));
    }
}