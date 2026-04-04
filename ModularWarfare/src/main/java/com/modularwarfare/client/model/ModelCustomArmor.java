package com.modularwarfare.client.model;

import com.modularwarfare.loader.api.ObjModelLoader;
import com.modularwarfare.loader.api.ObjModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.config.ArmorRenderConfig;
import com.modularwarfare.common.type.BaseType;
import com.modularwarfare.loader.MWModelBipedBase;
import net.minecraft.client.model.geom.ModelPart;

public class ModelCustomArmor extends MWModelBipedBase {
    public ArmorRenderConfig config;

    public ModelCustomArmor(ArmorRenderConfig config, BaseType type) {
        this.config = config;
        if (this.config != null && this.config.modelFileName != null && this.config.modelFileName.endsWith(".obj")) {
            if (type.isInDirectory) {
                this.staticModel = ObjModelLoader.load(String.format("%s/obj/%s/%s", type.contentPack, type.getAssetDir(), this.config.modelFileName));
            } else {
                this.staticModel = ObjModelLoader.load(type, String.format("obj/%s/%s", type.getAssetDir(), this.config.modelFileName));
            }
        } else if (this.config != null && this.config.modelFileName != null) {
            ModularWarfare.LOGGER.info(String.format("Internal error: %s is not a valid format.", this.config.modelFileName));
        }
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, String modelPart, ModelPart bodyPart, float scale, float modelScale) {
        poseStack.pushPose();
        if (this.staticModel != null) {
            ObjModelRenderer part = this.staticModel.getPart(modelPart);
            if (part != null) {
                copyModelAngles(bodyPart, part);
                if (modelPart.contains("Leg") || modelPart.contains("Foot")) {
                    copyModelAngles(bodyPart, part);
                } else {
                    if (this.crouching) {
                        part.rotateAngleX = bodyPart.xRot;
                        part.rotateAngleY = bodyPart.yRot;
                        part.rotateAngleZ = bodyPart.zRot;
                        poseStack.translate(0, -0.2f, 0.25f);
                    } else {
                        copyModelAngles(bodyPart, part);
                    }
                }
                if (modelPart.contains("head")) {
                    part.rotateAngleX = bodyPart.xRot;
                    part.rotateAngleY = -bodyPart.yRot;
                    part.rotateAngleZ = -bodyPart.zRot;
                }
                if (modelPart.contains("Arm")) {
                    part.rotateAngleX = bodyPart.xRot;
                    part.rotateAngleY = -bodyPart.yRot;
                    part.rotateAngleZ = bodyPart.zRot;
                }
                part.render(poseStack, consumer, scale);
            }
        }
        poseStack.popPose();
    }

    public void showHead(boolean show) { showGroup("headModel", show); }
    public void showChest(boolean show) {
        showGroup("bodyModel", show);
        showGroup("leftArmModel", show);
        showGroup("rightArmModel", show);
    }
    public void showLegs(boolean show) {
        showGroup("leftLegModel", show);
        showGroup("rightLegModel", show);
    }
    public void showFeet(boolean show) {
        showGroup("leftFootModel", show);
        showGroup("rightFootModel", show);
    }

    private void showGroup(String partName, boolean show) {
        if (this.staticModel != null) {
            ObjModelRenderer part = this.staticModel.getPart(partName);
            if (part != null) {
                part.isHidden = !show;
            }
        }
    }
}