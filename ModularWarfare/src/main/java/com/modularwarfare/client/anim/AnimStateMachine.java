package com.modularwarfare.client.anim;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.api.WeaponAnimations;
import com.modularwarfare.client.model.ModelGun;
import com.modularwarfare.common.guns.GunType;
import com.modularwarfare.common.guns.WeaponSoundType;
import com.modularwarfare.common.network.PacketGunReloadSound;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class AnimStateMachine {
    public boolean reloading = false;
    private float reloadTime;
    private ReloadType reloadType;
    private float reloadProgress = 0.0f;
    private ArrayList<StateEntry> reloadStateEntries;
    private StateEntry currentReloadState;
    private int reloadStateIndex = 0;

    public boolean tiltHold = false;
    public boolean attachmentMode = false;
    public boolean shooting = false;
    private float shootTime;
    private float shootProgress = 0.0f;
    private ArrayList<StateEntry> shootStateEntries;
    private StateEntry currentShootState;
    private int shootStateIndex = 0;

    public float gunRecoil = 0.0f;
    public float lastGunRecoil = 0.0f;
    public float gunSlide = 0.0f;
    public float lastGunSlide = 0.0f;
    public float hammerRotation = 0.0f;
    public int timeUntilPullback = 0;
    public float gunPullback = -1.0f;
    public float lastGunPullback = -1.0f;
    public boolean isFired = false;
    public ItemStack cachedAmmoStack;
    public int reloadAmmoCount = 1;
    public boolean isGunEmpty = false;
    public int muzzleFlashTime = 0;
    public int flashInt = 0;
    public boolean hasPlayedReloadSound = false;
    public boolean wasSprinting = false;

    public void onTickUpdate() {
        Minecraft mc = Minecraft.getInstance();

        if (reloading) {
            disableSprinting(true, mc);
            if (mc.player != null) {
                mc.player.setSprinting(false);
            }

            if (currentReloadState == null) {
                currentReloadState = reloadStateEntries.get(0);
            }

            if (currentReloadState.stateType == StateType.Tilt) {
                tiltHold = true;
            }
            if (currentReloadState.stateType == StateType.Untilt) {
                tiltHold = false;
            }

            if (reloadProgress >= currentReloadState.cutOffTime && reloadStateIndex + 1 < reloadStateEntries.size()) {
                reloadStateIndex++;
                currentReloadState.finished = true;
                currentReloadState = reloadStateEntries.get(reloadStateIndex);
            }

            reloadProgress += 1.0f / reloadTime;

            if (reloadProgress >= 0.8f) {
                isGunEmpty = false;
                disableSprinting(false, mc);
                if (mc.player != null) {
                    mc.player.setSprinting(wasSprinting);
                }
            }

            if (reloadProgress >= 1.0f) {
                reloading = false;
                reloadProgress = 0.0f;
                reloadStateEntries = null;
                currentReloadState = null;
                reloadStateIndex = 0;
                reloadType = null;
            }

            if (!hasPlayedReloadSound) {
                ModularWarfare.NETWORK.sendToServer(new PacketGunReloadSound(WeaponSoundType.Load));
                hasPlayedReloadSound = true;
            }
        }

        if (shooting) {
            if (currentShootState == null) {
                currentShootState = shootStateEntries.get(0);
            }

            if (shootProgress >= currentShootState.cutOffTime && shootStateIndex + 1 < shootStateEntries.size()) {
                shootStateIndex++;
                currentShootState.finished = true;
                currentShootState = shootStateEntries.get(shootStateIndex);
            }

            shootProgress += 1.0f / shootTime;

            if (shootProgress >= 1.0f) {
                shooting = false;
                shootProgress = 0.0f;
                shootStateEntries = null;
                currentShootState = null;
                shootStateIndex = 0;
            }
        }

        lastGunSlide = gunSlide;

        if (isGunEmpty) {
            gunSlide = 0.5f;
            lastGunSlide = 0.5f;
        }

        if (!isGunEmpty && gunSlide > 0.9) {
            gunSlide -= 0.1f;
        } else if (gunSlide > 0.0f && !isGunEmpty) {
            gunSlide *= 0.5f;
        }

        lastGunRecoil = gunRecoil;
        if (gunRecoil > 0.0f) {
            gunRecoil *= 0.5f;
        }

        if (isFired) {
            gunPullback += 0.5f;
            if (gunPullback >= 0.999f) {
                isFired = false;
            }
        }

        if (timeUntilPullback > 0) {
            timeUntilPullback--;
            if (timeUntilPullback == 0) {
                isFired = true;
                gunPullback = -1.0f;
                lastGunPullback = -1.0f;
            }
        } else {
            hammerRotation *= 0.6f;
        }

        if (muzzleFlashTime > 0) {
            muzzleFlashTime--;
        }
    }

    public void onRenderTickUpdate() {
        if (reloading && currentReloadState != null) {
            currentReloadState.onTick(reloadTime);
        }
        if (shooting && currentShootState != null) {
            currentShootState.onTick(shootTime);
        }
    }

    public void triggerShoot(ModelGun model, GunType gunType, int fireTickDelay) {
        Random r = new Random();
        gunRecoil = 1.0f;
        lastGunRecoil = 1.0f;
        gunSlide = 1.0f;
        lastGunSlide = 1.0f;
        hammerRotation = model.hammerAngle;
        timeUntilPullback = model.hammerDelay;
        muzzleFlashTime = 2;
        flashInt = r.nextInt(5) + 1;

        ArrayList<StateEntry> animEntries = WeaponAnimations.getAnimation(model.config.extra.reloadAnimation)
                .getShootStates(model, gunType);

        if (animEntries != null && animEntries.size() > 0) {
            shootStateEntries = adjustTiming(animEntries);
            shooting = true;
            shootTime = fireTickDelay;
        }
    }

    public void triggerReload(int reloadTime, int reloadCount, ModelGun model, ReloadType reloadType, boolean wasSprinting) {
        ArrayList<StateEntry> animEntries = WeaponAnimations.getAnimation(model.config.extra.reloadAnimation)
                .getReloadStates(reloadType, reloadCount);

        if (animEntries == null) return;

        reloadStateEntries = adjustTiming(animEntries);

        float time;
        if (reloadType == ReloadType.Full) {
            time = (float) reloadTime * 0.65f;
        } else {
            time = reloadTime;
        }

        this.reloadTime = time;
        this.reloadType = reloadType;
        this.reloading = true;
        this.hasPlayedReloadSound = false;
        this.wasSprinting = wasSprinting;
        this.reloadAmmoCount = reloadCount;
    }

    public Optional<StateEntry> getReloadState() {
        return Optional.ofNullable(currentReloadState);
    }

    public boolean isReloadState(StateType stateType) {
        return currentReloadState != null && currentReloadState.stateType == stateType;
    }

    public Optional<StateEntry> getShootState() {
        return Optional.ofNullable(currentShootState);
    }

    public boolean isShootState(StateType stateType) {
        return currentShootState != null && currentShootState.stateType == stateType;
    }

    public boolean shouldRenderAmmo() {
        if (reloading) {
            switch (reloadType) {
                case Load:
                    Optional<StateEntry> loadState = getState(StateType.Load);
                    return loadState.isPresent() && loadState.get().currentValue >= 1.0f;
                case Unload:
                    Optional<StateEntry> unloadState = getState(StateType.Unload);
                    return unloadState.isPresent() && unloadState.get().currentValue >= 1.0f;
                default:
                    return true;
            }
        }
        return true;
    }

    public boolean isReloadType(ReloadType type) {
        return reloadType != null && reloadType == type;
    }

    private ArrayList<StateEntry> adjustTiming(ArrayList<StateEntry> animEntries) {
        float currentTiming = 0.0f;
        for (StateEntry entry : animEntries) {
            currentTiming += entry.stateTime;
        }

        float dividedAmount = 0.0f;
        if (currentTiming < 1.0f) {
            dividedAmount = (1.0f - currentTiming) / animEntries.size();
        }

        if (dividedAmount > 0.0f) {
            for (StateEntry entry : animEntries) {
                entry.stateTime += dividedAmount;
            }
        }

        float cutOffTime = 0.0f;
        for (StateEntry entry : animEntries) {
            entry.cutOffTime = cutOffTime += entry.stateTime;
        }

        return animEntries;
    }

    private Optional<StateEntry> getState(StateType stateType) {
        if (reloadStateEntries == null) {
            return Optional.empty();
        }

        for (StateEntry entry : reloadStateEntries) {
            if (entry.stateType == stateType) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    private static void disableSprinting(boolean bool, Minecraft mc) {
        // Просто запрещаем спринт через флаги игрока
        if (mc.player != null) {
            if (bool) {
                mc.player.setSprinting(false);
            }
        }
    }
}