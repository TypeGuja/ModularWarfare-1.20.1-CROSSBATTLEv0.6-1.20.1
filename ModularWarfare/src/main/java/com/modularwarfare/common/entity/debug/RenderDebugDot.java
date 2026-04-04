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

public class RenderDebugDot extends EntityRenderer<EntityDebugDot> {

    public RenderDebugDot(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityDebugDot entity, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int light) {
        if (!ModularWarfare.DEV_ENV) return;

        VertexConsumer consumer = buffer.getBuffer(RenderType.lines());
        Matrix4f pose = poseStack.last().pose();

        float r = entity.getColorRed();
        float g = entity.getColorGreen();
        float b = entity.getColorBlue();

        consumer.vertex(pose, 0, 0, 0).color(r, g, b, 1.0f).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDebugDot entity) {
        return null;
    }

    @Override
    public boolean shouldRender(EntityDebugDot entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}