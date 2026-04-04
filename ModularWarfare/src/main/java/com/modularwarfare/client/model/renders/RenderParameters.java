package com.modularwarfare.client.model.renders;

import com.modularwarfare.client.ClientRenderHooks;

public class RenderParameters {
    public static float adsSwitch = 0.0f;
    public static float sprintSwitch = 0.0f;
    public static float crouchSwitch = 0.0f;
    public static float reloadSwitch = 1.0f;
    public static float attachmentSwitch = 0.0f;
    public static int switchDelay = 20;
    public static float swayVertical = 0.0f;
    public static float swayHorizontal = 0.0f;
    public static Float swayVerticalEP = 0.0f;
    public static Float swayHorizontalEP = 0.0f;
    public static float triggerPullSwitch = 0.0f;
    public static String lastModel = "";
    public static float smoothing = 1.0f;
    public static float GUN_ROT_X = 0.0f;
    public static float GUN_ROT_Y = 0.0f;
    public static float GUN_ROT_Z = 0.0f;
    public static float GUN_ROT_X_LAST = 0.0f;
    public static float GUN_ROT_Y_LAST = 0.0f;
    public static float GUN_ROT_Z_LAST = 0.0f;
    public static float playerRecoilPitch = 0.0f;
    public static float playerRecoilYaw = 0.0f;
    public static float prevPitch = 0.0f;
    public static float antiRecoilPitch = 0.0f;
    public static float antiRecoilYaw = 0.0f;

    public static void resetRenderMods() {
        swayHorizontal = 0.0f;
        swayVertical = 0.0f;
        swayHorizontalEP = 0.0f;
        swayVerticalEP = 0.0f;
        reloadSwitch = 0.0f;
        sprintSwitch = 0.0f;
        adsSwitch = 0.0f;
        crouchSwitch = 0.0f;
        ClientRenderHooks.isAimingScope = false;
        ClientRenderHooks.isAiming = false;
    }
}