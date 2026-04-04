package com.modularwarfare.common.entity;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.entity.debug.EntityDebugAABB;
import com.modularwarfare.common.entity.debug.EntityDebugDot;
import com.modularwarfare.common.entity.debug.EntityDebugVector;
import com.modularwarfare.common.entity.decals.EntityBulletHole;
import com.modularwarfare.common.entity.decals.EntityShell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ModularWarfare.MOD_ID);

    public static final RegistryObject<EntityType<EntityDebugAABB>> DEBUG_AABB = ENTITIES.register("debug_aabb",
            () -> EntityType.Builder.<EntityDebugAABB>of(EntityDebugAABB::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .build(new ResourceLocation(ModularWarfare.MOD_ID, "debug_aabb").toString()));

    public static final RegistryObject<EntityType<EntityDebugDot>> DEBUG_DOT = ENTITIES.register("debug_dot",
            () -> EntityType.Builder.<EntityDebugDot>of(EntityDebugDot::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .build(new ResourceLocation(ModularWarfare.MOD_ID, "debug_dot").toString()));

    public static final RegistryObject<EntityType<EntityDebugVector>> DEBUG_VECTOR = ENTITIES.register("debug_vector",
            () -> EntityType.Builder.<EntityDebugVector>of(EntityDebugVector::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .build(new ResourceLocation(ModularWarfare.MOD_ID, "debug_vector").toString()));

    public static final RegistryObject<EntityType<EntityBulletHole>> BULLET_HOLE = ENTITIES.register("bullet_hole",
            () -> EntityType.Builder.<EntityBulletHole>of(EntityBulletHole::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .build(new ResourceLocation(ModularWarfare.MOD_ID, "bullet_hole").toString()));

    public static final RegistryObject<EntityType<EntityShell>> SHELL = ENTITIES.register("shell",
            () -> EntityType.Builder.<EntityShell>of(EntityShell::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .build(new ResourceLocation(ModularWarfare.MOD_ID, "shell").toString()));
}