package com.modularwarfare.client.config;

import com.modularwarfare.api.WeaponAnimations;
import com.modularwarfare.client.model.objects.RenderVariables;
import com.modularwarfare.common.guns.AttachmentEnum;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

public class GunRenderConfig {
    public String modelFileName = "";
    public Arms arms = new Arms();
    public Sprint sprint = new Sprint();
    public ThirdPerson thirdPerson = new ThirdPerson();
    public Aim aim = new Aim();
    public Bolt bolt = new Bolt();
    public Attachments attachments = new Attachments();
    public Maps maps = new Maps();
    public Extra extra = new Extra();

    public static class Extra {
        public Vector3f translateAll = new Vector3f(1.0f, -1.02f, -0.07f);
        public float modelScale = 1.2f;
        public String reloadAnimation = WeaponAnimations.RIFLE;
        public boolean needExtraChargeModel = false;
        public float chargeHandleDistance = 0.0f;
        public float gunOffsetScoping = 0.0f;
        public float crouchZoom = -0.035f;
        public float adsSpeed = 0.02f;
        public float gunSlideDistance = 0.25f;
        public float modelRecoilBackwards = 0.15f;
        public float modelRecoilUpwards = 1.0f;
        public float modelRecoilShake = 0.5f;
    }

    public static class Maps {
        public HashMap<String, RenderVariables> ammoMap = new HashMap<>();
        public HashMap<String, RenderVariables> bulletMap = new HashMap<>();
    }

    public static class Attachments {
        public HashMap<AttachmentEnum, ArrayList<Vector3f>> attachmentPointMap = new HashMap<>();
        public Vector3f attachmentModeRotate = new Vector3f(10.0f, 30.0f, 0.0f);
        public boolean scopeIsOnSlide = false;
    }

    public static class Bolt {
        public float boltRotation = 0.0f;
        public Vector3f boltRotationPoint = new Vector3f();
        public Vector3f chargeModifier = new Vector3f(0.3f, 0.0f, 0.0f);
        public float pumpHandleDistance = 0.25f;
    }

    public static class Aim {
        public Vector3f rotateHipPosition = new Vector3f(0, 0, 0);
        public Vector3f translateHipPosition = new Vector3f(0, 0, 0);
        public Vector3f rotateAimPosition = new Vector3f(0, 0.065f, 0.3f);
        public Vector3f translateAimPosition = new Vector3f(0.14f, 0.01f, 0);
    }

    public static class ThirdPerson {
        public Vector3f thirdPersonOffset = new Vector3f(0, -0.1f, 0);
        public Vector3f backPersonOffset = new Vector3f(0, 0, 0);
        public float thirdPersonScale = 0.8f;
    }

    public static class Sprint {
        public Vector3f sprintRotate = new Vector3f(-20.0f, 30.0f, 0);
        public Vector3f sprintTranslate = new Vector3f(0.5f, -0.1f, -0.65f);
    }

    public static class Arms {
        public boolean leftHandAmmo = true;
        public EnumArm actionArm = EnumArm.Left;
        public EnumAction actionType = EnumAction.Charge;
        public LeftArm leftArm = new LeftArm();
        public RightArm rightArm = new RightArm();

        public enum EnumAction { Bolt, Pump, Charge }
        public enum EnumArm { Left, Right }

        public class RightArm {
            public Vector3f armScale = new Vector3f(0.8f, 0.8f, 0.8f);
            public Vector3f armPos = new Vector3f(0.26f, -0.65f, 0);
            public Vector3f armRot = new Vector3f(0, 0, -90.0f);
            public Vector3f armReloadPos = new Vector3f(0.27f, -0.65f, 0.04f);
            public Vector3f armReloadRot = new Vector3f(0, 0, -90.0f);
            public Vector3f armChargePos = new Vector3f(0.47f, -0.39f, 0.14f);
            public Vector3f armChargeRot = new Vector3f(0, 0, -90.0f);
        }

        public class LeftArm {
            public Vector3f armScale = new Vector3f(0.8f, 0.8f, 0.8f);
            public Vector3f armPos = new Vector3f(0.25f, -0.59f, 0.06f);
            public Vector3f armRot = new Vector3f(65.0f, 32.0f, -46.0f);
            public Vector3f armReloadPos = new Vector3f(-0.1f, -0.65f, 0.02f);
            public Vector3f armReloadRot = new Vector3f(35.0f, 0, -25.0f);
            public Vector3f armChargePos = new Vector3f(0, 0, 0);
            public Vector3f armChargeRot = new Vector3f(0, 0, 0);
        }
    }
}