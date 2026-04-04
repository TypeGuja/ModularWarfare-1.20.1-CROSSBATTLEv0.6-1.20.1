package com.modularwarfare.loader.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractObjModel {
    protected Map<String, ObjModelRenderer> parts = new HashMap<>();
    protected ResourceLocation texture;

    public AbstractObjModel() {}

    public AbstractObjModel(ResourceLocation texture) {
        this.texture = texture;
    }

    public ObjModelRenderer getPart(String name) {
        return parts.get(name);
    }

    public void addPart(String name, ObjModelRenderer part) {
        parts.put(name, part);
    }

    public List<ObjModelRenderer> getParts() {
        return parts.values().stream().toList();
    }

    public void renderAll(PoseStack poseStack, VertexConsumer consumer, float scale) {
        for (ObjModelRenderer part : parts.values()) {
            part.render(poseStack, consumer, scale);
        }
    }

    public void renderAll(PoseStack poseStack) {
        for (ObjModelRenderer part : parts.values()) {
            part.render(poseStack);
        }
    }

    public ResourceLocation getTexture() {
        return texture;
    }
}