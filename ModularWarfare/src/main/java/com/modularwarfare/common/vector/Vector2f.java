package com.modularwarfare.common.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Vector2f extends Vector implements Serializable, ReadableVector2f, WritableVector2f {
    private static final long serialVersionUID = 1L;
    public float x, y;

    public Vector2f() {}
    public Vector2f(ReadableVector2f src) { set(src); }
    public Vector2f(float x, float y) { set(x, y); }

    @Override
    public void set(float x, float y) { this.x = x; this.y = y; }
    public Vector2f set(ReadableVector2f src) { this.x = src.getX(); this.y = src.getY(); return this; }

    @Override
    public float lengthSquared() { return x * x + y * y; }
    public Vector2f translate(float x, float y) { this.x += x; this.y += y; return this; }
    @Override
    public Vector negate() { this.x = -x; this.y = -y; return this; }

    @Override
    public Vector load(FloatBuffer buf) { x = buf.get(); y = buf.get(); return this; }
    @Override
    public Vector scale(float scale) { x *= scale; y *= scale; return this; }
    @Override
    public Vector store(FloatBuffer buf) { buf.put(x); buf.put(y); return this; }

    @Override
    public float getX() { return x; }
    @Override
    public float getY() { return y; }
    @Override
    public void setX(float x) { this.x = x; }
    @Override
    public void setY(float y) { this.y = y; }

    @Override
    public String toString() { return String.format("Vector2f[%.2f, %.2f]", x, y); }
}