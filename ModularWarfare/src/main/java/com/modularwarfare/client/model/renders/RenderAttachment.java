package com.modularwarfare.client.model.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.client.model.ModelAttachment;
import com.modularwarfare.client.model.objects.CustomItemRenderType;
import com.modularwarfare.client.model.objects.CustomItemRenderer;
import com.modularwarfare.common.guns.AttachmentType;
import com.modularwarfare.common.guns.ItemAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public class RenderAttachment extends CustomItemRenderer {

    @Override
    public void renderItem(CustomItemRenderType type, ItemStack item, Object... data) {
        if (!(item.getItem() instanceof ItemAttachment)) return;

        AttachmentType attachmentType = ((ItemAttachment) item.getItem()).type;
        if (attachmentType == null) return;

        ModelAttachment model = (ModelAttachment) attachmentType.model;
        if (model == null) return;

        renderAttachment(type, item, attachmentType, data);
    }

    private void renderAttachment(CustomItemRenderType renderType, ItemStack item, AttachmentType attachmentType, Object... data) {
        ModelAttachment model = (ModelAttachment) attachmentType.model;
        PoseStack poseStack = new PoseStack();
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

        switch (renderType) {
            case ENTITY:
                poseStack.translate(-0.45f, -0.05f, 0);
                break;
            case EQUIPPED:
                float crouchOffset = Minecraft.getInstance().player.isCrouching() ? 0.2f : 0.0f;
                poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-10));
                poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-90));
                poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90));
                poseStack.translate(-0.15f, 0.15f, -0.025f);
                poseStack.scale(1, 1, 1);
                break;
            case EQUIPPED_FIRST_PERSON:
                float modelScale = model.config.extra.modelScale;
                float rotateX = 0;
                float rotateY = 46.0f - 1.0f * RenderParameters.adsSwitch;
                float rotateZ = 1.0f + -1.0f * RenderParameters.adsSwitch;
                Vector3f translateXYZ = new Vector3f(-1.3000001f, 0.834f - -0.064f * RenderParameters.adsSwitch, -1.05f - 0.35f * RenderParameters.adsSwitch);

                poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(rotateX));
                poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotateY));
                poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(rotateZ));
                poseStack.translate(translateXYZ.x, translateXYZ.y, translateXYZ.z);
                break;
        }

        int skinId = 0;
        if (item.hasTag() && item.getTag().contains("skinId")) {
            skinId = item.getTag().getInt("skinId");
        }
        String path = skinId > 0 ? attachmentType.modelSkins[skinId].getSkin() : attachmentType.modelSkins[0].getSkin();

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(
                new ResourceLocation("modularwarfare", String.format("textures/skins/attachments/%s.png", path))));
        poseStack.scale(model.config.extra.modelScale, model.config.extra.modelScale, model.config.extra.modelScale);
        model.renderAttachment(poseStack, consumer, 0.0625f);

        buffer.endBatch();
    }
}