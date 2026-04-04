package com.modularwarfare.utility.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class ForgeEvent {
    public ForgeEvent() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void register(IEventBus bus) {
        // Регистрация событий
    }
}