package com.modularwarfare.common.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public abstract class Vector implements Serializable, ReadableVector {
    protected Vector() {}

    @Override
    public final float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    @Override
    public abstract float lengthSquared();

    public abstract Vector load(FloatBuffer buf);
    public abstract Vector negate();

    public final Vector normalise() {
        float len = length();
        if (len != 0.0f) {
            return scale(1.0f / len);
        }
        return this;
    }

    @Override
    public abstract Vector store(FloatBuffer buf);
    public abstract Vector scale(float scale);
}