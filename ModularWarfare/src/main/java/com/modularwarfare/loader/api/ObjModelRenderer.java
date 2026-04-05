package com.modularwarfare.loader.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.ModularWarfare;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public class ObjModelRenderer {
    public float rotateAngleX = 0;
    public float rotateAngleY = 0;
    public float rotateAngleZ = 0;
    public float rotationPointX = 0;
    public float rotationPointY = 0;
    public float rotationPointZ = 0;
    public boolean isHidden = false;

    private String name;
    private List<Vector3f> vertices;
    private List<int[]> faces;

    public ObjModelRenderer() {
        this.name = "";
    }

    public ObjModelRenderer(String name) {
        this.name = name;
    }

    public ObjModelRenderer(Object model, Object modelObject) {
        this.name = "part";
    }

    public void setVertices(List<Vector3f> vertices) {
        this.vertices = vertices;
    }

    public void setFaces(List<int[]> faces) {
        this.faces = faces;
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, float scale) {
        if (isHidden) return;
        if (vertices == null || vertices.isEmpty()) return;
        if (faces == null || faces.isEmpty()) return;
        if (consumer == null) return;

        poseStack.pushPose();

        // Apply transformations
        poseStack.translate(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);
        if (rotateAngleX != 0) poseStack.mulPose(com.mojang.math.Axis.XP.rotation(rotateAngleX));
        if (rotateAngleY != 0) poseStack.mulPose(com.mojang.math.Axis.YP.rotation(rotateAngleY));
        if (rotateAngleZ != 0) poseStack.mulPose(com.mojang.math.Axis.ZP.rotation(rotateAngleZ));
        poseStack.translate(-rotationPointX * scale, -rotationPointY * scale, -rotationPointZ * scale);

        Matrix4f matrix = poseStack.last().pose();

        // Render all faces as triangles
        for (int[] face : faces) {
            if (face.length >= 3) {
                Vector3f v1 = vertices.get(face[0]);
                Vector3f v2 = vertices.get(face[1]);
                Vector3f v3 = vertices.get(face[2]);

                Vector3f normal = calculateNormal(v1, v2, v3);
                normal.normalize();

                float x1 = v1.x * scale;
                float y1 = v1.y * scale;
                float z1 = v1.z * scale;
                float x2 = v2.x * scale;
                float y2 = v2.y * scale;
                float z2 = v2.z * scale;
                float x3 = v3.x * scale;
                float y3 = v3.y * scale;
                float z3 = v3.z * scale;
                float nx = normal.x();
                float ny = normal.y();
                float nz = normal.z();

                // ВАЖНО: ВСЕ ВЫЗОВЫ В ОДНОЙ ЦЕПОЧКЕ!
                consumer.vertex(matrix, x1, y1, z1).color(1.0f, 1.0f, 1.0f, 1.0f).uv(0.0f, 0.0f).uv2(15728880).normal(nx, ny, nz).endVertex();
                consumer.vertex(matrix, x2, y2, z2).color(1.0f, 1.0f, 1.0f, 1.0f).uv(0.0f, 0.0f).uv2(15728880).normal(nx, ny, nz).endVertex();
                consumer.vertex(matrix, x3, y3, z3).color(1.0f, 1.0f, 1.0f, 1.0f).uv(0.0f, 0.0f).uv2(15728880).normal(nx, ny, nz).endVertex();
            }
        }

        poseStack.popPose();
    }

    private Vector3f calculateNormal(Vector3f v1, Vector3f v2, Vector3f v3) {
        Vector3f a = new Vector3f(v2).sub(v1);
        Vector3f b = new Vector3f(v3).sub(v1);
        Vector3f normal = new Vector3f();
        a.cross(b, normal);
        return normal;
    }

    public void render(PoseStack poseStack) {
        render(poseStack, null, 1.0f);
    }

    public String getName() { return name; }
}