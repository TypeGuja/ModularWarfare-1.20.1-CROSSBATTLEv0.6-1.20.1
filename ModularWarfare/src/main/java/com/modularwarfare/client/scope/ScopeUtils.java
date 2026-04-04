package com.modularwarfare.client.scope;

import com.modularwarfare.client.model.renders.RenderParameters;
import com.modularwarfare.common.guns.AttachmentEnum;
import com.modularwarfare.common.guns.AttachmentType;
import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemAttachment;
import com.modularwarfare.common.guns.ItemGun;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ScopeUtils {
    public static int MIRROR_TEX = -1;
    private static final int QUALITY = 1024;
    private static boolean initialized = false;

    public ScopeUtils() {
        // Не создаём текстуру в конструкторе - отложим до первого использования
    }

    private static void initTexture() {
        if (!initialized && Minecraft.getInstance().level != null && Minecraft.getInstance().getWindow() != null) {
            try {
                MIRROR_TEX = GL11.glGenTextures();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, MIRROR_TEX);
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, QUALITY, QUALITY, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                initialized = true;
            } catch (Exception e) {
                System.err.println("Failed to initialize scope texture: " + e.getMessage());
            }
        }
    }

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;
        if (Minecraft.getInstance().player == null) return;

        // Инициализируем текстуру при первом рендере
        initTexture();

        if (MIRROR_TEX == -1) return;

        ItemStack gunStack = Minecraft.getInstance().player.getMainHandItem();
        if (gunStack.getItem() instanceof ItemGun && RenderParameters.adsSwitch > 0 &&
                Minecraft.getInstance().options.keyUse.isDown()) {

            ItemStack sightStack = GunType.getAttachment(gunStack, AttachmentEnum.Sight);
            if (sightStack != null && sightStack.getItem() instanceof ItemAttachment) {
                AttachmentType attachmentType = ((ItemAttachment) sightStack.getItem()).type;
                renderWorld(Minecraft.getInstance(), attachmentType, event.getPartialTick());
            }
        }
    }

    private static void renderWorld(Minecraft mc, AttachmentType scopeType, float partialTick) {
        // Временно отключаем, так как требует FBO
        // Эта функциональность будет добавлена позже
    }
}