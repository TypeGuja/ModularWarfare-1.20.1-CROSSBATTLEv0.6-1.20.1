package com.modularwarfare.client.model.animations;

import com.modularwarfare.api.WeaponAnimation;
import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.anim.ReloadType;
import com.modularwarfare.client.anim.StateEntry;
import com.modularwarfare.client.anim.StateType;
import com.modularwarfare.client.model.ModelGun;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;

import java.util.ArrayList;

public class AnimationShotgun extends WeaponAnimation {

    @Override
    public void onGunAnimation(float tiltProgress, AnimStateMachine animation, PoseStack poseStack) {
        poseStack.translate(0, 0, -0.2f * tiltProgress);
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(10.0f * tiltProgress));
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-10.0f * tiltProgress));
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(15.0f * tiltProgress));
    }

    @Override
    public void onAmmoAnimation(ModelGun gunModel, float ammoPosition, int reloadAmmoCount, AnimStateMachine animation,
                                PoseStack poseStack, MultiBufferSource buffer) {
        float multiAmmoPosition = ammoPosition * reloadAmmoCount;
        int bulletNum = Mth.floor(multiAmmoPosition);
        float bulletProgress = multiAmmoPosition - bulletNum;
        float modelScale = gunModel.config.extra.modelScale;

        poseStack.translate(bulletProgress * -0.125f / modelScale, bulletProgress * -0.5f / modelScale, bulletProgress * -0.0625f / modelScale);
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(1.0f * bulletProgress));
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(1.0f * bulletProgress));
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(20.0f * bulletProgress));
    }

    @Override
    public ArrayList<StateEntry> getReloadStates(ReloadType reloadType, int reloadCount) {
        ArrayList<StateEntry> states = new ArrayList<>();
        states.add(new StateEntry(StateType.Tilt, 0.15f, 0, StateEntry.MathType.Add));
        states.add(new StateEntry(StateType.Load, 0.35f, 1.0f, StateEntry.MathType.Sub, reloadCount));
        states.add(new StateEntry(StateType.Untilt, 0.15f, 1.0f, StateEntry.MathType.Sub));
        return states;
    }
}