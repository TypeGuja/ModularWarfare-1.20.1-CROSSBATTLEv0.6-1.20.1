package com.modularwarfare.common.vector;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Vector3f extends Vector implements Serializable, ReadableVector3f, WritableVector3f {
    private static final long serialVersionUID = 1L;
    public float x, y, z;

    public Vector3f() {}
    public Vector3f(ReadableVector3f src) { set(src); }
    public Vector3f(float x, float y, float z) { set(x, y, z); }
    public Vector3f(Vec3 vec) { this((float) vec.x, (float) vec.y, (float) vec.z); }
    public Vector3f(BlockPos pos) { this(pos.getX(), pos.getY(), pos.getZ()); }

    public Vec3 toVec3() { return new Vec3(x, y, z); }

    @Override
    public void set(float x, float y) { this.x = x; this.y = y; }
    @Override
    public void set(float x, float y, float z) { this.x = x; this.y = y; this.z = z; }
    public Vector3f set(ReadableVector3f src) { this.x = src.getX(); this.y = src.getY(); this.z = src.getZ(); return this; }

    @Override
    public float lengthSquared() { return x * x + y * y + z * z; }
    public Vector3f translate(float x, float y, float z) { this.x += x; this.y += y; this.z += z; return this; }

    @Override
    public Vector negate() { x = -x; y = -y; z = -z; return this; }

    @Override
    public Vector load(FloatBuffer buf) { x = buf.get(); y = buf.get(); z = buf.get(); return this; }
    @Override
    public Vector scale(float scale) { x *= scale; y *= scale; z *= scale; return this; }
    @Override
    public Vector store(FloatBuffer buf) { buf.put(x); buf.put(y); buf.put(z); return this; }

    @Override
    public float getX() { return x; }
    @Override
    public float getY() { return y; }
    @Override
    public float getZ() { return z; }
    @Override
    public void setX(float x) { this.x = x; }
    @Override
    public void setY(float y) { this.y = y; }
    @Override
    public void setZ(float z) { this.z = z; }

    public static Vector3f add(Vector3f left, Vector3f right, Vector3f dest) {
        if (dest == null) dest = new Vector3f();
        dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
        return dest;
    }

    public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest) {
        if (dest == null) dest = new Vector3f();
        dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
        return dest;
    }

    public static Vector3f cross(Vector3f left, Vector3f right, Vector3f dest) {
        if (dest == null) dest = new Vector3f();
        dest.set(
                left.y * right.z - left.z * right.y,
                left.z * right.x - left.x * right.z,
                left.x * right.y - left.y * right.x
        );
        return dest;
    }

    @Override
    public String toString() { return String.format("Vector3f[%.2f, %.2f, %.2f]", x, y, z); }
}