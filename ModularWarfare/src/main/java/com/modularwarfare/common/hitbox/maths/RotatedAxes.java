package com.modularwarfare.common.hitbox.maths;

import com.modularwarfare.common.vector.Vector3f;
import org.joml.Matrix4f;
import org.joml.AxisAngle4f;

public class RotatedAxes {
    private float rotationYaw;
    private float rotationPitch;
    private float rotationRoll;
    private Matrix4f rotationMatrix;

    public RotatedAxes() {
        rotationMatrix = new Matrix4f();
    }

    public RotatedAxes(Matrix4f mat) {
        rotationMatrix = new Matrix4f(mat);
        convertMatrixToAngles();
    }

    public RotatedAxes(float yaw, float pitch, float roll) {
        rotationMatrix = new Matrix4f();
        setAngles(yaw, pitch, roll);
    }

    public RotatedAxes clone() {
        RotatedAxes newAxes = new RotatedAxes();
        newAxes.rotationMatrix.set(rotationMatrix);
        newAxes.convertMatrixToAngles();
        return newAxes;
    }

    public boolean isValid() {
        return Math.abs(rotationMatrix.determinant()) > 1e-6f && !Float.isNaN(rotationMatrix.determinant());
    }

    public void setAngles(float yaw, float pitch, float roll) {
        rotationYaw = yaw;
        rotationPitch = pitch;
        rotationRoll = roll;
        convertAnglesToMatrix();
    }

    public float getYaw() { return rotationYaw; }
    public float getPitch() { return rotationPitch; }
    public float getRoll() { return rotationRoll; }

    public Vector3f getXAxis() {
        return new Vector3f(rotationMatrix.m00(), rotationMatrix.m10(), rotationMatrix.m20());
    }

    public Vector3f getYAxis() {
        return new Vector3f(rotationMatrix.m01(), rotationMatrix.m11(), rotationMatrix.m21());
    }

    public Vector3f getZAxis() {
        return new Vector3f(-rotationMatrix.m02(), -rotationMatrix.m12(), -rotationMatrix.m22());
    }

    public Matrix4f getMatrix() {
        return rotationMatrix;
    }

    public void rotateLocalYaw(float rotateBy) {
        Vector3f axis = getYAxis();
        axis.normalise();
        rotationMatrix.rotate((float) (rotateBy * Math.PI / 180.0), axis.x, axis.y, axis.z);
        convertMatrixToAngles();
    }

    public void rotateLocalPitch(float rotateBy) {
        Vector3f axis = getZAxis();
        axis.normalise();
        rotationMatrix.rotate((float) (rotateBy * Math.PI / 180.0), axis.x, axis.y, axis.z);
        convertMatrixToAngles();
    }

    public void rotateLocalRoll(float rotateBy) {
        Vector3f axis = getXAxis();
        axis.normalise();
        rotationMatrix.rotate((float) (rotateBy * Math.PI / 180.0), axis.x, axis.y, axis.z);
        convertMatrixToAngles();
    }

    public RotatedAxes rotateGlobalYaw(float rotateBy) {
        rotationMatrix.rotate((float) (rotateBy * Math.PI / 180.0), 0, 1, 0);
        convertMatrixToAngles();
        return this;
    }

    public RotatedAxes rotateGlobalPitch(float rotateBy) {
        rotationMatrix.rotate((float) (rotateBy * Math.PI / 180.0), 0, 0, 1);
        convertMatrixToAngles();
        return this;
    }

    public RotatedAxes rotateGlobalRoll(float rotateBy) {
        rotationMatrix.rotate((float) (rotateBy * Math.PI / 180.0), 1, 0, 0);
        convertMatrixToAngles();
        return this;
    }

    public RotatedAxes rotateGlobalYawInRads(float rotateBy) {
        rotationMatrix.rotate(rotateBy, 0, 1, 0);
        convertMatrixToAngles();
        return this;
    }

    public RotatedAxes rotateGlobalPitchInRads(float rotateBy) {
        rotationMatrix.rotate(rotateBy, 0, 0, 1);
        convertMatrixToAngles();
        return this;
    }

    public RotatedAxes rotateGlobalRollInRads(float rotateBy) {
        rotationMatrix.rotate(rotateBy, 1, 0, 0);
        convertMatrixToAngles();
        return this;
    }

    public Vector3f findGlobalVectorLocally(Vector3f in) {
        Matrix4f mat = new Matrix4f();
        mat.m00(in.x);
        mat.m10(in.y);
        mat.m20(in.z);
        mat.rotate((float) (-rotationYaw * Math.PI / 180.0), 0, 1, 0);
        mat.rotate((float) (-rotationPitch * Math.PI / 180.0), 0, 0, 1);
        mat.rotate((float) (-rotationRoll * Math.PI / 180.0), 1, 0, 0);
        return new Vector3f(mat.m00(), mat.m10(), mat.m20());
    }

    public Vector3f findLocalVectorGlobally(Vector3f in) {
        Matrix4f mat = new Matrix4f();
        mat.m00(in.x);
        mat.m10(in.y);
        mat.m20(in.z);
        mat.rotate((float) (rotationRoll * Math.PI / 180.0), 1, 0, 0);
        mat.rotate((float) (rotationPitch * Math.PI / 180.0), 0, 0, 1);
        mat.rotate((float) (rotationYaw * Math.PI / 180.0), 0, 1, 0);
        return new Vector3f(mat.m00(), mat.m10(), mat.m20());
    }

    private void convertAnglesToMatrix() {
        rotationMatrix = new Matrix4f();
        rotationMatrix.rotate((float) (rotationRoll * Math.PI / 180.0), 1, 0, 0);
        rotationMatrix.rotate((float) (rotationPitch * Math.PI / 180.0), 0, 0, 1);
        rotationMatrix.rotate((float) (rotationYaw * Math.PI / 180.0), 0, 1, 0);
        convertMatrixToAngles();
    }

    private void convertMatrixToAngles() {
        rotationYaw = (float) Math.atan2(rotationMatrix.m20(), rotationMatrix.m00()) * 180.0f / (float) Math.PI;
        rotationPitch = (float) Math.atan2(-rotationMatrix.m10(),
                Math.sqrt(rotationMatrix.m12() * rotationMatrix.m12() + rotationMatrix.m11() * rotationMatrix.m11())) * 180.0f / (float) Math.PI;
        rotationRoll = (float) Math.atan2(rotationMatrix.m12(), rotationMatrix.m11()) * 180.0f / (float) Math.PI;
    }

    public RotatedAxes findLocalAxesGlobally(RotatedAxes in) {
        Matrix4f mat = new Matrix4f(in.getMatrix());
        mat.rotate((float) (rotationRoll * Math.PI / 180.0), 1, 0, 0);
        mat.rotate((float) (rotationPitch * Math.PI / 180.0), 0, 0, 1);
        mat.rotate((float) (rotationYaw * Math.PI / 180.0), 0, 1, 0);
        return new RotatedAxes(mat);
    }

    @Override
    public String toString() {
        return String.format("RotatedAxes[Yaw = %.2f, Pitch = %.2f, Roll = %.2f]", getYaw(), getPitch(), getRoll());
    }
}