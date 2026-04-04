package com.modularwarfare.client.model.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.api.WeaponAnimation;
import com.modularwarfare.api.WeaponAnimations;
import com.modularwarfare.client.ClientProxy;
import com.modularwarfare.client.ClientRenderHooks;
import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.anim.ReloadType;
import com.modularwarfare.client.anim.StateEntry;
import com.modularwarfare.client.anim.StateType;
import com.modularwarfare.client.config.GunRenderConfig;
import com.modularwarfare.client.model.ModelGun;
import com.modularwarfare.client.model.objects.BreakActionData;
import com.modularwarfare.client.model.objects.CustomItemRenderType;
import com.modularwarfare.client.model.objects.CustomItemRenderer;
import com.modularwarfare.client.model.objects.RenderVariables;
import com.modularwarfare.common.guns.*;
import com.modularwarfare.common.network.PacketAimingRequest;
import com.modularwarfare.utility.ModUtil;
import com.modularwarfare.utility.NumberHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.Random;

public class RenderGunStatic extends CustomItemRenderer {
    private float slowDiff;
    private ItemStack light;
    public static boolean isLightOn;

    @Override
    public void renderItem(CustomItemRenderType type, ItemStack item, Object... data) {
        if (!(item.getItem() instanceof ItemGun)) return;

        GunType gunType = ((ItemGun) item.getItem()).type;
        if (gunType == null) return;

        ModelGun model = (ModelGun) gunType.model;
        if (model == null) return;

        Player player = null;
        AnimStateMachine anim = null;

        if (data.length > 1 && data[1] instanceof Player) {
            anim = ClientRenderHooks.getAnimMachine((Player) data[1]);
        } else {
            anim = new AnimStateMachine();
        }

        renderGun(type, item, anim, gunType, data);
    }

    private void renderGun(CustomItemRenderType renderType, ItemStack item, AnimStateMachine anim, GunType gunType, Object... data) {
        Minecraft mc = Minecraft.getInstance();
        ModelGun model = (ModelGun) gunType.model;

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

        PoseStack poseStack = new PoseStack();
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

        switch (renderType) {
            case ENTITY:
                poseStack.translate(-0.5f, 0.0f, 0.0f);
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
                break;
            case EQUIPPED_FIRST_PERSON:
                if (mc.player != null) {
                    renderFirstPerson(poseStack, buffer, model, anim, gunType, item, mc.player);
                }
                break;
        }

        int skinId = 0;
        if (item.hasTag() && item.getTag().contains("skinId")) {
            skinId = item.getTag().getInt("skinId");
        }
        String path = gunType.modelSkins != null && skinId > 0 && skinId < gunType.modelSkins.length ?
                gunType.modelSkins[skinId].getSkin() : (gunType.modelSkins != null && gunType.modelSkins.length > 0 ? gunType.modelSkins[0].getSkin() : "default");

        // Bind texture and render model
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(new ResourceLocation("modularwarfare",
                String.format("textures/skins/guns/%s.png", path))));

        poseStack.scale(model.config.extra.modelScale, model.config.extra.modelScale, model.config.extra.modelScale);
        poseStack.translate(3.0f * worldScale, 5.37f * worldScale, 0.01f * worldScale);
        poseStack.translate(model.config.extra.translateAll.x * worldScale,
                -model.config.extra.translateAll.y * worldScale,
                -model.config.extra.translateAll.z * worldScale);

        model.renderPart(poseStack, vertexConsumer, "gunModel", worldScale);

        buffer.endBatch();
    }

    private void renderFirstPerson(PoseStack poseStack, MultiBufferSource.BufferSource buffer, ModelGun model,
                                   AnimStateMachine anim, GunType gunType, ItemStack item, LocalPlayer player) {
        // Calculate ADS and sprint switches
        float adsSpeed = (0.1f + model.config.extra.adsSpeed) * RenderParameters.smoothing;
        float adsSwitch = anim.reloading ? 0.0f :
                (Minecraft.getInstance().options.keyUse.isDown() && !anim.attachmentMode) ?
                        Math.min(1.0f, RenderParameters.adsSwitch + adsSpeed) :
                        Math.max(0.0f, RenderParameters.adsSwitch - adsSpeed);
        RenderParameters.adsSwitch = adsSwitch;

        float sprintSpeed = 0.15f * RenderParameters.smoothing;
        float sprintSwitch = (player.isSprinting() && !anim.attachmentMode) ?
                Math.min(1.0f, RenderParameters.sprintSwitch + sprintSpeed) :
                Math.max(0.0f, RenderParameters.sprintSwitch - sprintSpeed);
        RenderParameters.sprintSwitch = sprintSwitch;

        float attachmentSpeed = 0.15f * RenderParameters.smoothing;
        float attachmentSwitch = anim.attachmentMode ?
                Math.min(1.0f, RenderParameters.attachmentSwitch + attachmentSpeed) :
                Math.max(0.0f, RenderParameters.attachmentSwitch - attachmentSpeed);
        RenderParameters.attachmentSwitch = attachmentSwitch;

        float crouchSpeed = 0.15f * RenderParameters.smoothing;
        float crouchSwitch = player.isCrouching() ?
                Math.min(1.0f, RenderParameters.crouchSwitch + crouchSpeed) :
                Math.max(0.0f, RenderParameters.crouchSwitch - crouchSpeed);
        RenderParameters.crouchSwitch = crouchSwitch;

        float reloadSpeed = 0.15f * RenderParameters.smoothing;
        float reloadSwitch = anim.reloading ?
                Math.max(0.0f, RenderParameters.reloadSwitch - reloadSpeed) :
                Math.min(1.0f, RenderParameters.reloadSwitch + reloadSpeed);
        RenderParameters.reloadSwitch = reloadSwitch;

        // Apply transforms
        float worldScale = 0.0625f;
        float modelScale = model.config.extra.modelScale;

        Vector3f customHipRotation = new Vector3f(
                model.config.aim.rotateHipPosition.x + model.config.sprint.sprintRotate.x * sprintSwitch * reloadSwitch,
                model.config.aim.rotateHipPosition.y + model.config.sprint.sprintRotate.y * sprintSwitch * reloadSwitch,
                model.config.aim.rotateHipPosition.z + model.config.sprint.sprintRotate.z * sprintSwitch * reloadSwitch);

        Vector3f customHipTranslate = new Vector3f(
                model.config.aim.translateHipPosition.x + model.config.sprint.sprintTranslate.x * sprintSwitch * reloadSwitch,
                model.config.aim.translateHipPosition.y + model.config.sprint.sprintTranslate.y * sprintSwitch * reloadSwitch,
                model.config.aim.translateHipPosition.z + model.config.sprint.sprintTranslate.z * sprintSwitch * reloadSwitch);

        Vector3f customAimRotation = new Vector3f(
                model.config.aim.rotateAimPosition.x,
                model.config.aim.rotateAimPosition.y,
                model.config.aim.rotateAimPosition.z);

        Vector3f customAimTranslate = new Vector3f(
                model.config.aim.translateAimPosition.x,
                model.config.aim.translateAimPosition.y,
                model.config.aim.translateAimPosition.z);

        float rotateX = customHipRotation.x - (customAimRotation.x + customHipRotation.x) * adsSwitch;
        float rotateY = 46.0f + customHipRotation.y + RenderParameters.swayHorizontal -
                (1.0f + customAimRotation.y + customHipRotation.y + RenderParameters.swayHorizontal) * adsSwitch;
        float rotateZ = 1.0f + customHipRotation.z + RenderParameters.swayVertical -
                (1.0f + customAimRotation.z + customHipRotation.z + RenderParameters.swayVertical) * adsSwitch;

        float translateX = -1.3f + customHipTranslate.x - (customAimTranslate.x + customHipTranslate.x) * adsSwitch;
        float translateY = 0.834f + customHipTranslate.y - (-0.064f + customAimTranslate.y + customHipTranslate.y) * adsSwitch;
        float translateZ = -1.05f + customHipTranslate.z - (0.35f + customAimTranslate.z + customHipTranslate.z) * adsSwitch;

        // Weapon bobbing
        float bobModifier = player.isSprinting() ? (anim.reloading ? 0.7f : 0.2f) : (anim.reloading ? 0.75f : 0.4f);
        if (ClientRenderHooks.isAimingScope) {
            bobModifier *= 0.5f;
        }

        float f1 = (player.walkDist - player.walkDistO) * bobModifier;
        float f2 = -(player.walkDist + f1 * RenderParameters.smoothing) * bobModifier;
        float f3 = (player.xxa + (player.zza - player.xxa) * RenderParameters.smoothing) * bobModifier;
        float f4 = (player.yya + (player.zza - player.yya) * RenderParameters.smoothing) * bobModifier;

        poseStack.translate(Mth.sin(f2 * (float) Math.PI) * f3 * 0.5f,
                -Math.abs(Mth.cos(f2 * (float) Math.PI) * f3), 0.0f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f2 * (float) Math.PI) * f3 * 3.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(Math.abs(Mth.cos(f2 * (float) Math.PI - 0.2f)) * f3 * 5.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(f4));

        poseStack.mulPose(Axis.XP.rotationDegrees(rotateX));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotateY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotateZ));
        poseStack.translate(translateX + model.config.extra.crouchZoom * crouchSwitch, translateY, translateZ);

        Vector3f customAttachmentModeRotation = new Vector3f(
                model.config.attachments.attachmentModeRotate.x * attachmentSwitch,
                model.config.attachments.attachmentModeRotate.y * attachmentSwitch,
                model.config.attachments.attachmentModeRotate.z * attachmentSwitch);
        poseStack.mulPose(Axis.XP.rotationDegrees(customAttachmentModeRotation.x));
        poseStack.mulPose(Axis.YP.rotationDegrees(customAttachmentModeRotation.y));
        poseStack.mulPose(Axis.ZP.rotationDegrees(customAttachmentModeRotation.z));

        // Recoil
        Random random = new Random();
        float randomShake = -1.5f + random.nextFloat() * 3.0f;
        poseStack.translate(-(anim.lastGunRecoil + (anim.gunRecoil - anim.lastGunRecoil) * RenderParameters.smoothing)
                * model.config.extra.modelRecoilBackwards, 0.0f, 0.0f);
        poseStack.mulPose(Axis.ZP.rotationDegrees((anim.lastGunRecoil + (anim.gunRecoil - anim.lastGunRecoil) * RenderParameters.smoothing)
                * model.config.extra.modelRecoilUpwards));
        poseStack.mulPose(Axis.YP.rotationDegrees((-anim.lastGunRecoil + (anim.gunRecoil - anim.lastGunRecoil) * RenderParameters.smoothing)
                * randomShake * model.config.extra.modelRecoilShake));
        poseStack.mulPose(Axis.XP.rotationDegrees((-anim.lastGunRecoil + (anim.gunRecoil - anim.lastGunRecoil) * RenderParameters.smoothing)
                * randomShake * model.config.extra.modelRecoilShake));
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