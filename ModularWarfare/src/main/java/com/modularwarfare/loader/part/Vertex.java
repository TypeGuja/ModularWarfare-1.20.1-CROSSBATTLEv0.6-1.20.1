package com.modularwarfare.loader.part;

public class Vertex {
    public float x;
    public float y;
    public float z;

    public Vertex(float x, float y) {
        this(x, y, 0.0f);
    }

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex copy() {
        return new Vertex(x, y, z);
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vertex normalize() {
        float len = length();
        if (len != 0.0f) {
            x /= len;
            y /= len;
            z /= len;
        }
        return this;
    }

    public static Vertex add(Vertex a, Vertex b) {
        return new Vertex(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vertex sub(Vertex a, Vertex b) {
        return new Vertex(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static float dot(Vertex a, Vertex b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vertex cross(Vertex a, Vertex b) {
        return new Vertex(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x
        );
    }

    @Override
    public String toString() {
        return String.format("Vertex[%.4f, %.4f, %.4f]", x, y, z);
    }
}