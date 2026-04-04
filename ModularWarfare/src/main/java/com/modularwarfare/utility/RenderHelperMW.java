package com.modularwarfare.utility;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class RenderHelperMW {

    public static void renderCenteredText(String text, int x, int y, int color) {
        Font font = Minecraft.getInstance().font;
        int width = font.width(text);
        // Создаем временный GuiGraphics для рендера
        PoseStack poseStack = new PoseStack();
        poseStack.translate(x - width / 2, y, 0);
        font.drawInBatch(text, 0, 0, color, false, poseStack.last().pose(),
                Minecraft.getInstance().renderBuffers().bufferSource(),
                Font.DisplayMode.NORMAL, 0, 15728880);
    }

    public static void renderCenteredTextWithShadow(String text, int x, int y, int color) {
        Font font = Minecraft.getInstance().font;
        int width = font.width(text);
        PoseStack poseStack = new PoseStack();
        poseStack.translate(x - width / 2, y, 0);
        font.drawInBatch(text, 0, 0, color, true, poseStack.last().pose(),
                Minecraft.getInstance().renderBuffers().bufferSource(),
                Font.DisplayMode.NORMAL, 0, 15728880);
    }

    public static void renderCenteredText(GuiGraphics guiGraphics, String text, int x, int y, int color) {
        Font font = Minecraft.getInstance().font;
        int width = font.width(text);
        guiGraphics.drawString(font, text, x - width / 2, y, color);
    }

    public static void renderCenteredTextWithShadow(GuiGraphics guiGraphics, String text, int x, int y, int color) {
        Font font = Minecraft.getInstance().font;
        int width = font.width(text);
        guiGraphics.drawString(font, text, x - width / 2, y, color);
    }

    public static void renderCenteredComponent(GuiGraphics guiGraphics, Component text, int x, int y, int color) {
        Font font = Minecraft.getInstance().font;
        int width = font.width(text);
        guiGraphics.drawString(font, text.getString(), x - width / 2, y, color);
    }

    @Deprecated
    public static void drawString(PoseStack poseStack, String text, int x, int y, int color) {
        Font font = Minecraft.getInstance().font;
        font.drawInBatch(text, x, y, color, false, poseStack.last().pose(),
                Minecraft.getInstance().renderBuffers().bufferSource(),
                Font.DisplayMode.NORMAL, 0, 15728880);
    }
}