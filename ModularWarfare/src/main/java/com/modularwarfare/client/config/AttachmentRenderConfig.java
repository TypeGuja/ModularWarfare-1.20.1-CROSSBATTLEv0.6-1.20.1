package com.modularwarfare.client.config;

import org.joml.Vector3f;

public class AttachmentRenderConfig {
    public String modelFileName = "";
    public Extra extra = new Extra();
    public Sight sight = new Sight();
    public Grip grip = new Grip();

    public static class Grip {
        public Vector3f leftArmOffset = new Vector3f(0, 0, 0);
    }

    public static class Sight {
        public Vector3f translateSight = new Vector3f(0, 0, 0);
        public Vector3f rotateSight = new Vector3f(0, 0, 0);
    }

    public static class Extra {
        public float modelScale = 1.2f;
    }
}