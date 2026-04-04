package com.modularwarfare.common.vector;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class Matrix3f {
    float[][] matrix = new float[3][3];

    public Matrix3f(float[][] m) {
        for (int i = 0; i < 3; i++) {
            System.arraycopy(m[i], 0, matrix[i], 0, 3);
        }
    }

    public Matrix3f(float m11, float m12, float m13,
                    float m21, float m22, float m23,
                    float m31, float m32, float m33) {
        matrix[0] = new float[]{m11, m12, m13};
        matrix[1] = new float[]{m21, m22, m23};
        matrix[2] = new float[]{m31, m32, m33};
    }

    public Matrix3f mult(Matrix3f m) { return multMatrix(this, m); }
    public Vec3 mult(Vec3 v) { return multVec(this, v); }

    public static Matrix3f getMatrixRotX(float r) {
        float sn = Mth.sin(r);
        float cs = Mth.cos(r);
        return new Matrix3f(1, 0, 0, 0, cs, -sn, 0, sn, cs);
    }

    public static Matrix3f getMatrixRotY(float r) {
        float sn = Mth.sin(r);
        float cs = Mth.cos(r);
        return new Matrix3f(cs, 0, sn, 0, 1, 0, -sn, 0, cs);
    }

    public static Matrix3f getMatrixRotZ(float r) {
        float sn = Mth.sin(r);
        float cs = Mth.cos(r);
        return new Matrix3f(cs, -sn, 0, sn, cs, 0, 0, 0, 1);
    }

    public static Vec3 multVec(Matrix3f m, Vec3 vec) {
        float[] ret = new float[3];
        for (int i = 0; i < 3; i++) {
            float[] row = {m.matrix[i][0], m.matrix[i][1], m.matrix[i][2]};
            float[] col = {(float) vec.x, (float) vec.y, (float) vec.z};
            for (int s = 0; s < 3; s++) {
                ret[i] += row[s] * col[s];
            }
        }
        return new Vec3(ret[0], ret[1], ret[2]);
    }

    public static Matrix3f multMatrix(Matrix3f m1, Matrix3f m2) {
        float[][] ret = new float[3][3];
        for (int i = 0; i < 3; i++) {
            float[] row = {m1.matrix[i][0], m1.matrix[i][1], m1.matrix[i][2]};
            for (int j = 0; j < 3; j++) {
                float[] col = {m2.matrix[0][j], m2.matrix[1][j], m2.matrix[2][j]};
                for (int s = 0; s < 3; s++) {
                    ret[i][j] += row[s] * col[s];
                }
            }
        }
        return new Matrix3f(ret);
    }
}