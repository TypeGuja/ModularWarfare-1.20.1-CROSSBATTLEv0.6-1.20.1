package com.modularwarfare.common.body;

import com.google.gson.Gson;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.config.BodyRenderConfig;
import com.modularwarfare.client.model.ModelBody;
import com.modularwarfare.common.type.BaseType;

public class BodyType extends BaseType {
    public int size = 16;

    @Override
    public void loadExtraValues() {
        if (maxStackSize == null) maxStackSize = 1;
        loadBaseValues();
    }

    @Override
    public void reloadModel() {
        this.model = new ModelBody(
                ModularWarfare.getRenderConfig(this, new Gson(), BodyRenderConfig.class),
                this
        );
    }

    @Override
    public String getAssetDir() {
        return "bodies";
    }
}