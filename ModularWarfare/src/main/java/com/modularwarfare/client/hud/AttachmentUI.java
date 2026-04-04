package com.modularwarfare.client.hud;

import com.modularwarfare.common.guns.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.ClientRenderHooks;
import com.modularwarfare.client.input.KeyType;
import com.modularwarfare.common.network.PacketGunAddAttachment;
import com.modularwarfare.common.network.PacketGunUnloadAttachment;
import com.modularwarfare.common.type.BaseType;
import com.modularwarfare.utility.RenderHelperMW;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class AttachmentUI {
    public int selectedAttachTypeIndex = 0;
    public int selectedAttachIndex = 0;
    public int sizeAttachTypeIndex = 0;
    public int sizeAttachAttachIndex = 0;
    public AttachmentEnum selectedAttachEnum;

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        ItemStack gunStack = mc.player.getMainHandItem();
        if (!(gunStack.getItem() instanceof ItemGun gun)) return;
        if (!ClientRenderHooks.getAnimMachine(mc.player).attachmentMode) return;

        if (gun.type.modelSkins != null && gun.type.acceptedAttachments != null) {
            List<AttachmentEnum> keys = new ArrayList<>(gun.type.acceptedAttachments.keySet());
            if (gun.type.modelSkins.length > 1) {
                keys.add(AttachmentEnum.Skin);
            }

            if (selectedAttachTypeIndex >= 0 && selectedAttachTypeIndex < keys.size()) {
                selectedAttachEnum = keys.get(selectedAttachTypeIndex);
                List<Integer> slots = checkAttach(mc.player, gun.type, selectedAttachEnum);
                sizeAttachTypeIndex = keys.size();
                sizeAttachAttachIndex = slots.size();

                if (selectedAttachIndex >= 0 && selectedAttachIndex < slots.size()) {
                    if (GunType.getAttachment(gunStack, selectedAttachEnum) == null) {
                        ModularWarfare.NETWORK.sendToServer(new PacketGunAddAttachment(slots.get(selectedAttachIndex)));
                        selectedAttachIndex = 0;
                    }
                } else {
                    selectedAttachIndex = 0;
                }
            }
        }
    }

    // В методе checkAttach исправьте:
    public List<Integer> checkAttach(Player player, GunType gunType, AttachmentEnum attachmentEnum) {
        List<Integer> attachments = new ArrayList<>();
        attachments.add(-1);

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty()) continue;

            if (attachmentEnum == AttachmentEnum.Skin) {
                if (stack.getItem() instanceof ItemAttachment attachment) {
                    AttachmentType attachType = attachment.type;  // Исправлено: attachment.type вместо attachmentType
                    if (attachType.attachmentType == attachmentEnum) {
                        attachments.add(i);
                    }
                } else if (stack.getItem() instanceof ItemSpray spray) {
                    for (int j = 0; j < gunType.modelSkins.length; j++) {
                        if (gunType.modelSkins[j].internalName.equalsIgnoreCase(spray.type.skinName)) {
                            attachments.add(i);
                        }
                    }
                }
            } else {
                if (stack.getItem() instanceof ItemAttachment attachment) {
                    AttachmentType attachType = attachment.type;  // Исправлено: attachment.type вместо attachmentType
                    if (attachType.attachmentType == attachmentEnum) {
                        attachments.add(i);
                    }
                }
            }
        }
        return attachments;
    }
    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();

        ItemStack gunStack = mc.player.getMainHandItem();
        if (gunStack.getItem() instanceof ItemGun && ClientRenderHooks.getAnimMachine(mc.player).attachmentMode) {
            RenderHelperMW.renderCenteredText(guiGraphics, ChatFormatting.YELLOW + "[Attachment mode]", width / 2, height - 32, -1);

            if (selectedAttachEnum != null) {
                ItemGun gun = (ItemGun) gunStack.getItem();
                if (gun.type.modelSkins != null && gun.type.acceptedAttachments != null) {
                    List<AttachmentEnum> keys = new ArrayList<>(gun.type.acceptedAttachments.keySet());
                    if (gun.type.modelSkins.length > 1) {
                        keys.add(AttachmentEnum.Skin);
                    }

                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(0, -18, 0);
                    RenderHelperMW.renderCenteredText(guiGraphics, firstArrowType(selectedAttachTypeIndex) + " " + selectedAttachEnum + " " + secondArrowType(selectedAttachTypeIndex, keys.size()), width / 2 - 50, height - 40, -1);
                    RenderHelperMW.renderCenteredText(guiGraphics, "Change", width / 2 + 10, height - 40, -1);
                    RenderHelperMW.renderCenteredText(guiGraphics, "Unattach", width / 2 + 60, height - 40, -1);

                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(width / 2 + 10, height - 42, 0);
                    guiGraphics.pose().scale(1, -1, 1);
                    RenderHelperMW.renderCenteredText(guiGraphics, firstArrowAttach(selectedAttachIndex, sizeAttachAttachIndex) + "[V]", 0, 0, -1);
                    guiGraphics.pose().popPose();

                    ChatFormatting color = GunType.getAttachment(gunStack, selectedAttachEnum) != null ? ChatFormatting.GREEN : ChatFormatting.GRAY;
                    RenderHelperMW.renderCenteredText(guiGraphics, color + "[V]", width / 2 + 60, height - 30, -1);
                    guiGraphics.pose().popPose();
                } else {
                    resetAttachmentMode();
                }
            }
        }
    }

    public void processKeyInput(KeyType type) {
        switch (type) {
            case Left:
                if (selectedAttachTypeIndex - 1 >= 0) selectedAttachTypeIndex--;
                break;
            case Right:
                if (selectedAttachTypeIndex + 1 < sizeAttachTypeIndex) selectedAttachTypeIndex++;
                break;
            case Up:
                if (selectedAttachIndex - 1 >= 0) {
                    selectedAttachIndex--;
                } else if (selectedAttachIndex == 0) {
                    ModularWarfare.NETWORK.sendToServer(new PacketGunUnloadAttachment(selectedAttachEnum.getName(), false));
                }
                break;
            case Down:
                if (selectedAttachIndex + 1 < sizeAttachAttachIndex) selectedAttachIndex++;
                break;
        }
    }

    private String firstArrowType(int index) {
        return index > 0 ? ChatFormatting.GREEN + "[<]" + ChatFormatting.RESET : ChatFormatting.GRAY + "[<]" + ChatFormatting.RESET;
    }

    private String secondArrowType(int index, int size) {
        return index == size - 1 ? ChatFormatting.GRAY + "[>]" + ChatFormatting.RESET : ChatFormatting.GREEN + "[>]" + ChatFormatting.RESET;
    }

    private String firstArrowAttach(int index, int size) {
        return index == size - 1 ? ChatFormatting.GRAY + "" : ChatFormatting.GREEN + "";
    }

    public void resetAttachmentMode() {
        selectedAttachTypeIndex = 0;
        selectedAttachIndex = 0;
        sizeAttachTypeIndex = 0;
        sizeAttachAttachIndex = 0;
    }
}