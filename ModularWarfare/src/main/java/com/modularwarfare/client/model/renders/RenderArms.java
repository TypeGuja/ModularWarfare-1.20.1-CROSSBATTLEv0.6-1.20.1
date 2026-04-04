package com.modularwarfare.client.model.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.modularwarfare.api.WeaponAnimation;
import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.anim.ReloadType;
import com.modularwarfare.client.anim.StateEntry;
import com.modularwarfare.client.anim.StateType;
import com.modularwarfare.client.model.ModelGun;
import com.modularwarfare.utility.NumberHelper;
import org.joml.Vector3f;

import java.util.Optional;

public class RenderArms {

    public static void renderToFrom(PoseStack poseStack, ModelGun model, AnimStateMachine anim, float smoothing,
                                    Vector3f targetRot, Vector3f targetPos, Vector3f originRot, Vector3f originPos, boolean leftHand) {
        float progress = 1.0f;
        Optional<StateEntry> reloadState = anim.getReloadState();
        if (reloadState.isPresent()) {
            progress = reloadState.get().currentValue;
        } else {
            Optional<StateEntry> shootState = anim.getShootState();
            if (shootState.isPresent()) {
                progress = shootState.get().currentValue;
            }
        }

        if (NumberHelper.subtractVector(targetPos, originPos) != null) {
            Vector3f offsetPosition = NumberHelper.multiplyVector(NumberHelper.subtractVector(targetPos, originPos), progress);

            float cancelOut = 1.0f;
            if (reloadState.isPresent()) {
                if (reloadState.get().stateType != StateType.ReturnHands) {
                    cancelOut = 1.0f;
                } else {
                    cancelOut = 0.0f;
                }
            } else if (anim.getShootState().isPresent()) {
                if (anim.getShootState().get().stateType != StateType.ReturnHands) {
                    cancelOut = 1.0f;
                } else {
                    cancelOut = 0.0f;
                }
            }

            float chargeModX = Math.abs(1.0f + 0.0f * smoothing) * (model.config.bolt.chargeModifier.x * model.config.extra.modelScale);
            float chargeModY = Math.abs(1.0f + 0.0f * smoothing) * (model.config.bolt.chargeModifier.y * model.config.extra.modelScale);
            float chargeModZ = Math.abs(1.0f + 0.0f * smoothing) * (model.config.bolt.chargeModifier.z * model.config.extra.modelScale);

            poseStack.translate(originPos.x + offsetPosition.x + cancelOut * chargeModX, 0, 0);
            poseStack.translate(0, originPos.y + offsetPosition.y + cancelOut * chargeModY, 0);
            poseStack.translate(0, 0, originPos.z + offsetPosition.z + cancelOut * chargeModZ);

            Vector3f offsetRotation = NumberHelper.multiplyVector(NumberHelper.subtractVector(targetRot, originRot), progress);

            if (leftHand) {
                poseStack.translate(0.225f, 0.75f, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(originRot.x + offsetRotation.x));
                poseStack.mulPose(Axis.YP.rotationDegrees(originRot.y + offsetRotation.y));
                poseStack.mulPose(Axis.ZP.rotationDegrees(originRot.z + offsetRotation.z));
                poseStack.translate(-0.225f, -0.75f, 0);
            } else {
                poseStack.translate(-0.225f, 0.75f, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(originRot.x + offsetRotation.x));
                poseStack.mulPose(Axis.YP.rotationDegrees(originRot.y + offsetRotation.y));
                poseStack.mulPose(Axis.ZP.rotationDegrees(originRot.z + offsetRotation.z));
                poseStack.translate(0.225f, -0.75f, 0);
            }
        }
    }

    public static void renderArmPump(PoseStack poseStack, ModelGun model, AnimStateMachine anim, float smoothing,
                                     Vector3f reloadRot, Vector3f reloadPos, boolean leftHand) {
        float pumpCurrent = 1.0f;
        float pumpLast = 1.0f;
        Optional<StateEntry> currentShootState = anim.getShootState();

        if (currentShootState.isPresent()) {
            if (currentShootState.get().stateType != StateType.PumpOut && currentShootState.get().stateType != StateType.PumpIn) {
                pumpCurrent = currentShootState.get().currentValue;
                pumpLast = currentShootState.get().lastValue;
            }
        }

        if (leftHand) {
            poseStack.translate(model.config.arms.leftArm.armPos.x - (1.0f - Math.abs(pumpLast + (pumpCurrent - pumpLast) * smoothing)) * model.config.bolt.pumpHandleDistance,
                    model.config.arms.leftArm.armPos.y, model.config.arms.leftArm.armPos.z);
            handleRotateLeft(poseStack, reloadRot);
        } else {
            poseStack.translate(model.config.arms.rightArm.armPos.x - (1.0f - Math.abs(pumpLast + (pumpCurrent - pumpLast) * smoothing)) * model.config.bolt.pumpHandleDistance,
                    model.config.arms.rightArm.armPos.y, model.config.arms.rightArm.armPos.z);
            handleRotateRight(poseStack, reloadRot);
        }
    }

    public static void renderArmCharge(PoseStack poseStack, ModelGun model, AnimStateMachine anim, float smoothing,
                                       Vector3f reloadRot, Vector3f reloadPos, Vector3f defaultRot, Vector3f defaultPos, boolean leftHand) {
        Vector3f offsetPosition = NumberHelper.multiplyVector(NumberHelper.subtractVector(reloadPos, defaultPos), 1.0f);

        float chargeCurrent = 1.0f;
        float chargeLast = 1.0f;
        Optional<StateEntry> currentReloadState = anim.getReloadState();

        if (currentReloadState.isPresent()) {
            if (currentReloadState.get().stateType != StateType.Charge && currentReloadState.get().stateType != StateType.Uncharge) {
                chargeCurrent = currentReloadState.get().currentValue;
                chargeLast = currentReloadState.get().lastValue;
            }
        }

        poseStack.translate(defaultPos.x + offsetPosition.x + Math.abs(chargeLast + (chargeCurrent - chargeLast) * smoothing) * (model.config.extra.chargeHandleDistance * model.config.extra.modelScale), 0, 0);
        poseStack.translate(0, defaultPos.y + offsetPosition.y, 0);
        poseStack.translate(0, 0, defaultPos.z + offsetPosition.z);

        Vector3f offsetRotation = NumberHelper.multiplyVector(NumberHelper.subtractVector(reloadRot, defaultRot), 1.0f);

        if (leftHand) {
            poseStack.translate(0.225f, 0.75f, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(defaultRot.x + offsetRotation.x));
            poseStack.mulPose(Axis.YP.rotationDegrees(defaultRot.y + offsetRotation.y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(defaultRot.z + offsetRotation.z));
            poseStack.translate(-0.225f, -0.75f, 0);
        } else {
            poseStack.translate(-0.225f, 0.75f, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(defaultRot.x + offsetRotation.x));
            poseStack.mulPose(Axis.YP.rotationDegrees(defaultRot.y + offsetRotation.y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(defaultRot.z + offsetRotation.z));
            poseStack.translate(0.225f, -0.75f, 0);
        }
    }

    public static void renderArmBolt(PoseStack poseStack, ModelGun model, AnimStateMachine anim, float smoothing,
                                     Vector3f reloadRot, Vector3f reloadPos, boolean leftHand) {
        float pumpCurrent = 1.0f;
        float pumpLast = 1.0f;
        Optional<StateEntry> currentShootState = anim.getShootState();

        if (currentShootState.isPresent()) {
            if (currentShootState.get().stateType != StateType.Charge && currentShootState.get().stateType != StateType.Uncharge) {
                pumpCurrent = currentShootState.get().currentValue;
                pumpLast = currentShootState.get().lastValue;
            }
        }

        if (anim.isReloadState(StateType.Charge) && !anim.isReloadState(StateType.Uncharge)) {
            StateEntry boltState = anim.getReloadState().get();
            pumpCurrent = boltState.currentValue;
            pumpLast = boltState.lastValue;
        }

        poseStack.translate(reloadPos.x - (1.0f - Math.abs(pumpLast + (pumpCurrent - pumpLast) * smoothing)) * model.config.bolt.chargeModifier.x, 0, 0);
        poseStack.translate(0, reloadPos.y - (1.0f - Math.abs(pumpLast + (pumpCurrent - pumpLast) * smoothing)) * model.config.bolt.chargeModifier.y, 0);
        poseStack.translate(0, 0, reloadPos.z - (1.0f - Math.abs(pumpLast + (pumpCurrent - pumpLast) * smoothing)) * model.config.bolt.chargeModifier.z);

        if (leftHand) {
            handleRotateLeft(poseStack, reloadRot);
        } else {
            handleRotateRight(poseStack, reloadRot);
        }
    }

    public static void renderArmDefault(PoseStack poseStack, ModelGun model, AnimStateMachine anim, float smoothing,
                                        Vector3f reloadRot, Vector3f reloadPos, boolean firingHand, boolean leftHand) {
        float triggerPull = firingHand ? RenderParameters.triggerPullSwitch : 0.0f;

        poseStack.translate(reloadPos.x - triggerPull, reloadPos.y, reloadPos.z);

        if (leftHand) {
            handleRotateLeft(poseStack, reloadRot);
        } else {
            handleRotateRight(poseStack, reloadRot);
        }
    }

    public static void renderArmReload(PoseStack poseStack, ModelGun model, AnimStateMachine anim, WeaponAnimation animation,
                                       float smoothing, float tiltProgress, Vector3f reloadRot, Vector3f reloadPos,
                                       Vector3f defaultRot, Vector3f defaultPos, boolean leftHand) {
        Vector3f offsetPosition = NumberHelper.multiplyVector(NumberHelper.subtractVector(reloadPos, defaultPos), tiltProgress);

        Vector3f ammoLoadOffset = new Vector3f(0, 0, 0);
        Optional<StateEntry> currentState = anim.getReloadState();
        if (anim.isReloadType(ReloadType.Load) && currentState.isPresent() && currentState.get().stateType == StateType.Load && currentState.get().stateType != StateType.Untilt) {
            if (animation.ammoLoadOffset != null) {
                ammoLoadOffset = animation.ammoLoadOffset;
            }
        }

        poseStack.translate(defaultPos.x + offsetPosition.x + ammoLoadOffset.x * tiltProgress, 0, 0);
        poseStack.translate(0, defaultPos.y + offsetPosition.y + ammoLoadOffset.y * tiltProgress, 0);
        poseStack.translate(0, 0, defaultPos.z + offsetPosition.z + ammoLoadOffset.z * tiltProgress);

        Vector3f offsetRotation = NumberHelper.multiplyVector(NumberHelper.subtractVector(reloadRot, defaultRot), tiltProgress);

        if (leftHand) {
            poseStack.translate(0.225f, 0.75f, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(defaultRot.x + offsetRotation.x));
            poseStack.mulPose(Axis.YP.rotationDegrees(defaultRot.y + offsetRotation.y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(defaultRot.z + offsetRotation.z));
            poseStack.translate(-0.225f, -0.75f, 0);
        } else {
            poseStack.translate(-0.225f, 0.75f, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(defaultRot.x + offsetRotation.x));
            poseStack.mulPose(Axis.YP.rotationDegrees(defaultRot.y + offsetRotation.y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(defaultRot.z + offsetRotation.z));
            poseStack.translate(0.225f, -0.75f, 0);
        }
    }

    public static void renderStaticArmReload(PoseStack poseStack, ModelGun model, AnimStateMachine anim, float smoothing,
                                             float tiltProgress, Vector3f reloadRot, Vector3f reloadPos,
                                             Vector3f defaultRot, Vector3f defaultPos, boolean leftHand) {
        Vector3f offsetPosition = NumberHelper.multiplyVector(NumberHelper.subtractVector(reloadPos, defaultPos), tiltProgress);

        poseStack.translate(defaultPos.x + offsetPosition.x, defaultPos.y + offsetPosition.y, defaultPos.z + offsetPosition.z);

        Vector3f offsetRotation = NumberHelper.multiplyVector(NumberHelper.subtractVector(reloadRot, defaultRot), tiltProgress);

        if (leftHand) {
            poseStack.translate(0.225f, 0.75f, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(defaultRot.x + offsetRotation.x));
            poseStack.mulPose(Axis.YP.rotationDegrees(defaultRot.y + offsetRotation.y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(defaultRot.z + offsetRotation.z));
            poseStack.translate(-0.225f, -0.75f, 0);
        } else {
            poseStack.translate(-0.225f, 0.75f, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(defaultRot.x + offsetRotation.x));
            poseStack.mulPose(Axis.YP.rotationDegrees(defaultRot.y + offsetRotation.y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(defaultRot.z + offsetRotation.z));
            poseStack.translate(0.225f, -0.75f, 0);
        }
    }

    public static void renderArmLoad(PoseStack poseStack, ModelGun model, AnimStateMachine anim, WeaponAnimation animation,
                                     float smoothing, float tiltProgress, Vector3f reloadRot, Vector3f reloadPos,
                                     Vector3f defaultRot, Vector3f defaultPos, boolean leftHand) {
        Vector3f offsetPosition = NumberHelper.multiplyVector(NumberHelper.subtractVector(reloadPos, defaultPos), tiltProgress);

        Vector3f ammoLoadOffset = new Vector3f(0, 0, 0);
        Optional<StateEntry> currentState = anim.getReloadState();
        if (anim.isReloadType(ReloadType.Load) && currentState.isPresent() && currentState.get().stateType == StateType.Load) {
            if (animation.ammoLoadOffset != null) {
                ammoLoadOffset = animation.ammoLoadOffset;
            }
        }

        poseStack.translate(defaultPos.x + offsetPosition.x + ammoLoadOffset.x * tiltProgress, 0, 0);
        poseStack.translate(0, defaultPos.y + offsetPosition.y + ammoLoadOffset.y * tiltProgress, 0);
        poseStack.translate(0, 0, defaultPos.z + offsetPosition.z + ammoLoadOffset.z * tiltProgress);

        Vector3f offsetRotation = NumberHelper.multiplyVector(NumberHelper.subtractVector(reloadRot, defaultRot), tiltProgress);

        if (leftHand) {
            poseStack.translate(0.225f, 0.75f, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(defaultRot.x + offsetRotation.x));
            poseStack.mulPose(Axis.YP.rotationDegrees(defaultRot.y + offsetRotation.y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(defaultRot.z + offsetRotation.z));
            poseStack.translate(-0.225f, -0.75f, 0);
        } else {
            poseStack.translate(-0.225f, 0.75f, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(defaultRot.x + offsetRotation.x));
            poseStack.mulPose(Axis.YP.rotationDegrees(defaultRot.y + offsetRotation.y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(defaultRot.z + offsetRotation.z));
            poseStack.translate(0.225f, -0.75f, 0);
        }
    }

    public static void renderArmUnload(PoseStack poseStack, ModelGun model, AnimStateMachine anim, WeaponAnimation animation,
                                       float smoothing, float tiltProgress, Vector3f reloadRot, Vector3f reloadPos,
                                       Vector3f defaultRot, Vector3f defaultPos, boolean leftHand) {
        Vector3f offsetPosition = NumberHelper.multiplyVector(NumberHelper.subtractVector(reloadPos, defaultPos), tiltProgress);

        Vector3f ammoLoadOffset = new Vector3f(0, 0, 0);
        if (anim.isReloadType(ReloadType.Load)) {
            if (animation.ammoLoadOffset != null) {
                ammoLoadOffset = animation.ammoLoadOffset;
            }
        }

        poseStack.translate(defaultPos.x + offsetPosition.x + ammoLoadOffset.x * tiltProgress, 0, 0);
        poseStack.translate(0, defaultPos.y + offsetPosition.y + ammoLoadOffset.y * tiltProgress, 0);
        poseStack.translate(0, 0, defaultPos.z + offsetPosition.z + ammoLoadOffset.z * tiltProgress);

        Vector3f offsetRotation = NumberHelper.multiplyVector(NumberHelper.subtractVector(reloadRot, defaultRot), tiltProgress);

        if (leftHand) {
            poseStack.translate(0.225f, 0.75f, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(defaultRot.x + offsetRotation.x));
            poseStack.mulPose(Axis.YP.rotationDegrees(defaultRot.y + offsetRotation.y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(defaultRot.z + offsetRotation.z));
            poseStack.translate(-0.225f, -0.75f, 0);
        } else {
            poseStack.translate(-0.225f, 0.75f, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(defaultRot.x + offsetRotation.x));
            poseStack.mulPose(Axis.YP.rotationDegrees(defaultRot.y + offsetRotation.y));
            poseStack.mulPose(Axis.ZP.rotationDegrees(defaultRot.z + offsetRotation.z));
            poseStack.translate(0.225f, -0.75f, 0);
        }
    }

    private static void handleRotateLeft(PoseStack poseStack, Vector3f reloadRot) {
        poseStack.translate(0.225f, 0.75f, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(reloadRot.x));
        poseStack.mulPose(Axis.YP.rotationDegrees(reloadRot.y));
        poseStack.mulPose(Axis.ZP.rotationDegrees(reloadRot.z));
        poseStack.translate(-0.225f, -0.75f, 0);
    }

    private static void handleRotateRight(PoseStack poseStack, Vector3f reloadRot) {
        poseStack.translate(-0.225f, 0.75f, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(reloadRot.x));
        poseStack.mulPose(Axis.YP.rotationDegrees(reloadRot.y));
        poseStack.mulPose(Axis.ZP.rotationDegrees(reloadRot.z));
        poseStack.translate(0.225f, -0.75f, 0);
    }
}