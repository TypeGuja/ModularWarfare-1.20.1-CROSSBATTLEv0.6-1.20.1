package com.modularwarfare.client.model.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.api.WeaponAnimation;
import com.modularwarfare.api.WeaponAnimations;
import com.modularwarfare.client.ClientRenderHooks;
import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.anim.StateEntry;
import com.modularwarfare.client.anim.StateType;
import com.modularwarfare.client.config.GunRenderConfig;
import com.modularwarfare.client.model.ModelGun;
import com.modularwarfare.client.model.objects.CustomItemRenderType;
import com.modularwarfare.client.model.objects.CustomItemRenderer;
import com.modularwarfare.common.guns.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.Optional;

public class RenderGunStatic extends CustomItemRenderer {
    private float slowDiff;
    private ItemStack light;
    public static boolean isLightOn;

    // Флаг для однократного логирования
    private static boolean DEBUG_LOGGED = false;

    @Override
    public void renderItem(CustomItemRenderType type, ItemStack item, Object... data) {
        if (!DEBUG_LOGGED) {
            ModularWarfare.LOGGER.info("========== RenderGunStatic.renderItem CALLED ==========");
            ModularWarfare.LOGGER.info("Type: " + type);
            ModularWarfare.LOGGER.info("Item: " + item);
            ModularWarfare.LOGGER.info("Data length: " + data.length);
            for (int i = 0; i < data.length; i++) {
                ModularWarfare.LOGGER.info("  data[" + i + "]: " + (data[i] != null ? data[i].getClass().getSimpleName() : "null"));
            }
        }

        if (!(item.getItem() instanceof ItemGun)) {
            if (!DEBUG_LOGGED) ModularWarfare.LOGGER.warn("Item is not ItemGun: " + item.getItem());
            return;
        }

        GunType gunType = ((ItemGun) item.getItem()).type;
        if (gunType == null) {
            if (!DEBUG_LOGGED) ModularWarfare.LOGGER.warn("GunType is null");
            return;
        }

        ModelGun model = (ModelGun) gunType.model;
        if (model == null) {
            if (!DEBUG_LOGGED) ModularWarfare.LOGGER.warn("Model is null for gun: " + gunType.internalName);
            return;
        }

        if (!DEBUG_LOGGED) {
            ModularWarfare.LOGGER.info("Model loaded: " + (model.staticModel != null));
            ModularWarfare.LOGGER.info("Model parts: " + (model.staticModel != null ? model.staticModel.getParts().size() : 0));
        }

        Player player = null;
        AnimStateMachine anim = null;
        PoseStack poseStack = null;
        MultiBufferSource.BufferSource buffer = null;
        int light = 15728880;
        Level level = null;

        // Разбираем параметры из data
        for (Object obj : data) {
            if (obj instanceof Level l) level = l;
            if (obj instanceof Player p) player = p;
            if (obj instanceof AnimStateMachine a) anim = a;
            if (obj instanceof PoseStack ps) poseStack = ps;
            if (obj instanceof MultiBufferSource.BufferSource bs) buffer = bs;
            if (obj instanceof Integer l) light = l;
        }

        if (!DEBUG_LOGGED) {
            ModularWarfare.LOGGER.info("Parsed params: player=" + player + ", anim=" + anim +
                    ", poseStack=" + poseStack + ", buffer=" + buffer);
        }

        if (anim == null) {
            if (player != null) {
                anim = ClientRenderHooks.getAnimMachine(player);
            } else {
                anim = new AnimStateMachine();
            }
            if (!DEBUG_LOGGED) ModularWarfare.LOGGER.info("Created new AnimStateMachine");
        }

        // Для EQUIPPED_FIRST_PERSON poseStack и buffer обязательны
        if (type == CustomItemRenderType.EQUIPPED_FIRST_PERSON) {
            if (poseStack == null) {
                ModularWarfare.LOGGER.error("PoseStack is null for EQUIPPED_FIRST_PERSON render!");
                return;
            }
            if (buffer == null) {
                buffer = Minecraft.getInstance().renderBuffers().bufferSource();
                if (!DEBUG_LOGGED) ModularWarfare.LOGGER.info("Created new BufferSource");
            }
        } else {
            poseStack = new PoseStack();
            buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        }

        renderGun(type, item, anim, gunType, poseStack, buffer, light, data);

        if (!DEBUG_LOGGED) {
            DEBUG_LOGGED = true;
            ModularWarfare.LOGGER.info("========== RenderGunStatic.renderItem FINISHED ==========");
        }
    }

    private void renderGun(CustomItemRenderType renderType, ItemStack item, AnimStateMachine anim, GunType gunType,
                           PoseStack poseStack, MultiBufferSource.BufferSource buffer, int light, Object... data) {
        Minecraft mc = Minecraft.getInstance();
        ModelGun model = (ModelGun) gunType.model;

        if (model == null) {
            ModularWarfare.LOGGER.error("Model is null in renderGun for: " + gunType.internalName);
            return;
        }

        float tiltProgress = 0.0f;
        Optional<StateEntry> currentReloadState = anim.getReloadState();
        if (currentReloadState.isPresent()) {
            if (currentReloadState.get().stateType != StateType.Tilt && currentReloadState.get().stateType != StateType.Untilt) {
                tiltProgress = currentReloadState.get().currentValue;
            } else if (anim.tiltHold) {
                tiltProgress = 1.0f;
            }
        }

        float worldScale = 0.0625f;

        switch (renderType) {
            case ENTITY:
                poseStack.translate(-0.5f, 0.0f, 0.0f);
                renderGunModel(poseStack, buffer, model, item, gunType, worldScale, light);
                break;

            case EQUIPPED:
                LivingEntity entityLiving = (LivingEntity) data[1];
                float crouchOffset = entityLiving.isCrouching() ? -0.18f : 0.0f;
                poseStack.mulPose(Axis.XP.rotationDegrees(0.0f));
                poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
                poseStack.translate(0.25f, 0.0f, -0.05f);
                poseStack.scale(model.config.thirdPerson.thirdPersonScale,
                        model.config.thirdPerson.thirdPersonScale,
                        model.config.thirdPerson.thirdPersonScale);
                poseStack.translate(model.config.thirdPerson.thirdPersonOffset.x,
                        model.config.thirdPerson.thirdPersonOffset.y + crouchOffset,
                        model.config.thirdPerson.thirdPersonOffset.z);
                renderGunModel(poseStack, buffer, model, item, gunType, worldScale, light);
                break;

            case BACK:
                poseStack.scale(model.config.thirdPerson.thirdPersonScale,
                        model.config.thirdPerson.thirdPersonScale,
                        model.config.thirdPerson.thirdPersonScale);
                poseStack.translate(-0.32f, 1.3f, -0.23f);
                poseStack.translate(model.config.thirdPerson.backPersonOffset.x,
                        model.config.thirdPerson.backPersonOffset.y,
                        model.config.thirdPerson.backPersonOffset.z);
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
                poseStack.mulPose(Axis.ZP.rotationDegrees(270.0f));
                renderGunModel(poseStack, buffer, model, item, gunType, worldScale, light);
                break;

            case EQUIPPED_FIRST_PERSON:
                if (mc.player != null) {
                    ModularWarfare.LOGGER.info("Rendering first person for: " + gunType.internalName);
                    renderFirstPerson(poseStack, buffer, model, anim, gunType, item, mc.player, light);
                    return;
                }
                break;
        }

        if (renderType != CustomItemRenderType.EQUIPPED_FIRST_PERSON) {
            buffer.endBatch();
        }
    }

    private void renderGunModel(PoseStack poseStack, MultiBufferSource.BufferSource buffer, ModelGun model,
                                ItemStack item, GunType gunType, float worldScale, int light) {
        int skinId = 0;
        if (item.hasTag() && item.getTag().contains("skinId")) {
            skinId = item.getTag().getInt("skinId");
        }

        String skinAsset = gunType.modelSkins != null && skinId > 0 && skinId < gunType.modelSkins.length ?
                gunType.modelSkins[skinId].getSkin() :
                (gunType.modelSkins != null && gunType.modelSkins.length > 0 ? gunType.modelSkins[0].getSkin() : gunType.internalName);

        ResourceLocation texture = new ResourceLocation("modularwarfare",
                String.format("textures/skins/guns/%s.png", skinAsset));

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));

        poseStack.pushPose();
        poseStack.scale(model.config.extra.modelScale, model.config.extra.modelScale, model.config.extra.modelScale);
        poseStack.translate(3.0f * worldScale, 5.37f * worldScale, 0.01f * worldScale);
        poseStack.translate(model.config.extra.translateAll.x * worldScale,
                -model.config.extra.translateAll.y * worldScale,
                -model.config.extra.translateAll.z * worldScale);

        if (model.staticModel != null) {
            model.renderPart(poseStack, vertexConsumer, "gunModel", worldScale);
            model.renderPart(poseStack, vertexConsumer, "slideModel", worldScale);
            model.renderPart(poseStack, vertexConsumer, "ammoModel", worldScale);
            model.renderPart(poseStack, vertexConsumer, "flashModel", worldScale);
        }

        poseStack.popPose();
    }

    private void renderFirstPerson(PoseStack poseStack, MultiBufferSource.BufferSource buffer, ModelGun model,
                                   AnimStateMachine anim, GunType gunType, ItemStack item, LocalPlayer player, int light) {

        float worldScale = 0.0625f;
        float modelScale = model.config.extra.modelScale;

        // Загружаем текстуру
        int skinId = 0;
        if (item.hasTag() && item.getTag().contains("skinId")) {
            skinId = item.getTag().getInt("skinId");
        }

        String skinAsset = gunType.modelSkins != null && skinId >= 0 && skinId < gunType.modelSkins.length ?
                gunType.modelSkins[skinId].getSkin() :
                (gunType.modelSkins != null && gunType.modelSkins.length > 0 ? gunType.modelSkins[0].getSkin() : gunType.internalName);

        ResourceLocation texture = new ResourceLocation("modularwarfare",
                String.format("textures/skins/guns/%s.png", skinAsset));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));

        poseStack.pushPose();

        // === ГАРАНТИРОВАННО ВИДИМАЯ ПОЗИЦИЯ ===
        // Помещаем модель прямо перед камерой
        poseStack.translate(0.5f, -0.5f, -1.0f);  // X, Y, Z

        // Применяем масштаб
        poseStack.scale(modelScale, modelScale, modelScale);

        // Рендерим модель
        if (model.staticModel != null) {
            model.renderPart(poseStack, consumer, "gunModel", worldScale);
            model.renderPart(poseStack, consumer, "slideModel", worldScale);
            model.renderPart(poseStack, consumer, "ammoModel", worldScale);
            model.renderPart(poseStack, consumer, "flashModel", worldScale);
        }

        poseStack.popPose();
    }

    public static String getStaticArmState(ModelGun model, AnimStateMachine anim) {
        Optional<StateEntry> currentShootState = anim.getShootState();
        Optional<StateEntry> currentReloadState = anim.getReloadState();

        float pumpCurrent = 1.0f;
        if (currentShootState.isPresent()) {
            if (currentShootState.get().stateType != StateType.PumpOut && currentShootState.get().stateType != StateType.PumpIn) {
                pumpCurrent = currentShootState.get().currentValue;
            }
        }

        float chargeCurrent = 1.0f;
        if (currentReloadState.isPresent()) {
            if (currentReloadState.get().stateType != StateType.Charge && currentReloadState.get().stateType != StateType.Uncharge) {
                chargeCurrent = currentReloadState.get().currentValue;
            }
        } else if (currentShootState.isPresent()) {
            if (currentShootState.get().stateType != StateType.Charge && currentShootState.get().stateType != StateType.Uncharge) {
                chargeCurrent = currentShootState.get().currentValue;
            }
        }

        if (model.config.arms.leftHandAmmo) {
            if ((!anim.isReloadState(StateType.MoveHands) || anim.isReloadState(StateType.ReturnHands)) &&
                    (!anim.isShootState(StateType.MoveHands) || anim.isShootState(StateType.ReturnHands))) {
                return "ToFrom";
            }
            if (anim.reloading && model.isType(GunRenderConfig.Arms.EnumArm.Right, GunRenderConfig.Arms.EnumAction.Pump)) {
                return "Pump";
            }
            if (chargeCurrent > 0.66 && model.isType(GunRenderConfig.Arms.EnumArm.Right, GunRenderConfig.Arms.EnumAction.Charge) && chargeCurrent < 1.0f) {
                return "Charge";
            }
            if ((!anim.isReloadState(StateType.Charge) || anim.isReloadState(StateType.Uncharge)) &&
                    model.isType(GunRenderConfig.Arms.EnumArm.Right, GunRenderConfig.Arms.EnumAction.Bolt)) {
                return "Bolt";
            }
            if ((!anim.isShootState(StateType.Charge) || anim.isShootState(StateType.Uncharge)) &&
                    model.isType(GunRenderConfig.Arms.EnumArm.Right, GunRenderConfig.Arms.EnumAction.Bolt)) {
                return "Bolt";
            }
            if (anim.reloading && model.isType(GunRenderConfig.Arms.EnumArm.Right, GunRenderConfig.Arms.EnumAction.Pump)) {
                return "Default";
            }
            return "Reload";
        }

        if (anim.reloading && model.isType(GunRenderConfig.Arms.EnumArm.Left, GunRenderConfig.Arms.EnumAction.Pump)) {
            return "Pump";
        }
        if (chargeCurrent > 0.9 && model.isType(GunRenderConfig.Arms.EnumArm.Right, GunRenderConfig.Arms.EnumAction.Charge) && chargeCurrent < 1.0f) {
            return "Charge";
        }
        if (chargeCurrent > 0.9 && model.isType(GunRenderConfig.Arms.EnumArm.Right, GunRenderConfig.Arms.EnumAction.Bolt)) {
            return "Bolt";
        }
        if (anim.reloading && model.isType(GunRenderConfig.Arms.EnumArm.Left, GunRenderConfig.Arms.EnumAction.Pump)) {
            return "Default";
        }
        return "Reload";
    }
}