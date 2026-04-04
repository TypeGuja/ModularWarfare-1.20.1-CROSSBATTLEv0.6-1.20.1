package com.modularwarfare.common.guns;

import com.google.gson.Gson;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.config.BulletRenderConfig;
import com.modularwarfare.client.model.ModelBullet;
import com.modularwarfare.common.type.BaseType;

import java.util.HashMap;

public class BulletType extends BaseType {
    public HashMap<String, BulletProperty> bulletProperties = new HashMap<>();
    public boolean isSlug = false;
    public boolean isDynamicAmmo = false;

    @Override
    public void loadExtraValues() {
        if (maxStackSize == null) {
            maxStackSize = 64;
        }
        loadBaseValues();
    }

    @Override
    public void reloadModel() {
        if (isDynamicAmmo) {
            BulletRenderConfig config = ModularWarfare.getRenderConfig(this, new Gson(), BulletRenderConfig.class);
            if (config != null) {
                this.model = new ModelBullet(config, this);
            }
        }
    }

    @Override
    public String getAssetDir() {
        return "bullets";
    }
}