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

        ModularWarfare.LOGGER.info("========================================");
        ModularWarfare.LOGGER.info("ModelGun constructor for: " + type.internalName);

        if (config != null && config.modelFileName != null && !config.modelFileName.isEmpty()) {
            String modelPath = config.modelFileName;

            if (!modelPath.endsWith(".obj")) {
                modelPath = modelPath + ".obj";
            }

            String[] pathsToTry = {
                    "assets/modularwarfare/obj/guns/" + modelPath,
                    "obj/guns/" + modelPath,
                    modelPath
            };

            boolean loaded = false;

            for (String tryPath : pathsToTry) {
                ModularWarfare.LOGGER.info("Trying to load: " + tryPath);
                try {
                    this.staticModel = ObjModelLoader.load(tryPath);
                    if (this.staticModel != null && !this.staticModel.getParts().isEmpty()) {
                        ModularWarfare.LOGGER.info("✅ SUCCESS! Loaded from: " + tryPath);
                        ModularWarfare.LOGGER.info("   Parts: " + this.staticModel.getParts().size());
                        for (String partName : this.staticModel.getParts().keySet()) {
                            ModularWarfare.LOGGER.info("     - Part: " + partName);
                        }
                        loaded = true;
                        break;
                    }
                } catch (Exception e) {
                    ModularWarfare.LOGGER.warn("Failed: " + e.getMessage());
                }
            }

            if (!loaded) {
                ModularWarfare.LOGGER.error("❌ Could not load model for " + type.internalName);
            }
        } else {
            ModularWarfare.LOGGER.warn("No modelFileName in config for: " + type.internalName);
        }

        ModularWarfare.LOGGER.info("========================================");
    }

    public void renderPart(PoseStack poseStack, VertexConsumer consumer, String part, float scale) {
        if (staticModel != null) {
            var partRenderer = staticModel.getPart(part);
            if (partRenderer != null) {
                partRenderer.render(poseStack, consumer, scale);
            }
        }
    }

    public boolean hasArms() {
        return config != null && config.arms.leftArm.armPos != null || (config != null && config.arms.rightArm.armPos != null);
    }

    public boolean isType(GunRenderConfig.Arms.EnumArm arm, GunRenderConfig.Arms.EnumAction action) {
        return config != null && config.arms.actionArm == arm && config.arms.actionType == action;
    }
}