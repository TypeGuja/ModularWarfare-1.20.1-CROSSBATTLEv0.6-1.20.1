package com.modularwarfare.client.model.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.client.model.ModelBody;
import com.modularwarfare.common.body.ItemBody;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RenderLayerBody extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public RenderLayerBody(PlayerRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        player.getCapability(CapabilityExtra.EXTRA_CAPABILITY).ifPresent(extra -> {
            ItemStack bodyStack = extra.getStackInSlot(1);
            if (!bodyStack.isEmpty() && bodyStack.getItem() instanceof ItemBody) {
                ItemBody body = (ItemBody) bodyStack.getItem();

                poseStack.pushPose();
                if (player.isCrouching()) {
                    poseStack.translate(0, 0.2f, 0);
                }

                int skinId = 0;
                if (bodyStack.hasTag() && bodyStack.getTag().contains("skinId")) {
                    skinId = bodyStack.getTag().getInt("skinId");
                }

                String path = skinId > 0 && body.type.modelSkins.length > skinId ?
                        body.type.modelSkins[skinId].getSkin() :
                        (body.type.modelSkins.length > 0 ? body.type.modelSkins[0].getSkin() : "default");
                ResourceLocation texture = ResourceLocation.tryBuild("modularwarfare",
                        String.format("textures/skins/bodies/%s.png", path));

                if (texture != null && body.type.model instanceof ModelBody) {
                    VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
                    ModelBody model = (ModelBody) body.type.model;
                    model.render(poseStack, consumer, "bodyModel", 0.0625f, model.config.extra.modelScale);
                }

                poseStack.popPose();
            }
        });
    }
}