package com.modularwarfare.common.entity.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.ModularWarfare;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderDebugVector extends EntityRenderer<EntityDebugVector> {

    public RenderDebugVector(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityDebugVector entity, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int light) {
        if (ModularWarfare.DEV_ENV) return;

        VertexConsumer consumer = buffer.getBuffer(RenderType.LINES);
        Matrix4f pose = poseStack.last().pose();

        float r = entity.getColorRed();
        float g = entity.getColorGreen();
        float b = entity.getColorBlue();
        float px = entity.getPointingX();
        float py = entity.getPointingY();
        float pz = entity.getPointingZ();

        consumer.vertex(pose, 0, 0, 0).color(r, g, b, 1.0f).endVertex();
        consumer.vertex(pose, px, py, pz).color(r, g, b, 1.0f).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDebugVector entity) {
        return null;
    }

    @Override
    public boolean shouldRender(EntityDebugVector entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}