package com.modularwarfare.common.type;

import com.modularwarfare.common.guns.SkinType;
import com.modularwarfare.loader.MWModelBase;
import com.modularwarfare.loader.MWModelBipedBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class BaseType {
    @OnlyIn(Dist.CLIENT)
    public transient MWModelBase model;
    @OnlyIn(Dist.CLIENT)
    public transient MWModelBipedBase bipedModel;
    public Integer maxStackSize;
    public SkinType[] modelSkins;
    public String internalName;
    public String displayName;
    public String iconName;
    public transient int id;
    public transient String contentPack;
    public transient boolean isInDirectory;

    public void loadExtraValues() {
    }

    public void loadBaseValues() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            // НЕ ПЕРЕЗАПИСЫВАЕМ МОДЕЛЬ, ЕСЛИ ОНА УЖЕ ЗАГРУЖЕНА ИЗ RENDER.JSON
            if (this.model == null && this.bipedModel == null) {
                reloadModel();
            }
        }
        if (modelSkins == null) {
            modelSkins = new SkinType[]{SkinType.getDefaultSkin(this)};
        }
        if (iconName == null) {
            iconName = internalName;
        }
    }

    public void reloadModel() {
    }

    public boolean hasModel() {
        return model != null || bipedModel != null;
    }

    @Override
    public String toString() {
        return internalName;
    }

    public String getAssetDir() {
        return "";
    }
}