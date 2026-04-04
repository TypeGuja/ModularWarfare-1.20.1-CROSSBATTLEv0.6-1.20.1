package com.modularwarfare.common.network;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.guns.AttachmentEnum;
import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.ItemGun;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PacketGunUnloadAttachment extends PacketBase {
    public String attachmentType;
    public boolean unloadAll;

    public PacketGunUnloadAttachment() {}

    public PacketGunUnloadAttachment(String attachmentType, boolean unloadAll) {
        this.attachmentType = attachmentType;
        this.unloadAll = unloadAll;
    }

    @Override
    public void encodeInto(FriendlyByteBuf buffer) {
        buffer.writeUtf(attachmentType);
        buffer.writeBoolean(unloadAll);
    }

    @Override
    public void decodeInto(FriendlyByteBuf buffer) {
        this.attachmentType = buffer.readUtf();
        this.unloadAll = buffer.readBoolean();
    }

    @Override
    public void handleServerSide(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack gunStack = player.getMainHandItem();
            if (!gunStack.isEmpty() && gunStack.getItem() instanceof ItemGun) {
                if (unloadAll) {
                    for (AttachmentEnum att : AttachmentEnum.values()) {
                        ItemStack attStack = GunType.getAttachment(gunStack, att);
                        if (attStack != null && !attStack.isEmpty()) {
                            GunType.removeAttachment(gunStack, att);
                            player.getInventory().add(attStack);
                        }
                    }
                    ModularWarfare.NETWORK.sendTo(new PacketPlaySound(player.blockPosition(), "attachment.apply", 1.0f, 1.0f), serverPlayer);
                } else {
                    AttachmentEnum att = AttachmentEnum.getAttachment(attachmentType);
                    ItemStack attStack = GunType.getAttachment(gunStack, att);
                    if (attStack != null && !attStack.isEmpty()) {
                        GunType.removeAttachment(gunStack, att);
                        player.getInventory().add(attStack);
                        ModularWarfare.NETWORK.sendTo(new PacketPlaySound(player.blockPosition(), "attachment.apply", 1.0f, 1.0f), serverPlayer);
                    }
                }
            }
        }
    }

    @Override
    public void handleClientSide(Player player) {}
}