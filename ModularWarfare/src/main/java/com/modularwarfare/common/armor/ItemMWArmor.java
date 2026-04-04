package com.modularwarfare.common.armor;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.api.MWArmorType;
import com.modularwarfare.client.model.ModelCustomArmor;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ItemMWArmor extends ArmorItem {
    public ArmorType type;
    public BaseType baseType;
    public String internalName;

    public ItemMWArmor(ArmorType type, MWArmorType armorSlot) {
        super(new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type p_266807_) { return type.durability != null ? type.durability : 1; }
            @Override
            public int getDefenseForType(ArmorItem.Type p_266752_) { return (int) (type.defense * 20); }
            @Override
            public int getEnchantmentValue() { return 0; }
            @Override
            public net.minecraft.sounds.SoundEvent getEquipSound() { return net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER; }
            @Override
            public net.minecraft.world.item.crafting.Ingredient getRepairIngredient() { return net.minecraft.world.item.crafting.Ingredient.EMPTY; }
            @Override
            public String getName() { return ""; }
            @Override
            public float getToughness() { return 0; }
            @Override
            public float getKnockbackResistance() { return 0; }
        }, getSlotType(armorSlot), new Properties());

        type.initializeArmor(armorSlot.name().toLowerCase());
        type.loadExtraValues();
        this.internalName = type.armorTypes.get(armorSlot).internalName;
        ForgeRegistries.ITEMS.register(new ResourceLocation(ModularWarfare.MOD_ID, this.internalName), this);
        this.baseType = type;
        this.type = type;
    }

    private static ArmorItem.Type getSlotType(MWArmorType armorSlot) {
        return switch (armorSlot) {
            case Head -> ArmorItem.Type.HELMET;
            case Chest -> ArmorItem.Type.CHESTPLATE;
            case Legs -> ArmorItem.Type.LEGGINGS;
            case Feet -> ArmorItem.Type.BOOTS;
            default -> ArmorItem.Type.CHESTPLATE;
        };
    }

    @Override
    public void inventoryTick(ItemStack stack, net.minecraft.world.level.Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player && stack.getTag() == null) {
            var tag = stack.getOrCreateTag();
            tag.putInt("skinId", 0);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int skinId = stack.getTag() != null ? stack.getTag().getInt("skinId") : 0;
        String path = skinId > 0 && this.type.modelSkins.length > skinId ?
                this.type.modelSkins[skinId].getSkin() :
                (this.type.modelSkins.length > 0 ? this.type.modelSkins[0].getSkin() : "default");
        return String.format("modularwarfare:textures/skins/armor/%s.png", path);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public net.minecraft.client.model.HumanoidModel getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, net.minecraft.client.model.HumanoidModel defaultModel) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemMWArmor) {
            ArmorType armorType = ((ItemMWArmor) stack.getItem()).type;
            if (armorType.bipedModel instanceof ModelCustomArmor) {
                ModelCustomArmor armorModel = (ModelCustomArmor) armorType.bipedModel;

                armorModel.showHead(slot == EquipmentSlot.HEAD);
                armorModel.showChest(slot == EquipmentSlot.CHEST);
                armorModel.showLegs(slot == EquipmentSlot.LEGS);
                armorModel.showFeet(slot == EquipmentSlot.FEET);

                armorModel.crouching = entity.isCrouching();
                armorModel.riding = entity.isPassenger();
                armorModel.young = entity.isBaby();

                return armorModel;
            }
        }
        return null;
    }
}