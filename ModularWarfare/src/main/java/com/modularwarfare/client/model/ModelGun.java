package com.modularwarfare.client.model;

import com.modularwarfare.loader.api.ObjModelLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.config.GunRenderConfig;
import com.modularwarfare.client.model.objects.BreakActionData;
import com.modularwarfare.common.type.BaseType;
import com.modularwarfare.loader.MWModelBase;
import org.joml.Vector3f;

import java.util.ArrayList;

public class ModelGun extends MWModelBase {
    public GunRenderConfig config;
    public boolean switchIsOnSlide = false;
    public Vector3f switchRotationPoint = new Vector3f(0.0f, 0.0f, 0.0f);
    public float switchSemiRot;
    public float switchBurstRot;
    public float switchAutoRot;
    public boolean scopeIsOnSlide = false;
    public boolean scopeIsOnBreakAction = false;
    public int hammerDelay = 0;
    public float triggerRotation = 0.0f;
    public Vector3f triggerRotationPoint = new Vector3f();
    public float triggerDistance = 0.02f;
    public float leverRotation = 0.0f;
    public Vector3f leverRotationPoint = new Vector3f();
    public float cylinderRotation = 0.0f;
    public Vector3f cylinderRotationPoint = new Vector3f();
    public Vector3f hammerRotationPoint = new Vector3f();
    public float hammerAngle = 75.0f;
    public boolean slideLockOnEmpty = true;
    public ArrayList<BreakActionData> breakActions = new ArrayList<>();

    public ModelGun(GunRenderConfig config, BaseType type) {
        this.config = config;
        if (config != null && config.modelFileName != null && config.modelFileName.endsWith(".obj")) {
            try {
                if (type.isInDirectory) {
                    this.staticModel = ObjModelLoader.load(String.format("%s/obj/%s/%s", type.contentPack, type.getAssetDir(), config.modelFileName));
                } else {
                    this.staticModel = ObjModelLoader.load(type, String.format("obj/%s/%s", type.getAssetDir(), config.modelFileName));
                }
            } catch (Exception e) {
                ModularWarfare.LOGGER.error("Failed to load model: " + config.modelFileName, e);
            }
        } else if (config != null && config.modelFileName != null) {
            ModularWarfare.LOGGER.info(String.format("Internal error: %s is not a valid format.", config.modelFileName));
        }
    }

    public void renderPart(PoseStack poseStack, VertexConsumer consumer, String part, float scale) {
        if (staticModel != null && staticModel.getPart(part) != null) {
            staticModel.getPart(part).render(poseStack, consumer, scale);
        }
    }

    public boolean hasArms() {
        return config != null && config.arms.leftArm.armPos != null || (config != null && config.arms.rightArm.armPos != null);
    }

    public boolean isType(GunRenderConfig.Arms.EnumArm arm, GunRenderConfig.Arms.EnumAction action) {
        return config != null && config.arms.actionArm == arm && config.arms.actionType == action;
    }
}