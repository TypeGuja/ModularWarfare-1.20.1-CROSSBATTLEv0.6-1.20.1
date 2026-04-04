package com.modularwarfare.client.model.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.common.entity.decals.EntityBulletHole;
import com.modularwarfare.common.entity.decals.EntityDecal;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RenderDecal extends EntityRenderer<EntityDecal> {

    public RenderDecal(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityDecal entity, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int light) {
        float transparency = 0.75f;
        if (!entity.isPermanent()) {
            transparency = entity.getAgeRatio() * 0.85f;
        }
        if (entity instanceof EntityBulletHole) {
            transparency = entity.getAgeRatio() * 1.0f;
        }

        switch (entity.getSideID()) {
            case 0:
                renderDecalFloor(entity, poseStack, buffer, transparency);
                break;
            case 1:
                renderDecalNorth(entity, poseStack, buffer, transparency);
                renderDecalEast(entity, poseStack, buffer, transparency);
                renderDecalSouth(entity, poseStack, buffer, transparency);
                renderDecalWest(entity, poseStack, buffer, transparency);
                break;
            case 2:
                renderDecalFloor(entity, poseStack, buffer, transparency);
                break;
            case 3:
                renderDecalNorth(entity, poseStack, buffer, transparency);
                break;
            case 4:
                renderDecalEast(entity, poseStack, buffer, transparency);
                break;
            case 5:
                renderDecalSouth(entity, poseStack, buffer, transparency);
                break;
            case 6:
                renderDecalWest(entity, poseStack, buffer, transparency);
                break;
        }
    }

    private void renderDecalFloor(EntityDecal entity, PoseStack poseStack, MultiBufferSource buffer, float alpha) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(entity.getDecalTexture()));
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        AABB renderBox = new AABB(x - 0.5, y - 0.01, z - 0.5, x + 0.5, y + 0.01, z + 0.5);

        poseStack.pushPose();
        poseStack.translate(renderBox.minX, renderBox.minY, renderBox.minZ);

        var matrix = poseStack.last().pose();

        // Render quad for floor decal
        consumer.vertex(matrix, 0, 0, 0).uv(0, 0).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 1, 0, 0).uv(1, 0).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 1, 0, 1).uv(1, 1).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 0, 0, 1).uv(0, 1).color(1, 1, 1, alpha).endVertex();

        poseStack.popPose();
    }

    private void renderDecalNorth(EntityDecal entity, PoseStack poseStack, MultiBufferSource buffer, float alpha) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(entity.getDecalTexture()));
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        AABB renderBox = new AABB(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z - 0.49);

        poseStack.pushPose();
        poseStack.translate(renderBox.minX, renderBox.minY, renderBox.minZ);

        var matrix = poseStack.last().pose();

        consumer.vertex(matrix, 0, 0, 0).uv(0, 0).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 1, 0, 0).uv(1, 0).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 1, 1, 0).uv(1, 1).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 0, 1, 0).uv(0, 1).color(1, 1, 1, alpha).endVertex();

        poseStack.popPose();
    }

    private void renderDecalEast(EntityDecal entity, PoseStack poseStack, MultiBufferSource buffer, float alpha) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(entity.getDecalTexture()));
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        AABB renderBox = new AABB(x + 0.49, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5);

        poseStack.pushPose();
        poseStack.translate(renderBox.minX, renderBox.minY, renderBox.minZ);

        var matrix = poseStack.last().pose();

        consumer.vertex(matrix, 0, 0, 0).uv(0, 0).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 0, 1, 0).uv(0, 1).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 1, 1, 0).uv(1, 1).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 1, 0, 0).uv(1, 0).color(1, 1, 1, alpha).endVertex();

        poseStack.popPose();
    }

    private void renderDecalSouth(EntityDecal entity, PoseStack poseStack, MultiBufferSource buffer, float alpha) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(entity.getDecalTexture()));
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        AABB renderBox = new AABB(x - 0.5, y - 0.5, z + 0.49, x + 0.5, y + 0.5, z + 0.5);

        poseStack.pushPose();
        poseStack.translate(renderBox.minX, renderBox.minY, renderBox.minZ);

        var matrix = poseStack.last().pose();

        consumer.vertex(matrix, 0, 0, 0).uv(0, 0).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 1, 0, 0).uv(1, 0).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 1, 1, 0).uv(1, 1).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 0, 1, 0).uv(0, 1).color(1, 1, 1, alpha).endVertex();

        poseStack.popPose();
    }

    private void renderDecalWest(EntityDecal entity, PoseStack poseStack, MultiBufferSource buffer, float alpha) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(entity.getDecalTexture()));
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        AABB renderBox = new AABB(x - 0.5, y - 0.5, z - 0.5, x - 0.49, y + 0.5, z + 0.5);

        poseStack.pushPose();
        poseStack.translate(renderBox.minX, renderBox.minY, renderBox.minZ);

        var matrix = poseStack.last().pose();

        consumer.vertex(matrix, 0, 0, 0).uv(0, 0).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 1, 0, 0).uv(1, 0).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 1, 1, 0).uv(1, 1).color(1, 1, 1, alpha).endVertex();
        consumer.vertex(matrix, 0, 1, 0).uv(0, 1).color(1, 1, 1, alpha).endVertex();

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDecal entity) {
        return entity.getDecalTexture();
    }

    @Override
    public boolean shouldRender(EntityDecal entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}