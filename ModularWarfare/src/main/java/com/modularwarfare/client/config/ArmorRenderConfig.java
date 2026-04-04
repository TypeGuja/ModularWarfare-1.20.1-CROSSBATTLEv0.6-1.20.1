package com.modularwarfare.client.config;

public class ArmorRenderConfig {
    public String modelFileName = "";
    public Extra extra = new Extra();

    public static class Extra {
        public float modelScale = 1.0f;
    }
}