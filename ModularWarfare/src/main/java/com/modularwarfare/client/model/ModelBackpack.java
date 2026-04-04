package com.modularwarfare.client.model;

import com.modularwarfare.loader.api.ObjModelLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.config.BackpackRenderConfig;
import com.modularwarfare.common.type.BaseType;
import com.modularwarfare.loader.MWModelBase;

public class ModelBackpack extends MWModelBase {
    public BackpackRenderConfig config;

    public ModelBackpack(BackpackRenderConfig config, BaseType type) {
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

    public void render(PoseStack poseStack, VertexConsumer consumer, String modelPart, float scale, float modelScale) {
        poseStack.pushPose();
        if (this.staticModel != null) {
            var part = this.staticModel.getPart(modelPart);
            if (part != null) {
                part.render(poseStack, consumer, scale * modelScale);
            }
        }
        poseStack.popPose();
    }
}