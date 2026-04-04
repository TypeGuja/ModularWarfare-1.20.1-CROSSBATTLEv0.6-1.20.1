package com.modularwarfare.common.guns;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.network.PacketGunReload;
import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemAmmo extends BaseItem {
    public AmmoType type;

    public ItemAmmo(AmmoType type) {
        super(type);
        this.type = type;
        this.render3d = false;
    }

    @Override
    public void setType(BaseType type) {
        this.type = (AmmoType) type;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (stack.getTag() == null && !level.isClientSide) {
            var tag = stack.getOrCreateTag();
            tag.putInt("ammocount", type.ammoCapacity);
            tag.putInt("skinId", 0);
            if (type.magazineCount > 1) {
                tag.putInt("magcount", 1);
                for (int i = 1; i <= type.magazineCount; i++) {
                    tag.putInt("ammocount" + i, type.ammoCapacity);
                }
            }
        }
    }

    public static boolean hasAmmo(ItemStack ammoStack) {
        if (ammoStack.hasTag()) {
            var tag = ammoStack.getTag();
            if (tag.contains("magcount")) {
                ItemAmmo itemAmmo = (ItemAmmo) ammoStack.getItem();
                for (int i = 0; i < itemAmmo.type.magazineCount; i++) {
                    if (tag.getInt("ammocount" + i) > 0) {
                        return true;
                    }
                }
            } else {
                return tag.getInt("ammocount") > 0;
            }
        }
        return false;
    }

    public static ItemBullet getUsedBullet(ItemStack gunStack) {
        if (ItemGun.hasAmmoLoaded(gunStack)) {
            CompoundTag ammoTag = gunStack.getOrCreateTag().getCompound("ammo");
            ItemStack ammoStack = ItemStack.of(ammoTag);
            if (ammoStack.hasTag() && ammoStack.getTag().contains("bullet")) {
                CompoundTag bulletTag = ammoStack.getTag().getCompound("bullet");
                ItemStack bulletStack = ItemStack.of(bulletTag);
                if (bulletStack.getItem() instanceof ItemBullet bullet) {
                    return bullet;
                }
            }
        }
        return null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (type.subAmmo != null) {
            ModularWarfare.NETWORK.sendToServer(new PacketGunReload());
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (type.magazineCount == 1) {
            int currentAmmoCount = 0;
            if (stack.hasTag()) {
                currentAmmoCount = stack.getTag().getInt("ammocount");
            } else {
                currentAmmoCount = type.ammoCapacity;
            }
            tooltip.add(generateLoreLineAlt("Ammo", Integer.toString(currentAmmoCount), Integer.toString(type.ammoCapacity)));
        } else if (stack.hasTag()) {
            String baseDisplayLine = ChatFormatting.BLUE + "Mag %s: " + ChatFormatting.GRAY + "%d" + ChatFormatting.DARK_GRAY + "/" + ChatFormatting.GRAY + "%s";
            for (int i = 1; i <= type.magazineCount; i++) {
                var tag = stack.getTag();
                tooltip.add(Component.literal(String.format(baseDisplayLine, i, tag.getInt("ammocount" + i), type.ammoCapacity)));
            }
        }

        if (stack.hasTag() && stack.getTag().contains("bullet")) {
            CompoundTag bulletTag = stack.getTag().getCompound("bullet");
            ItemStack bulletStack = ItemStack.of(bulletTag);
            if (bulletStack.getItem() instanceof ItemBullet bullet) {
                tooltip.add(generateLoreLine("Bullet", bullet.type.displayName));
            }
        }

        tooltip.add(Component.literal(ChatFormatting.BLUE + "Accepted bullets:"));
        if (type.subAmmo != null && type.subAmmo.length > 0) {
            for (String internalName : type.subAmmo) {
                tooltip.add(Component.literal("- " + internalName));
            }
        }

        tooltip.add(Component.literal(ChatFormatting.YELLOW + "[R] to reload"));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}