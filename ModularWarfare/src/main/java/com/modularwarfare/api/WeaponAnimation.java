package com.modularwarfare.api;

import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.anim.ReloadType;
import com.modularwarfare.client.anim.StateEntry;
import com.modularwarfare.client.anim.StateType;
import com.modularwarfare.client.model.ModelGun;
import com.modularwarfare.common.guns.GunType;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;


import java.util.ArrayList;

public class WeaponAnimation {
    public Vector3f ammoLoadOffset = new Vector3f(0.0f, 0.0f, 0.0f);

    public void onGunAnimation(float reloadRotate, AnimStateMachine animation, PoseStack poseStack) {}

    public void onAmmoAnimation(ModelGun gunModel, float ammoPosition, int reloadAmmoCount, AnimStateMachine animation, PoseStack poseStack, MultiBufferSource buffer) {}

    public ArrayList<StateEntry> getReloadStates(ReloadType reloadType, int reloadCount) {
        ArrayList<StateEntry> states = new ArrayList<>();
        states.add(new StateEntry(StateType.Tilt, 0.15f, 0.0f, StateEntry.MathType.Add));

        if (reloadType != ReloadType.Unload || reloadType == ReloadType.Full) {
            states.add(new StateEntry(StateType.Unload, 0.35f, 0.0f, StateEntry.MathType.Add));
        }

        if (reloadType != ReloadType.Load || reloadType == ReloadType.Full) {
            states.add(new StateEntry(StateType.Load, 0.35f, 1.0f, StateEntry.MathType.Sub, reloadCount));
        }

        states.add(new StateEntry(StateType.Untilt, 0.15f, 1.0f, StateEntry.MathType.Sub));
        return states;
    }

    public ArrayList<StateEntry> getShootStates(ModelGun gunModel, GunType gunType) {
        ArrayList<StateEntry> states = new ArrayList<>();
        if (gunModel.staticModel != null && gunModel.staticModel.getPart("pumpModel") != null) {
            states.add(new StateEntry(StateType.PumpOut, 0.5f, 1.0f, StateEntry.MathType.Sub));
            states.add(new StateEntry(StateType.PumpIn, 0.5f, 0.0f, StateEntry.MathType.Add));
        }
        return states;
    }
}