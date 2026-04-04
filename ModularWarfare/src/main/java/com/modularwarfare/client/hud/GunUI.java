package com.modularwarfare.client.hud;

import com.modularwarfare.ModularWarfare;
import com.mojang.blaze3d.systems.RenderSystem;

import com.modularwarfare.client.ClientProxy;
import com.modularwarfare.client.ClientRenderHooks;
import com.modularwarfare.client.model.renders.RenderParameters;
import com.modularwarfare.common.guns.AttachmentEnum;
import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemAmmo;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.utility.RayUtil;
import com.modularwarfare.utility.RenderHelperMW;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class GunUI {
    private static final ResourceLocation CROSSHAIR = new ResourceLocation("modularwarfare", "textures/gui/crosshair.png");
    private static final ResourceLocation REDDOT = new ResourceLocation("modularwarfare", "textures/gui/reddot.png");
    private static final ResourceLocation GREENDOT = new ResourceLocation("modularwarfare", "textures/gui/greendot.png");
    private static final ResourceLocation BLUEDOT = new ResourceLocation("modularwarfare", "textures/gui/bluedot.png");
    private static final ResourceLocation HIT_MARKER = new ResourceLocation("modularwarfare", "textures/gui/hitmarker.png");
    private static final ResourceLocation HIT_MARKER_HS = new ResourceLocation("modularwarfare", "textures/gui/hitmarkerhs.png");

    public static int hitMarkerTime = 0;
    public static boolean hitMarkerHeadshot;
    public static float bulletSnapFade;

    @SubscribeEvent
    public static void onRender(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();
        ItemStack stack = mc.player.getMainHandItem();

        if (stack.getItem() instanceof ItemGun) {
            renderScope(mc, guiGraphics, width, height);

            if (ModularWarfare.ModConfig.INSTANCE.enableDynamicCrosshair && !ClientRenderHooks.getAnimMachine(mc.player).attachmentMode &&
                    !ClientRenderHooks.isAimingScope && !ClientRenderHooks.isAiming && mc.options.keyUse.isDown() &&
                    !mc.player.isSprinting() && !ClientRenderHooks.getAnimMachine(mc.player).reloading &&
                    mc.player.getMainHandItem().getItem() instanceof ItemGun) {
                renderDynamicCrosshair(guiGraphics, width, height);
            }

            renderHitMarker(guiGraphics, width, height);

            if (ModularWarfare.ModConfig.INSTANCE.showAmmoCount) {
                renderPlayerAmmo(mc, guiGraphics, width, height);
            }

            renderPlayerSnap(guiGraphics, width, height);
        }
    }

    private static void renderScope(Minecraft mc, GuiGraphics guiGraphics, int width, int height) {
        ItemStack gunStack = mc.player.getMainHandItem();
        if (mc.options.keyUse.isDown() && ClientRenderHooks.isAimingScope &&
                gunStack.getItem() instanceof ItemGun &&
                GunType.getAttachment(gunStack, AttachmentEnum.Sight) != null) {

            var attachment = (com.modularwarfare.common.guns.ItemAttachment) GunType.getAttachment(gunStack, AttachmentEnum.Sight).getItem();
            if (attachment.type.sight != null && attachment.type.sight.scopeType != null) {
                float gunRotX = RenderParameters.GUN_ROT_X_LAST + (RenderParameters.GUN_ROT_X - RenderParameters.GUN_ROT_X_LAST) * ClientProxy.renderHooks.partialTicks;
                float gunRotY = RenderParameters.GUN_ROT_Y_LAST + (RenderParameters.GUN_ROT_Y - RenderParameters.GUN_ROT_Y_LAST) * ClientProxy.renderHooks.partialTicks;
                float alpha = Math.abs(gunRotX);

                if (gunRotX > -1.5f && gunRotX < 1.5f && gunRotY > -0.75f && gunRotY < 0.75f) {
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderTexture(0, getDotTexture(attachment.type.sight.dotColorType));
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f - alpha);

                    guiGraphics.blit(getDotTexture(attachment.type.sight.dotColorType), width / 2 - 8, height / 2 - 8, 0, 0, 16, 16, 16, 16);
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
    }

    private static ResourceLocation getDotTexture(com.modularwarfare.common.guns.WeaponDotColorType type) {
        return switch (type) {
            case RED -> REDDOT;
            case BLUE -> BLUEDOT;
            case GREEN -> GREENDOT;
            default -> REDDOT;
        };
    }

    private static void renderDynamicCrosshair(GuiGraphics guiGraphics, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        float accuracy = RayUtil.calculateAccuracyClient((ItemGun) mc.player.getMainHandItem().getItem(), mc.player);
        int move = Math.max(0, (int) (accuracy * 3.0f));

        int x = width / 2;
        int y = height / 2;

        guiGraphics.blit(CROSSHAIR, x, y, 0, 1, 1, 1, 16, 16, 16, 16);
        guiGraphics.blit(CROSSHAIR, x, y + move, 0, 1, 1, 1, 4, 16, 16, 16);
        guiGraphics.blit(CROSSHAIR, x, y - move - 3, 0, 1, 1, 1, 4, 16, 16, 16);
        guiGraphics.blit(CROSSHAIR, x + move, y, 0, 1, 1, 1, 4, 16, 16, 16);
        guiGraphics.blit(CROSSHAIR, x - move - 3, y, 0, 1, 1, 1, 4, 16, 16, 16);
    }

    private static void renderHitMarker(GuiGraphics guiGraphics, int width, int height) {
        if (hitMarkerTime > 0) {
            ResourceLocation texture = hitMarkerHeadshot ? HIT_MARKER_HS : HIT_MARKER;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.enableBlend();
            float alpha = Math.max((hitMarkerTime - 10.0f + ClientProxy.renderHooks.partialTicks) / 10.0f, 0.0f);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

            guiGraphics.blit(texture, width / 2 - 4, height / 2 - 4, 0, 0, 9, 9, 16, 16);

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.disableBlend();
        }
    }

    private static void renderPlayerAmmo(Minecraft mc, GuiGraphics guiGraphics, int width, int height) {
        ItemStack stack = mc.player.getMainHandItem();
        if (stack.getItem() instanceof ItemGun && stack.hasTag()) {
            CompoundTag ammoTag = stack.getTag().getCompound("ammo");
            ItemStack ammoStack = ItemStack.of(ammoTag);
            int top = height - 38;
            int right = Math.min(68, width / 2 - 60);
            int bottom = top + 22;

            if (ammoStack.getItem() instanceof ItemAmmo itemAmmo) {
                int currentAmmo = ItemGun.getMagazineBullets(stack);

                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                guiGraphics.fill(2 + right - 3, top, right * 2 - 18, bottom, Integer.MIN_VALUE);

                // Исправленные методы рендера предметов
                guiGraphics.renderItem(ammoStack, 69, height - 35);
                guiGraphics.renderItemDecorations(mc.font, ammoStack, 69, height - 35);

                String color = currentAmmo < itemAmmo.type.ammoCapacity / 6 ? ChatFormatting.RED.toString() : ChatFormatting.WHITE.toString();
                if (currentAmmo < itemAmmo.type.ammoCapacity / 6) {
                    guiGraphics.drawString(mc.font, ChatFormatting.YELLOW + "[R] " + ChatFormatting.WHITE + "Reload", 10, height - 30, 0xFFFFFF);
                }

                String text = color + currentAmmo + "/" + itemAmmo.type.ammoCapacity;
                guiGraphics.drawString(mc.font, text, 85, height - 30, 0xFFFFFF);

                if (GunType.getFireMode(stack) != null) {
                    RenderHelperMW.renderCenteredTextWithShadow(guiGraphics, GunType.getFireMode(stack).toString(), 92, height - 50, 0xFFFFFF);
                }
            } else if (stack.getTag().contains("bullet")) {
                var bullet = ItemGun.getUsedBullet(stack, ((ItemGun) stack.getItem()).type);
                int currentAmmo = stack.getTag().getInt("ammocount");
                int maxAmmo = ((ItemGun) stack.getItem()).type.numBullets;

                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                guiGraphics.fill(2 + right - 3, top, right * 2 - 18, bottom, Integer.MIN_VALUE);

                ItemStack bulletStack = bullet != null ? new ItemStack(bullet) : ItemStack.EMPTY;
                guiGraphics.renderItem(bulletStack, 69, height - 35);
                guiGraphics.renderItemDecorations(mc.font, bulletStack, 69, height - 35);

                String color = currentAmmo < maxAmmo / 6 ? ChatFormatting.RED.toString() : ChatFormatting.WHITE.toString();
                if (currentAmmo < maxAmmo / 6) {
                    guiGraphics.drawString(mc.font, ChatFormatting.YELLOW + "[R] " + ChatFormatting.WHITE + "Reload", 10, height - 30, 0xFFFFFF);
                }

                String text = color + currentAmmo + "/" + maxAmmo;
                guiGraphics.drawString(mc.font, text, 85, height - 30, 0xFFFFFF);

                if (GunType.getFireMode(stack) != null) {
                    RenderHelperMW.renderCenteredTextWithShadow(guiGraphics, GunType.getFireMode(stack).toString(), 92, height - 50, 0xFFFFFF);
                }
            }
        }
    }

    private static void renderPlayerSnap(GuiGraphics guiGraphics, int width, int height) {
        ResourceLocation texture = new ResourceLocation("modularwarfare", "textures/gui/snapshadow.png");
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, (float) bulletSnapFade);

        guiGraphics.blit(texture, 0, 0, 0, 0, width, height, width, height);

        RenderSystem.disableBlend();
    }

    public static void addHitMarker(boolean headshot) {
        hitMarkerTime = 20;
        hitMarkerHeadshot = headshot;
    }
}