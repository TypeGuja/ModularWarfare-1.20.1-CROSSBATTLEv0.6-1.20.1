package com.modularwarfare.client.handler;

import com.modularwarfare.api.WeaponFireEvent;
import com.modularwarfare.api.WeaponReloadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public class ClientWeaponHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onWeaponFire(WeaponFireEvent.Post event) {
        // Client-side weapon fire handling
    }

    @SubscribeEvent
    public void onWeaponReload(WeaponReloadEvent.Pre event) {
        // Client-side weapon reload handling
    }
}