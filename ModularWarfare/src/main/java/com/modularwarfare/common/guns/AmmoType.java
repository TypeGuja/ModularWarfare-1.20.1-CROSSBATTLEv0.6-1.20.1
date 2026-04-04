package com.modularwarfare.common.guns;

import com.google.gson.Gson;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.config.AmmoRenderConfig;
import com.modularwarfare.client.model.ModelAmmo;
import com.modularwarfare.common.type.BaseType;

public class AmmoType extends BaseType {
    public int ammoCapacity = 30;
    public int magazineCount = 1;
    public boolean allowEmptyMagazines = true;
    public boolean isDynamicAmmo = false;
    public float reloadTimeFactor = 1.0f;
    public boolean sameTextureAsGun = true;
    public String[] subAmmo;

    @Override
    public void loadExtraValues() {
        if (maxStackSize == null) {
            maxStackSize = 4;
        }
        loadBaseValues();
    }

    @Override
    public void reloadModel() {
        if (isDynamicAmmo) {
            this.model = new ModelAmmo(ModularWarfare.getRenderConfig(this, new Gson(), AmmoRenderConfig.class), this);
        }
    }

    @Override
    public String getAssetDir() {
        return "ammo";
    }
}