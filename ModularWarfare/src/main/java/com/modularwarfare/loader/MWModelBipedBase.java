package com.modularwarfare.loader;

import com.modularwarfare.loader.api.AbstractObjModel;
import com.modularwarfare.loader.api.ObjModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MWModelBipedBase extends HumanoidModel {
    public AbstractObjModel staticModel;

    public MWModelBipedBase() {
        super(createModelPart());
    }

    public MWModelBipedBase(AbstractObjModel model) {
        super(createModelPart());
        this.staticModel = model;
    }

    private static ModelPart createModelPart() {
        return new ModelPart(new java.util.ArrayList<>(), new java.util.HashMap<>());
    }

    public AbstractObjModel getStaticModel() {
        return this.staticModel;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(ObjModelRenderer model, PoseStack poseStack, VertexConsumer consumer, float scale) {
        poseStack.pushPose();
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0f));
        if (model != null) {
            model.render(poseStack, consumer, scale);
        }
        poseStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public void renderAll(PoseStack poseStack, VertexConsumer consumer, float scale) {
        poseStack.pushPose();
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0f));
        if (staticModel != null) {
            // Исправлено: передаём PoseStack, VertexConsumer и scale
            staticModel.renderAll(poseStack, consumer, scale);
        }
        poseStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public void renderPart(PoseStack poseStack, VertexConsumer consumer, String part, float scale) {
        if (staticModel != null && staticModel.getPart(part) != null) {
            render(staticModel.getPart(part), poseStack, consumer, scale);
        }
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