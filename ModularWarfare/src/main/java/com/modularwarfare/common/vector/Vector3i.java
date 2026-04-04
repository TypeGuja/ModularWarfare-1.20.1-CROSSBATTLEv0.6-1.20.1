package com.modularwarfare.common.vector;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Vector3i extends Vector implements Serializable {
    private static final long serialVersionUID = 1L;
    public int x, y, z;

    public Vector3i() {}
    public Vector3i(int x, int y, int z) { set(x, y, z); }
    public Vector3i(Vec3 vec) { this((int) vec.x, (int) vec.y, (int) vec.z); }
    public Vector3i(BlockPos pos) { this(pos.getX(), pos.getY(), pos.getZ()); }

    public Vec3 toVec3() { return new Vec3(x, y, z); }
    public void set(int x, int y, int z) { this.x = x; this.y = y; this.z = z; }

    @Override
    public float lengthSquared() { return x * x + y * y + z * z; }
    public Vector3i translate(int x, int y, int z) { this.x += x; this.y += y; this.z += z; return this; }

    @Override
    public Vector negate() { x = -x; y = -y; z = -z; return this; }

    @Override
    public Vector load(FloatBuffer buf) { x = (int) buf.get(); y = (int) buf.get(); z = (int) buf.get(); return this; }
    @Override
    public Vector scale(float scale) { x = (int) (x * scale); y = (int) (y * scale); z = (int) (z * scale); return this; }
    @Override
    public Vector store(FloatBuffer buf) { buf.put(x); buf.put(y); buf.put(z); return this; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setZ(int z) { this.z = z; }

    @Override
    public String toString() { return String.format("Vector3i[%d, %d, %d]", x, y, z); }
}