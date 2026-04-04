package com.modularwarfare.loader;

import com.modularwarfare.loader.api.AbstractObjModel;
import com.modularwarfare.loader.api.ObjModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;

public class MWModelBase {
    public AbstractObjModel staticModel;

    public MWModelBase() {}

    public MWModelBase(AbstractObjModel model) {
        this.staticModel = model;
    }

    public AbstractObjModel getStaticModel() {
        return staticModel;
    }

    public void renderPart(PoseStack poseStack, VertexConsumer consumer, String part, float scale) {
        if (staticModel != null && staticModel.getPart(part) != null) {
            render(poseStack, consumer, staticModel.getPart(part), scale);
        }
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, ObjModelRenderer model, float scale) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        if (model != null) {
            model.render(poseStack, consumer, scale);
        }
        poseStack.popPose();
    }

    public void renderAll(PoseStack poseStack, VertexConsumer consumer, float scale) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        if (staticModel != null) {
            // Исправлено: передаём PoseStack и другие параметры
            staticModel.renderAll(poseStack, consumer, scale);
        }
        poseStack.popPose();
    }

    public static void copyModelAngles(ObjModelRenderer source, ObjModelRenderer dest) {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
        dest.rotationPointX = source.rotationPointX;
        dest.rotationPointY = source.rotationPointY;
        dest.rotationPointZ = source.rotationPointZ;
    }

    public static void copyModelAngles(ObjModelRenderer source, ModelPart dest) {
        dest.xRot = source.rotateAngleX;
        dest.yRot = source.rotateAngleY;
        dest.zRot = source.rotateAngleZ;
        dest.x = source.rotationPointX;
        dest.y = source.rotationPointY;
        dest.z = source.rotationPointZ;
    }

    public static void copyModelAngles(ModelPart source, ObjModelRenderer dest) {
        dest.rotateAngleX = source.xRot;
        dest.rotateAngleY = source.yRot;
        dest.rotateAngleZ = source.zRot;
        dest.rotationPointX = source.x;
        dest.rotationPointY = source.y;
        dest.rotationPointZ = source.z;
    }
}