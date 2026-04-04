package com.modularwarfare.objects;

import com.modularwarfare.common.guns.WeaponSoundType;

public class SoundEntry {
    public WeaponSoundType soundEvent;
    public String soundName;
    public int soundDelay = 0;
    public float soundVolumeMultiplier = 1.0f;
    public float soundFarVolumeMultiplier = 1.0f;
    public float soundPitch = 1.0f;
    public float soundRandomPitch = 5.0f;
    public Integer soundRange;
    public String soundNameDistant;
    public Integer soundMaxRange;
}