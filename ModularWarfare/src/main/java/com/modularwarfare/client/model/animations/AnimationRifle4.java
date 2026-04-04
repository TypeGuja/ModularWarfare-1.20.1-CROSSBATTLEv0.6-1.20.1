package com.modularwarfare.client.model.animations;

import com.modularwarfare.api.WeaponAnimation;
import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.model.ModelGun;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;

public class AnimationRifle4 extends WeaponAnimation {

    @Override
    public void onGunAnimation(float tiltProgress, AnimStateMachine animation, PoseStack poseStack) {
        poseStack.translate(0.1f * tiltProgress, 0.2f * tiltProgress, -0.25f * tiltProgress);
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-60.0f * tiltProgress));
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-10.0f * tiltProgress));
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(70.0f * tiltProgress));
    }

    @Override
    public void onAmmoAnimation(ModelGun gunModel, float ammoProgress, int reloadAmmoCount, AnimStateMachine animation,
                                PoseStack poseStack, MultiBufferSource buffer) {
        float multiAmmoPosition = ammoProgress * 1.0f;
        int bulletNum = Mth.floor(multiAmmoPosition);
        float bulletProgress = multiAmmoPosition - bulletNum;

        poseStack.translate(ammoProgress * -12.75f, ammoProgress * -5.0f, ammoProgress * -3.0f);
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-50.0f * ammoProgress));
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-150.0f * ammoProgress));
    }
}