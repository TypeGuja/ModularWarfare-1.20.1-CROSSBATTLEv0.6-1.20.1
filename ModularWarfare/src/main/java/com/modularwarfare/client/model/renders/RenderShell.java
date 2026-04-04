package com.modularwarfare.client.model.renders;

import com.modularwarfare.loader.api.ObjModelLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.entity.decals.EntityShell;
import com.modularwarfare.loader.MWModelBase;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderShell extends EntityRenderer<EntityShell> {
    public static MWModelBase normalShell;
    public static MWModelBase gaugeShell;

    static {
        try {
            normalShell = new MWModelBase(ObjModelLoader.load("modularwarfare:obj/shell.obj"));
            gaugeShell = new MWModelBase(ObjModelLoader.load("modularwarfare:obj/12gauge.obj"));
        } catch (Exception e) {
            normalShell = new MWModelBase();
            gaugeShell = new MWModelBase();
        }
    }

    public RenderShell(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityShell entity, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int light) {
        if (entity == null) return;

        String weaponType = entity.getWeaponType();
        if (weaponType != null && weaponType.equals("shotgun") && gaugeShell != null && gaugeShell.getStaticModel() != null) {
            ResourceLocation texture = ResourceLocation.tryBuild(ModularWarfare.MOD_ID, "textures/skins/12gauge.png");
            if (texture == null) texture = new ResourceLocation(ModularWarfare.MOD_ID, "textures/skins/12gauge.png");
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(texture));
            poseStack.pushPose();
            poseStack.translate(entity.getX(), entity.getY() + 0.02, entity.getZ());
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTick));
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-entity.getXRot()));
            poseStack.scale(0.0625f, 0.0625f, 0.0625f);
            gaugeShell.renderAll(poseStack, consumer, 0.0625f);
            poseStack.popPose();
        } else if (normalShell != null && normalShell.getStaticModel() != null) {
            ResourceLocation texture = ResourceLocation.tryBuild(ModularWarfare.MOD_ID, "textures/skins/shell.png");
            if (texture == null) texture = new ResourceLocation(ModularWarfare.MOD_ID, "textures/skins/shell.png");
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(texture));
            poseStack.pushPose();
            poseStack.translate(entity.getX(), entity.getY(), entity.getZ());
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTick));
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-entity.getXRot()));
            poseStack.scale(0.0625f, 0.0625f, 0.0625f);
            normalShell.renderAll(poseStack, consumer, 0.0625f);
            poseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EntityShell entity) {
        return ResourceLocation.tryBuild(ModularWarfare.MOD_ID, "textures/skins/shell.png");
    }

    @Override
    public boolean shouldRender(EntityShell entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}
