package com.modularwarfare.client.anim;

import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.guns.WeaponSoundType;
import com.modularwarfare.common.guns.WeaponType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class StateEntry {
    public float stateTime = 0.0f;
    public float currentValue = 0.0f;
    public float lastValue = 0.0f;
    public StateType stateType;
    private MathType mathType;
    private float minValue;
    private float incrementValue;
    private float startingValue;
    private float operationCount;
    public float cutOffTime;
    public boolean finished = false;
    public static float smoothing = 1.0f;

    public StateEntry(StateType stateType, float stateTime, float startingValue, MathType mathType) {
        this(stateType, stateTime, startingValue, mathType, 1);
    }

    public StateEntry(StateType stateType, float stateTime, float startingValue, MathType mathType, int operationCount) {
        this.stateTime = stateTime;
        this.currentValue = this.lastValue = startingValue;
        this.startingValue = this.lastValue;
        this.mathType = mathType;
        this.stateType = stateType;
        this.minValue = 0.0f;
        this.incrementValue = 1.0f;
        this.operationCount = operationCount;
    }

    public void onTick(float reloadTime) {
        this.lastValue = this.currentValue;

        if (this.mathType == MathType.Add) {
            this.currentValue += this.incrementValue * smoothing / (reloadTime * this.stateTime) * this.operationCount;
        } else if (this.mathType == MathType.Sub) {
            this.currentValue -= this.incrementValue * smoothing / (reloadTime * this.stateTime) * this.operationCount;
        }

        this.currentValue = Math.max(this.minValue, Math.min(this.currentValue, 0.999f));

        if ((this.currentValue >= 1.0f || this.currentValue <= 0.0f) && this.operationCount > 1) {
            this.currentValue = this.startingValue;
            this.operationCount -= 1.0f;

            Player player = Minecraft.getInstance().player;
            if (player != null) {
                ItemStack mainHand = player.getMainHandItem();
                if (!mainHand.isEmpty() && mainHand.getItem() instanceof ItemGun gun) {
                    if (gun.type != null && gun.type.weaponType == WeaponType.SHOTGUN) {
                        gun.type.playClientSound(player, WeaponSoundType.BulletLoad);
                    }
                }
            }
        }
    }

    public static enum MathType {
        Add,
        Sub;
    }
}