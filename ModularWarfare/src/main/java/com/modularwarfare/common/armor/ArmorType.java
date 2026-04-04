package com.modularwarfare.common.armor;

import com.google.gson.Gson;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.api.MWArmorType;
import com.modularwarfare.client.config.ArmorRenderConfig;
import com.modularwarfare.client.model.ModelCustomArmor;
import com.modularwarfare.common.type.BaseType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Iterator;

public class ArmorType extends BaseType {
    public Integer durability;
    public double defense;
    public HashMap<MWArmorType, ArmorInfo> armorTypes = new HashMap<>();

    public void initializeArmor(String slot) {
        Iterator<MWArmorType> iterator = armorTypes.keySet().iterator();
        while (iterator.hasNext()) {
            MWArmorType armorType = iterator.next();
            if (armorType.name().toLowerCase().equalsIgnoreCase(slot)) {
                String suffix = armorTypes.size() > 1 ? "_" + slot : "";
                armorTypes.get(armorType).internalName = internalName + suffix;
            }
        }
    }

    @Override
    public void loadExtraValues() {
        if (maxStackSize == null) {
            maxStackSize = 1;
        }
        loadBaseValues();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void reloadModel() {
        this.bipedModel = new ModelCustomArmor(ModularWarfare.getRenderConfig(this, new Gson(), ArmorRenderConfig.class), this);
    }

    @Override
    public String getAssetDir() {
        return "armor";
    }

    public static class ArmorInfo {
        public String displayName;
        public transient String internalName;
    }
}