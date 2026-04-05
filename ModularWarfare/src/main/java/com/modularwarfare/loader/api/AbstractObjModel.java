package com.modularwarfare.loader.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import java.util.HashMap;
import java.util.Map;

public class AbstractObjModel {
    protected Map<String, ObjModelRenderer> parts = new HashMap<>();

    public AbstractObjModel() {}

    public ObjModelRenderer getPart(String name) {
        return parts.get(name);
    }

    public void addPart(String name, ObjModelRenderer part) {
        parts.put(name, part);
    }

    public Map<String, ObjModelRenderer> getParts() {
        return parts;
    }

    public void renderAll(PoseStack poseStack, VertexConsumer consumer, float scale) {
        for (ObjModelRenderer part : parts.values()) {
            part.render(poseStack, consumer, scale);
        }
    }
}