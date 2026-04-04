package com.modularwarfare.client.scope;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;

// Временно отключаем наследование от LevelRenderer из-за изменений в конструкторе
public class ScopeRenderGlobal {

    private Minecraft mc;
    private LevelRenderer originalRenderer;

    public ScopeRenderGlobal(Minecraft mc) {
        this.mc = mc;
        this.originalRenderer = mc.levelRenderer;
    }

    public void renderLevel(PoseStack poseStack, float partialTick, long finishTimeNano,
                            boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                            LightTexture lightTexture, Matrix4f projectionMatrix) {
        // Пустая реализация - ничего не рендерим для прицела
    }

    public void enable() {
        // Временно отключаем - не можем заменить levelRenderer
    }

    public void disable() {
        // Временно отключаем
    }
}