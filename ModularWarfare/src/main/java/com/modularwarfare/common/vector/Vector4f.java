package com.modularwarfare.common.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Vector4f extends Vector implements Serializable, ReadableVector4f, WritableVector4f {
    private static final long serialVersionUID = 1L;
    public float x, y, z, w;

    public Vector4f() {}
    public Vector4f(ReadableVector4f src) { set(src); }
    public Vector4f(float x, float y, float z, float w) { set(x, y, z, w); }

    @Override
    public void set(float x, float y) { this.x = x; this.y = y; }
    @Override
    public void set(float x, float y, float z) { this.x = x; this.y = y; this.z = z; }
    @Override
    public void set(float x, float y, float z, float w) { this.x = x; this.y = y; this.z = z; this.w = w; }
    public Vector4f set(ReadableVector4f src) { this.x = src.getX(); this.y = src.getY(); this.z = src.getZ(); this.w = src.getW(); return this; }

    @Override
    public float lengthSquared() { return x * x + y * y + z * z + w * w; }

    @Override
    public Vector negate() { x = -x; y = -y; z = -z; w = -w; return this; }

    @Override
    public Vector load(FloatBuffer buf) { x = buf.get(); y = buf.get(); z = buf.get(); w = buf.get(); return this; }
    @Override
    public Vector scale(float scale) { x *= scale; y *= scale; z *= scale; w *= scale; return this; }
    @Override
    public Vector store(FloatBuffer buf) { buf.put(x); buf.put(y); buf.put(z); buf.put(w); return this; }

    @Override
    public float getX() { return x; }
    @Override
    public float getY() { return y; }
    @Override
    public float getZ() { return z; }
    @Override
    public float getW() { return w; }
    @Override
    public void setX(float x) { this.x = x; }
    @Override
    public void setY(float y) { this.y = y; }
    @Override
    public void setZ(float z) { this.z = z; }
    @Override
    public void setW(float w) { this.w = w; }

    @Override
    public String toString() { return String.format("Vector4f[%.2f, %.2f, %.2f, %.2f]", x, y, z, w); }
}