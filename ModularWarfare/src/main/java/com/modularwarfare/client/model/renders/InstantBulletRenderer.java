package com.modularwarfare.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;

public class InstantBulletRenderer {
    private static ArrayList<InstantShotTrail> trails = new ArrayList<>();

    public static void addTrail(InstantShotTrail trail) {
        trails.add(trail);
    }

    public static void renderAllTrails(PoseStack poseStack, MultiBufferSource buffer, float partialTicks) {
        Iterator<InstantShotTrail> iterator = trails.iterator();
        while (iterator.hasNext()) {
            InstantShotTrail trail = iterator.next();
            trail.render(poseStack, buffer, partialTicks);
        }
    }

    public static void updateAllTrails() {
        for (int i = trails.size() - 1; i >= 0; i--) {
            if (trails.get(i).update()) {
                trails.remove(i);
            }
        }
    }

    public static class InstantShotTrail {
        private Vec3 origin;
        private Vec3 hitPos;
        private float width;
        private float length;
        private float distanceToTarget;
        private float bulletSpeed;
        private int ticksExisted = 0;
        private ResourceLocation texture;

        public InstantShotTrail(Vec3 origin, Vec3 hitPos, float bulletSpeed, boolean isPunched) {
            this.bulletSpeed = bulletSpeed;
            this.origin = origin;
            this.hitPos = hitPos;
            this.length = 10.0f;

            if (!isPunched) {
                this.texture = new ResourceLocation("modularwarfare", "textures/skins/defaultbullettrail.png");
                this.width = 0.05f;
            } else {
                this.texture = new ResourceLocation("modularwarfare", "textures/skins/punchedbullettrail.png");
                this.width = 0.1f;
            }

            Vec3 dPos = hitPos.subtract(origin);
            this.distanceToTarget = (float) dPos.length();
            if (Math.abs(this.distanceToTarget) > 300.0f) {
                this.distanceToTarget = 300.0f;
            }
        }

        public boolean update() {
            this.ticksExisted++;
            return (float) this.ticksExisted * this.bulletSpeed >= this.distanceToTarget - this.length;
        }

        public void render(PoseStack poseStack, MultiBufferSource buffer, float partialTicks) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));

            var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            double x = camera.getPosition().x;
            double y = camera.getPosition().y;
            double z = camera.getPosition().z;

            poseStack.pushPose();
            poseStack.translate(-x, -y, -z);

            float parametric = (this.ticksExisted + partialTicks) * this.bulletSpeed;

            Vec3 dPos = this.hitPos.subtract(this.origin);
            dPos = dPos.normalize();

            float startParametric = parametric - this.length * 0.5f;
            Vec3 startPos = new Vec3(
                    this.origin.x + dPos.x * startParametric,
                    this.origin.y + dPos.y * startParametric,
                    this.origin.z + dPos.z * startParametric);

            float endParametric = parametric + this.length * 0.5f;
            Vec3 endPos = new Vec3(
                    this.origin.x + dPos.x * endParametric,
                    this.origin.y + dPos.y * endParametric,
                    this.origin.z + dPos.z * endParametric);

            dPos = dPos.normalize();

            var player = Minecraft.getInstance().player;
            Vec3 vectorToPlayer = new Vec3(
                    player.getX() - this.hitPos.x,
                    player.getY() - this.hitPos.y,
                    player.getZ() - this.hitPos.z).normalize();

            Vec3 trailTangent = dPos.cross(vectorToPlayer).normalize();
            trailTangent = trailTangent.scale(-this.width * 0.5);

            Vec3 normal = trailTangent.cross(dPos).normalize();

            Matrix4f matrix = poseStack.last().pose();

            consumer.vertex(matrix, (float) startPos.x + (float) trailTangent.x, (float) startPos.y + (float) trailTangent.y, (float) startPos.z + (float) trailTangent.z).uv(0, 0).endVertex();
            consumer.vertex(matrix, (float) startPos.x - (float) trailTangent.x, (float) startPos.y - (float) trailTangent.y, (float) startPos.z - (float) trailTangent.z).uv(0, 1).endVertex();
            consumer.vertex(matrix, (float) endPos.x - (float) trailTangent.x, (float) endPos.y - (float) trailTangent.y, (float) endPos.z - (float) trailTangent.z).uv(1, 1).endVertex();
            consumer.vertex(matrix, (float) endPos.x + (float) trailTangent.x, (float) endPos.y + (float) trailTangent.y, (float) endPos.z + (float) trailTangent.z).uv(1, 0).endVertex();

            poseStack.popPose();
        }
    }
}