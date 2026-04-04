package com.modularwarfare.client.model;

import com.modularwarfare.loader.api.ObjModelLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.config.AttachmentRenderConfig;
import com.modularwarfare.common.type.BaseType;
import com.modularwarfare.loader.MWModelBase;

public class ModelAttachment extends MWModelBase {
    public AttachmentRenderConfig config;
    public float renderOffset = 0.0f;

    public ModelAttachment(AttachmentRenderConfig config, BaseType type) {
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

    public void renderAttachment(PoseStack poseStack, VertexConsumer consumer, float scale) {
        renderPart(poseStack, consumer, "attachmentModel", scale);
    }

    public void renderScope(PoseStack poseStack, VertexConsumer consumer, float scale) {
        renderPart(poseStack, consumer, "scopeModel", scale);
    }

    public void renderOverlay(PoseStack poseStack, VertexConsumer consumer, float scale) {
        renderPart(poseStack, consumer, "overlayModel", scale);
    }
}