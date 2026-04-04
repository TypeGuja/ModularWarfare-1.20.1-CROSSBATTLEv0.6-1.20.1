package com.modularwarfare.loader.part;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class ModelObject {
    public String name;
    public List<Face> faces = new ArrayList<>();
    public int glDrawingMode;

    public ModelObject() {
        this("");
    }

    public ModelObject(String name) {
        this(name, -1);
    }

    public ModelObject(String name, int glDrawingMode) {
        this.name = name;
        this.glDrawingMode = glDrawingMode;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(VertexConsumer consumer, float scale) {
        if (faces.size() > 0) {
            for (Face face : faces) {
                face.render(consumer, scale);
            }
        }
    }
}