package com.modularwarfare.loader.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
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
    private List<float[]> texCoords;

    public ObjModelRenderer() {
        this.name = "";
        this.vertices = new ArrayList<>();
        this.faces = new ArrayList<>();
        this.texCoords = new ArrayList<>();
    }

    public ObjModelRenderer(String name) {
        this.name = name;
        this.vertices = new ArrayList<>();
        this.faces = new ArrayList<>();
        this.texCoords = new ArrayList<>();
    }

    public ObjModelRenderer(Object model, Object modelObject) {
        this.name = "part";
        this.vertices = new ArrayList<>();
        this.faces = new ArrayList<>();
        this.texCoords = new ArrayList<>();
    }

    public void setVertices(List<Vector3f> vertices) {
        this.vertices = vertices;
    }

    public void setFaces(List<int[]> faces) {
        this.faces = faces;
    }

    public void setTexCoords(List<float[]> texCoords) {
        this.texCoords = texCoords;
    }

    public List<float[]> getTexCoords() {
        return this.texCoords;
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, float scale) {
        if (isHidden) return;
        if (vertices == null || vertices.isEmpty()) return;
        if (faces == null || faces.isEmpty()) return;
        if (consumer == null) return;

        poseStack.pushPose();

        // Apply transformations
        if (rotationPointX != 0 || rotationPointY != 0 || rotationPointZ != 0) {
            poseStack.translate(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);
        }
        if (rotateAngleX != 0) poseStack.mulPose(com.mojang.math.Axis.XP.rotation(rotateAngleX));
        if (rotateAngleY != 0) poseStack.mulPose(com.mojang.math.Axis.YP.rotation(rotateAngleY));
        if (rotateAngleZ != 0) poseStack.mulPose(com.mojang.math.Axis.ZP.rotation(rotateAngleZ));
        if (rotationPointX != 0 || rotationPointY != 0 || rotationPointZ != 0) {
            poseStack.translate(-rotationPointX * scale, -rotationPointY * scale, -rotationPointZ * scale);
        }

        Matrix4f matrix = poseStack.last().pose();

        // Render all faces
        for (int[] face : faces) {
            if (face.length >= 3) {
                renderTriangle(matrix, consumer, face, scale);
            }
        }

        poseStack.popPose();
    }

    private void renderTriangle(Matrix4f matrix, VertexConsumer consumer, int[] face, float scale) {
        Vector3f v1 = vertices.get(face[0]);
        Vector3f v2 = vertices.get(face[1]);
        Vector3f v3 = vertices.get(face[2]);

        // Вычисляем нормаль
        Vector3f normal = calculateNormal(v1, v2, v3);
        normal.normalize();

        float nx = normal.x();
        float ny = normal.y();
        float nz = normal.z();

        // Позиции вершин
        float x1 = v1.x * scale;
        float y1 = v1.y * scale;
        float z1 = v1.z * scale;
        float x2 = v2.x * scale;
        float y2 = v2.y * scale;
        float z2 = v2.z * scale;
        float x3 = v3.x * scale;
        float y3 = v3.y * scale;
        float z3 = v3.z * scale;

        // ПРАВИЛЬНЫЙ ПОРЯДОК ВЫЗОВОВ ДЛЯ MINECRAFT 1.20.1
        consumer.vertex(matrix, x1, y1, z1)
                .color(1.0f, 1.0f, 1.0f, 1.0f)
                .uv(0.0f, 0.0f)
                .overlayCoords(0, 0)
                .uv2(15728880)
                .normal(nx, ny, nz)
                .endVertex();

        consumer.vertex(matrix, x2, y2, z2)
                .color(1.0f, 1.0f, 1.0f, 1.0f)
                .uv(1.0f, 0.0f)
                .overlayCoords(0, 0)
                .uv2(15728880)
                .normal(nx, ny, nz)
                .endVertex();

        consumer.vertex(matrix, x3, y3, z3)
                .color(1.0f, 1.0f, 1.0f, 1.0f)
                .uv(1.0f, 1.0f)
                .overlayCoords(0, 0)
                .uv2(15728880)
                .normal(nx, ny, nz)
                .endVertex();
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

    public String getName() {
        return name;
    }
}