package com.modularwarfare.common.guns;

import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemBullet extends BaseItem {
    public BulletType type;

    public ItemBullet(BulletType type) {
        super(type);
        this.type = type;
        this.render3d = false;
    }

    @Override
    public void setType(BaseType type) {
        this.type = (BulletType) type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (!type.bulletProperties.isEmpty()) {
            String key = type.bulletProperties.keySet().iterator().next();
            tooltip.add(generateLoreHeader("Modifiers"));
            BulletProperty property = type.bulletProperties.get(key);
            tooltip.add(generateLoreListEntry(key + " Damage", String.format("%.1fx", property.bulletDamageFactor)));

            if (property.potionEffects != null) {
                tooltip.add(generateLoreHeader("Effects"));
                for (PotionEntry entry : property.potionEffects) {
                    // Add potion effect tooltip
                }
            }
        }
    }
}