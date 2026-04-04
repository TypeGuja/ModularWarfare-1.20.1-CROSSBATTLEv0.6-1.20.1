package com.modularwarfare.utility;

import org.joml.Vector3f;

public class NumberHelper {
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static boolean isNegative(float val) {
        return val < 0.0f;
    }

    public static boolean isTargetMet(float target, float current) {
        if (isNegative(target)) {
            return current <= target;
        }
        return current >= target;
    }

    public static float addTowards(float target, float current, float value) {
        if (isNegative(target)) {
            return current - value;
        }
        return current + value;
    }

    public static float generateInRange(float val) {
        return (float) (Math.random() * val - val / 2.0f);
    }

    public static float determineValue(boolean bool, float value) {
        return bool ? -value : value;
    }

    public static Vector3f addVector(Vector3f left, Vector3f right) {
        return new Vector3f(left.x + right.x, left.y + right.y, left.z + right.z);
    }

    public static Vector3f subtractVector(Vector3f left, Vector3f right) {
        if (right == null || left == null) return new Vector3f();
        return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
    }

    public static Vector3f multiplyVector(Vector3f vector, float amount) {
        return new Vector3f(vector.x * amount, vector.y * amount, vector.z * amount);
    }

    public static Vector3f divideVector(Vector3f vector, float amount) {
        return new Vector3f(vector.x / amount, vector.y / amount, vector.z / amount);
    }

    public static boolean isInRange(float maxValue, float currentValue) {
        return currentValue <= maxValue && currentValue >= -maxValue;
    }
}