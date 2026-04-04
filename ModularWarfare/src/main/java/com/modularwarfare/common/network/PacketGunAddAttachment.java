package com.modularwarfare.common.network;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.guns.AttachmentEnum;
import com.modularwarfare.common.guns.AttachmentType;
import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemAttachment;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.guns.ItemSpray;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PacketGunAddAttachment extends PacketBase {
    public int slot;

    public PacketGunAddAttachment() {}

    public PacketGunAddAttachment(int slot) {
        this.slot = slot;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeInt(slot);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.slot = buffer.readInt();
    }

    @Override
    public void handleServerSide(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack gunStack = player.getMainHandItem();
            if (!gunStack.isEmpty() && gunStack.getItem() instanceof ItemGun itemGun) {
                GunType gunType = itemGun.type;
                ItemStack attachStack = player.getInventory().getItem(slot);

                if (attachStack.getItem() instanceof ItemAttachment attach) {
                    AttachmentType attachType = attach.type;
                    if (gunType.acceptedAttachments.containsKey(attachType.attachmentType) &&
                            gunType.acceptedAttachments.get(attachType.attachmentType).contains(attachType.internalName)) {

                        ItemStack existing = GunType.getAttachment(gunStack, attachType.attachmentType);
                        if (existing != null && !existing.isEmpty()) {
                            GunType.removeAttachment(gunStack, attachType.attachmentType);
                            player.getInventory().add(existing);
                        }

                        GunType.addAttachment(gunStack, attachType.attachmentType, attachStack.copy());
                        attachStack.shrink(1);
                        ModularWarfare.NETWORK.sendTo(new PacketPlaySound(player.blockPosition(), "attachment.apply", 1.0f, 1.0f), serverPlayer);
                    }
                } else if (attachStack.getItem() instanceof ItemSpray spray) {
                    if (gunStack.hasTag()) {
                        for (int i = 0; i < gunType.modelSkins.length; i++) {
                            if (gunType.modelSkins[i].internalName.equalsIgnoreCase(spray.type.skinName)) {
                                gunStack.getTag().putInt("skinId", i);
                                attachStack.hurtAndBreak(1, player, p -> {});
                                ModularWarfare.NETWORK.sendTo(new PacketPlaySound(player.blockPosition(), "spray", 1.0f, 1.0f), serverPlayer);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void handleClientSide(Player player) {}
}