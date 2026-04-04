package com.modularwarfare.client.model;

import com.modularwarfare.loader.api.ObjModelLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.config.AmmoRenderConfig;
import com.modularwarfare.client.model.objects.RenderVariables;
import com.modularwarfare.common.type.BaseType;
import com.modularwarfare.loader.MWModelBase;
import org.joml.Vector3f;

import java.util.HashMap;

public class ModelAmmo extends MWModelBase {
    public AmmoRenderConfig config;
    public Vector3f thirdPersonOffset = new Vector3f();
    public HashMap<Integer, RenderVariables> magCountOffset = new HashMap<>();

    public ModelAmmo(AmmoRenderConfig config, BaseType type) {
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

    public void renderAmmo(PoseStack poseStack, VertexConsumer consumer, float scale) {
        renderPart(poseStack, consumer, "ammoModel", scale);
    }
}