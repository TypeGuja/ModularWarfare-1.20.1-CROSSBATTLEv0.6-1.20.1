package com.modularwarfare.client.model;

import com.modularwarfare.client.config.BodyRenderConfig;
import com.modularwarfare.common.type.BaseType;
import com.modularwarfare.loader.MWModelBase;
import com.modularwarfare.loader.api.ObjModelLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelBody extends MWModelBase {
    public BodyRenderConfig config;

    public ModelBody(BodyRenderConfig config, BaseType type) {
        this.config = config;
        if (config != null && config.modelFileName != null && config.modelFileName.endsWith(".obj")) {
            if (type.isInDirectory) {
                this.staticModel = ObjModelLoader.load(String.format("%s/obj/%s/%s", type.contentPack, type.getAssetDir(), config.modelFileName));
            } else {
                this.staticModel = ObjModelLoader.load(type, String.format("obj/%s/%s", type.getAssetDir(), config.modelFileName));
            }
        }
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, String modelPart, float scale, float modelScale) {
        poseStack.pushPose();
        if (staticModel != null) {
            var part = staticModel.getPart(modelPart);
            if (part != null) {
                part.render(poseStack, consumer, scale * modelScale);
            }
        }
        poseStack.popPose();
    }
}