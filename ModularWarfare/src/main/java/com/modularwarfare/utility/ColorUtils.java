package com.modularwarfare.utility;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;

public class ColorUtils {

    public static Vector4f toFloat(Color color) {
        float[] rgba = color.getRGBComponents(null);
        return new Vector4f(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    public static Vector3f toFloat(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return new Vector3f(r / 255.0f, g / 255.0f, b / 255.0f);
    }

    public static Vector4f toFloat4(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return new Vector4f(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f);
    }

    public static int[] toRGB(int rgb) {
        return new int[]{(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
    }

    public static int getRGB(Color color) {
        if (color == null) return 0;
        return getRGB(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static int getRGBA(Color color) {
        if (color == null) return 0;
        return getRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getARGB(Color color) {
        if (color == null) return 0;
        return getARGB(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getRGB(Vector3f rgb) {
        return getRGB(rgb.x, rgb.y, rgb.z);
    }

    public static int getRGB(float r, float g, float b) {
        return getRGB((int) (r * 255), (int) (g * 255), (int) (b * 255));
    }

    public static int getRGBA(Vector4f col) {
        return getRGBA(col.x, col.y, col.z, col.w);
    }

    public static int getRGBA(float r, float g, float b, float a) {
        return getRGBA((int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255));
    }

    public static int getARGB(float r, float g, float b, float a) {
        return getARGB((int) (a * 255), (int) (r * 255), (int) (g * 255), (int) (b * 255));
    }

    public static int getRGB(int r, int g, int b) {
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public static int getARGB(int r, int g, int b, int a) {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public static int getRGBA(int r, int g, int b, int a) {
        return (r & 0xFF) << 24 | (g & 0xFF) << 16 | (b & 0xFF) << 8 | (a & 0xFF);
    }

    public static void setGLColorFromInt(int color) {
        float red = ((color >> 16) & 0xFF) / 255.0f;
        float green = ((color >> 8) & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;
        RenderSystem.setShaderColor(red, green, blue, 1.0f);
    }

    public static int toHex(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }
}