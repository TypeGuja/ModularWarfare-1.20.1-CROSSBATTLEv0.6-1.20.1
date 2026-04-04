package com.modularwarfare.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class NonDumbAxisABB {
    public double minX, minY, minZ, maxX, maxY, maxZ;

    public NonDumbAxisABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public NonDumbAxisABB(AABB axis) {
        this(axis.minX, axis.minY, axis.minZ, axis.maxX, axis.maxY, axis.maxZ);
    }

    public NonDumbAxisABB(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    public AABB toAABB() {
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public boolean intersects(NonDumbAxisABB other) {
        return this.intersects(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }

    public boolean intersects(double x1, double y1, double z1, double x2, double y2, double z2) {
        return this.minX < x2 && this.maxX > x1 && this.minY < y2 && this.maxY > y1 && this.minZ < z2 && this.maxZ > z1;
    }

    public boolean contains(Vec3 vec) {
        return vec.x >= minX && vec.x <= maxX && vec.y >= minY && vec.y <= maxY && vec.z >= minZ && vec.z <= maxZ;
    }
}