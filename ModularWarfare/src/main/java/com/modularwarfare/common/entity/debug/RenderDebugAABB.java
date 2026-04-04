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
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class RenderDebugAABB extends EntityRenderer<EntityDebugAABB> {

    public RenderDebugAABB(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityDebugAABB entity, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int light) {
        if (ModularWarfare.DEV_ENV) return;

        VertexConsumer consumer = buffer.getBuffer(RenderType.LINES);
        Matrix4f pose = poseStack.last().pose();

        float r = entity.red;
        float g = entity.green;
        float b = entity.blue;

        AABB box = new AABB(entity.offset.x, entity.offset.y, entity.offset.z,
                entity.offset.x + entity.vector.x, entity.offset.y + entity.vector.y, entity.offset.z + entity.vector.z);

        // Draw box edges
        drawBox(pose, consumer, box, r, g, b, 0.5f);
    }

    private void drawBox(Matrix4f pose, VertexConsumer consumer, AABB box, float r, float g, float b, float a) {
        // Bottom face
        consumer.vertex(pose, (float) box.minX, (float) box.minY, (float) box.minZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.maxX, (float) box.minY, (float) box.minZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.maxX, (float) box.minY, (float) box.minZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.maxX, (float) box.minY, (float) box.maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.maxX, (float) box.minY, (float) box.maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.minX, (float) box.minY, (float) box.maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.minX, (float) box.minY, (float) box.maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.minX, (float) box.minY, (float) box.minZ).color(r, g, b, a).endVertex();

        // Top face
        consumer.vertex(pose, (float) box.minX, (float) box.maxY, (float) box.minZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.maxX, (float) box.maxY, (float) box.minZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.maxX, (float) box.maxY, (float) box.minZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.maxX, (float) box.maxY, (float) box.maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.maxX, (float) box.maxY, (float) box.maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.minX, (float) box.maxY, (float) box.maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.minX, (float) box.maxY, (float) box.maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.minX, (float) box.maxY, (float) box.minZ).color(r, g, b, a).endVertex();

        // Vertical edges
        consumer.vertex(pose, (float) box.minX, (float) box.minY, (float) box.minZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.minX, (float) box.maxY, (float) box.minZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.maxX, (float) box.minY, (float) box.minZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.maxX, (float) box.maxY, (float) box.minZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.maxX, (float) box.minY, (float) box.maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.maxX, (float) box.maxY, (float) box.maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.minX, (float) box.minY, (float) box.maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(pose, (float) box.minX, (float) box.maxY, (float) box.maxZ).color(r, g, b, a).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDebugAABB entity) {
        return null;
    }

    @Override
    public boolean shouldRender(EntityDebugAABB entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}