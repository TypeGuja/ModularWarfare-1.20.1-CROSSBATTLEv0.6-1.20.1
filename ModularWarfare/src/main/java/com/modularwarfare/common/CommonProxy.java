package com.modularwarfare.common;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.handler.EventHandlerEntity;
import com.modularwarfare.common.type.BaseType;
import com.modularwarfare.utility.MWSound;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class CommonProxy extends com.modularwarfare.utility.event.ForgeEvent {
    public static Pattern zipJar = Pattern.compile("(.+).(zip|jar)$");

    public void preload() {}
    public void load() {}
    public void init() {}
    public void forceReload() {}

    public List<File> getContentList(Method method, ClassLoader classloader) {
        ArrayList<File> contentPacks = new ArrayList<>();
        File[] files = ModularWarfare.MOD_DIR.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.getName().contains("cache") &&
                        !file.getName().contains("officialmw") &&
                        !file.getName().contains("highres")) {
                    continue;
                }
                if (file.isDirectory() || zipJar.matcher(file.getName()).matches()) {
                    contentPacks.add(file);
                }
            }
        }
        ModularWarfare.LOGGER.info("Loaded content pack list server side.");
        return contentPacks;
    }

    public <T> T loadModel(String s, String shortName, Class<T> typeClass) { return null; }
    public void reloadModels(boolean reloadSkins) {}
    public void generateJsonModels(ArrayList<BaseType> types) {}
    public void generateJsonSounds(Collection<ItemGun> types, boolean replace) {}
    public void generateLangFiles(ArrayList<BaseType> types, boolean replace) {}
    public void playSound(MWSound sound) {}
    public void playHitmarker(boolean headshot) {}
    public void registerSound(String soundName) {}

    public void onShootAnimation(Player player, String wepType, int fireTickDelay, float recoilPitch, float recoilYaw) {}
    public void onReloadAnimation(Player player, String wepType, int reloadTime, int reloadCount, int reloadType) {}
    public Level getClientWorld() { return null; }
    public Player getClientPlayer() { return null; }
    public void addBlood(LivingEntity living, int amount) {}
    public void addBlood(LivingEntity living, int amount, boolean onhit) {}

    public void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(new EventHandlerEntity());
    }

    public void resetSens() {}
}