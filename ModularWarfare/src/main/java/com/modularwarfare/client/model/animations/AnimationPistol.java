package com.modularwarfare.client.model.animations;

import com.modularwarfare.api.WeaponAnimation;
import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.anim.ReloadType;
import com.modularwarfare.client.anim.StateEntry;
import com.modularwarfare.client.anim.StateType;
import com.modularwarfare.client.model.ModelGun;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;

import java.util.ArrayList;

public class AnimationPistol extends WeaponAnimation {

    @Override
    public void onGunAnimation(float tiltProgress, AnimStateMachine animation, PoseStack poseStack) {
        poseStack.translate(0.2f * tiltProgress, 0.1f * tiltProgress, -0.1f * tiltProgress);
        poseStack.mulPose(Axis.XP.rotationDegrees(20.0f * tiltProgress));
        poseStack.mulPose(Axis.YP.rotationDegrees(-10.0f * tiltProgress));
        poseStack.mulPose(Axis.ZP.rotationDegrees(25.0f * tiltProgress));
    }

    @Override
    public void onAmmoAnimation(ModelGun gunModel, float ammoProgress, int reloadAmmoCount, AnimStateMachine animation, PoseStack poseStack, MultiBufferSource buffer) {
        float multiAmmoPosition = ammoProgress * 1.0f;
        int bulletNum = Mth.floor(multiAmmoPosition);
        float bulletProgress = multiAmmoPosition - bulletNum;

        poseStack.translate(ammoProgress * -0.75f, ammoProgress * -8.0f, ammoProgress * 0.0f);
        poseStack.mulPose(Axis.XP.rotationDegrees(30.0f * ammoProgress));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-20.0f * ammoProgress));
    }

    @Override
    public ArrayList<StateEntry> getReloadStates(ReloadType reloadType, int reloadCount) {
        ArrayList<StateEntry> states = new ArrayList<>();
        states.add(new StateEntry(StateType.Tilt, 0.2f, 0.0f, StateEntry.MathType.Add));
        states.add(new StateEntry(StateType.Unload, 0.2f, 0.0f, StateEntry.MathType.Add));
        states.add(new StateEntry(StateType.Load, 0.2f, 1.0f, StateEntry.MathType.Sub));
        states.add(new StateEntry(StateType.Untilt, 0.2f, 1.0f, StateEntry.MathType.Sub));
        states.add(new StateEntry(StateType.Charge, 0.18f, 1.0f, StateEntry.MathType.Sub));
        states.add(new StateEntry(StateType.Uncharge, 0.02f, 0.0f, StateEntry.MathType.Add));
        return states;
    }
}