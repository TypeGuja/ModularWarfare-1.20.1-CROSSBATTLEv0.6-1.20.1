package com.modularwarfare.client.model.objects;

import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.model.renders.RenderParameters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public abstract class CustomItemRenderer {
    protected static final Minecraft mc = Minecraft.getInstance();
    private HashMap<String, ResourceLocation> cachedSkins = new HashMap<>();

    public abstract void renderItem(CustomItemRenderType type, ItemStack item, Object... data);

    public void bindTexture(String type, String fileName) {
        String pathFormat = "skins/%s/%s.png";
        try {
            ResourceLocation location = new ResourceLocation("modularwarfare", String.format(pathFormat, type, fileName));
            if (cachedSkins.containsKey(type + "_" + fileName)) {
                mc.getTextureManager().bindForSetup(cachedSkins.get(type + "_" + fileName));
                return;
            }
            if (mc.getTextureManager().getTexture(location) == null) {
                AbstractTexture texture = new SimpleTexture(location);
                texture.load(mc.getResourceManager());
            }
            mc.getTextureManager().bindForSetup(location);
        } catch (Exception e) {
            ResourceLocation fallback = new ResourceLocation("modularwarfare", String.format(pathFormat, "default", type, fileName));
            cachedSkins.put(type + "_" + fileName, fallback);
            e.printStackTrace();
        }
    }
}