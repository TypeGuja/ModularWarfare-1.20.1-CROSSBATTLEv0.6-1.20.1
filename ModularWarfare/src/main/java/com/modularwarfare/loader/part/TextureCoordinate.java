package com.modularwarfare.loader.part;

public class TextureCoordinate {
    public float u;
    public float v;
    public float w;

    public TextureCoordinate(float u, float v) {
        this(u, v, 0.0f);
    }

    public TextureCoordinate(float u, float v, float w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    @Override
    public String toString() {
        return String.format("TextureCoordinate[%.4f, %.4f, %.4f]", u, v, w);
    }
}