package com.modularwarfare.client.model.animations;

import com.modularwarfare.api.WeaponAnimation;
import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.anim.ReloadType;
import com.modularwarfare.client.anim.StateEntry;
import com.modularwarfare.client.anim.StateType;
import com.modularwarfare.client.model.ModelGun;
import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.WeaponType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.ArrayList;

public class AnimationSniperBottom extends WeaponAnimation {

    public AnimationSniperBottom() {
        this.ammoLoadOffset = new Vector3f(0, -1.5f, 0);
    }

    @Override
    public void onGunAnimation(float tiltProgress, AnimStateMachine animation, PoseStack poseStack) {
        poseStack.translate(0, 0, -0.2f * tiltProgress);
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(10.0f * tiltProgress));
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-10.0f * tiltProgress));
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(15.0f * tiltProgress));
    }

    @Override
    public void onAmmoAnimation(ModelGun gunModel, float ammoProgress, int reloadAmmoCount, AnimStateMachine animation,
                                PoseStack poseStack, MultiBufferSource buffer) {
        float multiAmmoPosition = ammoProgress * 1.0f;
        int bulletNum = Mth.floor(multiAmmoPosition);
        float bulletProgress = multiAmmoPosition - bulletNum;

        poseStack.translate(ammoProgress * -0.75f, ammoProgress * -8.0f, ammoProgress * 0);
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(30.0f * ammoProgress));
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-90.0f * ammoProgress));
    }

    @Override
    public ArrayList<StateEntry> getReloadStates(ReloadType reloadType, int reloadCount) {
        ArrayList<StateEntry> states = new ArrayList<>();
        states.add(new StateEntry(StateType.Tilt, 0.15f, 0, StateEntry.MathType.Add));

        if (reloadType != ReloadType.Unload || reloadType == ReloadType.Full) {
            states.add(new StateEntry(StateType.Unload, 0.15f, 0, StateEntry.MathType.Add));
        }
        if (reloadType != ReloadType.Load || reloadType == ReloadType.Full) {
            states.add(new StateEntry(StateType.Load, 0.15f, 1.0f, StateEntry.MathType.Sub, reloadCount));
        }

        states.add(new StateEntry(StateType.Untilt, 0.15f, 1.0f, StateEntry.MathType.Sub));
        states.add(new StateEntry(StateType.MoveHands, 0.1f, 0, StateEntry.MathType.Add));
        states.add(new StateEntry(StateType.Charge, 0.1f, 1.0f, StateEntry.MathType.Sub));
        states.add(new StateEntry(StateType.Uncharge, 0.1f, 0, StateEntry.MathType.Add));
        states.add(new StateEntry(StateType.ReturnHands, 0.1f, 1.0f, StateEntry.MathType.Sub));
        return states;
    }

    @Override
    public ArrayList<StateEntry> getShootStates(ModelGun gunModel, GunType gunType) {
        ArrayList<StateEntry> states = new ArrayList<>();
        if (gunType != null && gunType.weaponType == WeaponType.BOLT_SNIPER) {
            states.add(new StateEntry(StateType.MoveHands, 0.15f, 0, StateEntry.MathType.Add));
            states.add(new StateEntry(StateType.Charge, 0.35f, 1.0f, StateEntry.MathType.Sub));
            states.add(new StateEntry(StateType.Uncharge, 0.35f, 0, StateEntry.MathType.Add));
            states.add(new StateEntry(StateType.ReturnHands, 0.15f, 1.0f, StateEntry.MathType.Sub));
        }
        return states;
    }
}