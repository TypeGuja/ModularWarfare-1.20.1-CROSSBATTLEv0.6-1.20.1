package com.modularwarfare.loader.part;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;

public class Face {
    public Vertex[] vertices;
    public Vertex[] vertexNormals;
    public Vertex faceNormal;
    public TextureCoordinate[] textureCoordinates;

    public void render(VertexConsumer consumer, float scale) {
        if (vertices == null) return;

        boolean hasTexture = textureCoordinates != null && textureCoordinates.length > 0;

        if (faceNormal == null) {
            faceNormal = calculateFaceNormal();
        }

        for (int i = 0; i < vertices.length; i++) {
            float x = vertices[i].x * scale;
            float y = vertices[i].y * scale;
            float z = vertices[i].z * scale;
            float nx = faceNormal.x;
            float ny = faceNormal.y;
            float nz = faceNormal.z;

            if (hasTexture && textureCoordinates[i] != null) {
                float u = textureCoordinates[i].u;
                float v = textureCoordinates[i].v;
                consumer.vertex(x, y, z).uv(u, v).normal(nx, ny, nz).endVertex();
            } else {
                consumer.vertex(x, y, z).normal(nx, ny, nz).endVertex();
            }
        }
    }

    public Vertex calculateFaceNormal() {
        if (vertices == null || vertices.length < 3) return new Vertex(0, 0, 0);

        Vec3 v1 = new Vec3(
                vertices[1].x - vertices[0].x,
                vertices[1].y - vertices[0].y,
                vertices[1].z - vertices[0].z
        );
        Vec3 v2 = new Vec3(
                vertices[2].x - vertices[0].x,
                vertices[2].y - vertices[0].y,
                vertices[2].z - vertices[0].z
        );
        Vec3 normalVector = v1.cross(v2).normalize();
        return new Vertex((float) normalVector.x, (float) normalVector.y, (float) normalVector.z);
    }
}