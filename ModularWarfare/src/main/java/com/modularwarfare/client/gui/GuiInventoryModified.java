package com.modularwarfare.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.modularwarfare.common.container.ContainerInventoryModified;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class GuiInventoryModified extends AbstractContainerScreen<ContainerInventoryModified> {
    private static final ResourceLocation ICONS = ResourceLocation.tryBuild("modularwarfare", "textures/gui/icons.png");
    private static final ResourceLocation INVENTORY_BG = ResourceLocation.tryBuild("modularwarfare", "textures/gui/inventory.png");
    private float oldMouseX;
    private float oldMouseY;

    public GuiInventoryModified(ContainerInventoryModified container, Inventory playerInv, Component title) {
        super(container, playerInv, title);
        this.imageWidth = 176;
        this.imageHeight = 185;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
    }

    @Override
    public void containerTick() {
        this.leftPos = (this.width - this.imageWidth) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.9f);
        guiGraphics.blit(INVENTORY_BG, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        var container = this.menu;
        var backpack = container.extra.getStackInSlot(0);

        backpack.getCapability(net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER).ifPresent(backpackInv -> {
            int x = leftPos + 180;
            int y = topPos + 18;

            guiGraphics.blit(ICONS, x - 5, y - 18, 18, 0, 82, 18);
            guiGraphics.blit(ICONS, x - 5, y, 18, 5, 82, 18);

            int xP = 0;
            int yP = 0;

            for (int i = 0; i < backpackInv.getSlots(); i++) {
                guiGraphics.blit(ICONS, x + xP * 18, y - 1 + yP * 18, 0, 0, 18, 18);

                if (++xP % 4 == 0) {
                    xP = 0;
                    yP++;
                    if (i + 1 < backpackInv.getSlots()) {
                        guiGraphics.blit(ICONS, x - 5, y + yP * 18, 18, 5, 82, 18);
                    }
                } else if (i + 1 >= backpackInv.getSlots()) {
                    yP++;
                }
            }

            guiGraphics.blit(ICONS, x - 5, y - 1 + yP * 18, 18, 33, 82, 5);

            if (!backpack.isEmpty()) {
                var backpackItem = (com.modularwarfare.common.backpacks.ItemBackpack) backpack.getItem();
                String displayName = backpackItem.type.displayName;
                guiGraphics.drawString(font, displayName, x, y - 12, 0xFFFFFF);
            }
        });

        // Draw player model - исправлено для 1.20.1 с использованием Quaternionf
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            // Создаём кватернионы для вращения модели
            Quaternionf cameraRotation = new Quaternionf().rotationXYZ(0, 0, 0);
            Quaternionf modelRotation = new Quaternionf().rotationXYZ(0, 0, 0);

            InventoryScreen.renderEntityInInventory(guiGraphics, leftPos + 51, topPos + 75, 30,
                    cameraRotation, modelRotation, player);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.oldMouseX = mouseX;
        this.oldMouseY = mouseY;
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // No labels to render
    }
}