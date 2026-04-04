package com.modularwarfare.client.model.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.modularwarfare.client.model.ModelBackpack;
import com.modularwarfare.common.backpacks.ItemBackpack;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RenderLayerBackpack extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public RenderLayerBackpack(PlayerRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        player.getCapability(CapabilityExtra.EXTRA_CAPABILITY).ifPresent(extra -> {
            ItemStack backpackStack = extra.getStackInSlot(0);
            if (!backpackStack.isEmpty() && backpackStack.getItem() instanceof ItemBackpack) {
                ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();

                poseStack.pushPose();
                if (player.isCrouching()) {
                    poseStack.translate(0, 0.3f, 0);
                    poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(30));
                }

                int skinId = 0;
                if (backpackStack.hasTag() && backpackStack.getTag().contains("skinId")) {
                    skinId = backpackStack.getTag().getInt("skinId");
                }

                String path = skinId > 0 && backpack.type.modelSkins.length > skinId ?
                        backpack.type.modelSkins[skinId].getSkin() :
                        (backpack.type.modelSkins.length > 0 ? backpack.type.modelSkins[0].getSkin() : "default");
                ResourceLocation texture = ResourceLocation.tryBuild("modularwarfare",
                        String.format("textures/skins/backpacks/%s.png", path));

                if (texture != null && backpack.type.model instanceof ModelBackpack) {
                    VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
                    ModelBackpack model = (ModelBackpack) backpack.type.model;
                    model.render(poseStack, consumer, "backpackModel", 0.0625f, model.config.extra.modelScale);
                }

                poseStack.popPose();
            }
        });
    }
}