package com.modularwarfare.loader.api;

import com.modularwarfare.loader.ObjModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.loader.part.ModelObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ObjModelRenderer {
    public float rotateAngleX = 0;
    public float rotateAngleY = 0;
    public float rotateAngleZ = 0;
    public float rotationPointX = 0;
    public float rotationPointY = 0;
    public float rotationPointZ = 0;
    public boolean isHidden = false;

    private String name;
    private ModelObject modelObject;
    private ObjModel parentModel;

    public ObjModelRenderer() {
        this.name = "";
    }

    public ObjModelRenderer(String name) {
        this.name = name;
    }

    public ObjModelRenderer(ObjModel model, ModelObject modelObject) {
        this.parentModel = model;
        this.modelObject = modelObject;
        this.name = modelObject.name;
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, float scale) {
        if (isHidden || modelObject == null) return;

        poseStack.pushPose();

        poseStack.translate(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);

        if (rotateAngleX != 0) poseStack.mulPose(com.mojang.math.Axis.XP.rotation(rotateAngleX));
        if (rotateAngleY != 0) poseStack.mulPose(com.mojang.math.Axis.YP.rotation(rotateAngleY));
        if (rotateAngleZ != 0) poseStack.mulPose(com.mojang.math.Axis.ZP.rotation(rotateAngleZ));

        poseStack.translate(-rotationPointX * scale, -rotationPointY * scale, -rotationPointZ * scale);

        modelObject.render(consumer, scale);

        poseStack.popPose();
    }

    public void render(PoseStack poseStack) {
        render(poseStack, null, 1.0f);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModelObject getModelObject() {
        return modelObject;
    }
}