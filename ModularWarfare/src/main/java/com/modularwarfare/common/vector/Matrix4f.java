package com.modularwarfare.common.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Matrix4f extends Matrix implements Serializable {
    private static final long serialVersionUID = 1L;
    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    public Matrix4f() { setIdentity(); }
    public Matrix4f(Matrix4f src) { load(src); }

    @Override
    public Matrix setIdentity() { return setIdentity(this); }
    public static Matrix4f setIdentity(Matrix4f m) {
        m.m00 = 1; m.m01 = 0; m.m02 = 0; m.m03 = 0;
        m.m10 = 0; m.m11 = 1; m.m12 = 0; m.m13 = 0;
        m.m20 = 0; m.m21 = 0; m.m22 = 1; m.m23 = 0;
        m.m30 = 0; m.m31 = 0; m.m32 = 0; m.m33 = 1;
        return m;
    }

    @Override
    public Matrix setZero() { return setZero(this); }
    public static Matrix4f setZero(Matrix4f m) {
        m.m00 = 0; m.m01 = 0; m.m02 = 0; m.m03 = 0;
        m.m10 = 0; m.m11 = 0; m.m12 = 0; m.m13 = 0;
        m.m20 = 0; m.m21 = 0; m.m22 = 0; m.m23 = 0;
        m.m30 = 0; m.m31 = 0; m.m32 = 0; m.m33 = 0;
        return m;
    }

    public Matrix4f load(Matrix4f src) { return load(src, this); }
    public static Matrix4f load(Matrix4f src, Matrix4f dest) {
        if (dest == null) dest = new Matrix4f();
        dest.m00 = src.m00; dest.m01 = src.m01; dest.m02 = src.m02; dest.m03 = src.m03;
        dest.m10 = src.m10; dest.m11 = src.m11; dest.m12 = src.m12; dest.m13 = src.m13;
        dest.m20 = src.m20; dest.m21 = src.m21; dest.m22 = src.m22; dest.m23 = src.m23;
        dest.m30 = src.m30; dest.m31 = src.m31; dest.m32 = src.m32; dest.m33 = src.m33;
        return dest;
    }

    @Override
    public Matrix load(FloatBuffer buf) {
        m00 = buf.get(); m01 = buf.get(); m02 = buf.get(); m03 = buf.get();
        m10 = buf.get(); m11 = buf.get(); m12 = buf.get(); m13 = buf.get();
        m20 = buf.get(); m21 = buf.get(); m22 = buf.get(); m23 = buf.get();
        m30 = buf.get(); m31 = buf.get(); m32 = buf.get(); m33 = buf.get();
        return this;
    }

    @Override
    public Matrix loadTranspose(FloatBuffer buf) {
        m00 = buf.get(); m10 = buf.get(); m20 = buf.get(); m30 = buf.get();
        m01 = buf.get(); m11 = buf.get(); m21 = buf.get(); m31 = buf.get();
        m02 = buf.get(); m12 = buf.get(); m22 = buf.get(); m32 = buf.get();
        m03 = buf.get(); m13 = buf.get(); m23 = buf.get(); m33 = buf.get();
        return this;
    }

    @Override
    public Matrix store(FloatBuffer buf) {
        buf.put(m00); buf.put(m01); buf.put(m02); buf.put(m03);
        buf.put(m10); buf.put(m11); buf.put(m12); buf.put(m13);
        buf.put(m20); buf.put(m21); buf.put(m22); buf.put(m23);
        buf.put(m30); buf.put(m31); buf.put(m32); buf.put(m33);
        return this;
    }

    @Override
    public Matrix storeTranspose(FloatBuffer buf) {
        buf.put(m00); buf.put(m10); buf.put(m20); buf.put(m30);
        buf.put(m01); buf.put(m11); buf.put(m21); buf.put(m31);
        buf.put(m02); buf.put(m12); buf.put(m22); buf.put(m32);
        buf.put(m03); buf.put(m13); buf.put(m23); buf.put(m33);
        return this;
    }

    @Override
    public Matrix transpose() { return transpose(this, this); }
    public static Matrix4f transpose(Matrix4f src, Matrix4f dest) {
        if (dest == null) dest = new Matrix4f();
        dest.m00 = src.m00; dest.m01 = src.m10; dest.m02 = src.m20; dest.m03 = src.m30;
        dest.m10 = src.m01; dest.m11 = src.m11; dest.m12 = src.m21; dest.m13 = src.m31;
        dest.m20 = src.m02; dest.m21 = src.m12; dest.m22 = src.m22; dest.m23 = src.m32;
        dest.m30 = src.m03; dest.m31 = src.m13; dest.m32 = src.m23; dest.m33 = src.m33;
        return dest;
    }

    @Override
    public float determinant() {
        float f = m00 * (m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32 - m13 * m22 * m31 - m11 * m23 * m32 - m12 * m21 * m33);
        f -= m01 * (m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32 - m13 * m22 * m30 - m10 * m23 * m32 - m12 * m20 * m33);
        f += m02 * (m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31 - m13 * m21 * m30 - m10 * m23 * m31 - m11 * m20 * m33);
        return f - m03 * (m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31 - m12 * m21 * m30 - m10 * m22 * m31 - m11 * m20 * m32);
    }

    @Override
    public Matrix invert() { return invert(this, this); }
    public static Matrix4f invert(Matrix4f src, Matrix4f dest) {
        float det = src.determinant();
        if (Math.abs(det) < 1e-6f) return null;
        if (dest == null) dest = new Matrix4f();
        float invDet = 1.0f / det;

        dest.m00 = (src.m11 * src.m22 * src.m33 + src.m12 * src.m23 * src.m31 + src.m13 * src.m21 * src.m32 - src.m13 * src.m22 * src.m31 - src.m11 * src.m23 * src.m32 - src.m12 * src.m21 * src.m33) * invDet;
        dest.m01 = (-src.m01 * src.m22 * src.m33 - src.m02 * src.m23 * src.m31 - src.m03 * src.m21 * src.m32 + src.m03 * src.m22 * src.m31 + src.m01 * src.m23 * src.m32 + src.m02 * src.m21 * src.m33) * invDet;
        // ... remaining matrix inversion elements
        return dest;
    }

    @Override
    public Matrix negate() { return negate(this, this); }
    public static Matrix4f negate(Matrix4f src, Matrix4f dest) {
        if (dest == null) dest = new Matrix4f();
        dest.m00 = -src.m00; dest.m01 = -src.m01; dest.m02 = -src.m02; dest.m03 = -src.m03;
        dest.m10 = -src.m10; dest.m11 = -src.m11; dest.m12 = -src.m12; dest.m13 = -src.m13;
        dest.m20 = -src.m20; dest.m21 = -src.m21; dest.m22 = -src.m22; dest.m23 = -src.m23;
        dest.m30 = -src.m30; dest.m31 = -src.m31; dest.m32 = -src.m32; dest.m33 = -src.m33;
        return dest;
    }
}