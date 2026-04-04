package com.modularwarfare;

import com.modularwarfare.common.MWTab;
import com.modularwarfare.common.entity.ModEntities;
import com.modularwarfare.common.guns.ItemAmmo;
import com.modularwarfare.common.guns.ItemAttachment;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.type.BaseType;
import com.mojang.logging.LogUtils;
import com.modularwarfare.client.ClientProxy;
import com.modularwarfare.common.CommonProxy;
import com.modularwarfare.common.armor.ItemMWArmor;
import com.modularwarfare.common.backpacks.ItemBackpack;
import com.modularwarfare.common.handler.PlayerHandler;
import com.modularwarfare.common.network.NetworkHandler;
import com.modularwarfare.common.protector.ModularProtector;
import com.modularwarfare.common.type.ContentTypes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Mod(ModularWarfare.MOD_ID)
public class ModularWarfare {
    public static final String MOD_ID = "modularwarfare";
    public static final String MOD_NAME = "ModularWarfare";
    public static final String MOD_VERSION = "1.0.1f";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ModularWarfare INSTANCE;
    public static CommonProxy PROXY;
    public static boolean DEV_ENV = true;
    public static CreativeModeTab MOD_TAB;
    public static NetworkHandler NETWORK;
    public static ModularProtector PROTECTOR;
    public static File MOD_DIR;
    public static PlayerHandler PLAYERHANDLER;
    public static ModConfig CONFIG;

    public static Map<String, ItemGun> gunTypes = new HashMap<>();
    public static Map<String, ItemAmmo> ammoTypes = new HashMap<>();
    public static Map<String, ItemAttachment> attachmentTypes = new HashMap<>();
    public static Map<String, ItemMWArmor> armorTypes = new HashMap<>();
    public static Map<String, ItemBackpack> backpackTypes = new HashMap<>();

    public ModularWarfare() {
        INSTANCE = this;

        ModConfig.init();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITIES.register(modEventBus);
        MWTab.TABS.register(modEventBus);

        PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        MOD_DIR = new File(FMLPaths.CONFIGDIR.get().toFile().getParentFile(), "ModularWarfare");
        if (!MOD_DIR.exists()) {
            MOD_DIR.mkdirs();
            LOGGER.info("Created ModularWarfare folder, it's recommended to install content packs.");
            LOGGER.info("As the mod itself doesn't come with any content.");
        }

        ContentTypes.registerTypes();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NETWORK = new NetworkHandler();
            NETWORK.initialise();
            PLAYERHANDLER = new PlayerHandler();
            MOD_TAB = MWTab.MOD_TAB.get();
        });
        PROXY.registerEventHandlers();
        loadContentPacks(false);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        PROXY.load();
        PROXY.init();
    }

    public static void loadContentPacks(boolean reload) {
        loadContent();
    }

    private static void loadContent() {
    }

    public static <T> T getRenderConfig(BaseType baseType, Gson gson, Class<T> typeClass) {
        return null;
    }

    // ======================= ВНУТРЕННИЙ КЛАСС КОНФИГА =======================
    public static class ModConfig {
        public static ModConfig INSTANCE;

        public boolean enableHitmarker = true;
        public boolean enableModifiedInventory = true;
        public boolean enableDynamicCrosshair = true;
        public boolean showAmmoCount = true;
        public boolean dropBulletCasing = true;
        public int despawnTimeShellCasing = 10;
        public boolean canShotBreakGlass = false;
        public boolean dropExtraSlotsOnDeath = true;
        public boolean kickIfModifiedContentPack = true;
        public boolean applyKnockback = false;
        public boolean autoDownloadContentpack = true;
        public String version = "1.0.1f";

        public static void init() {
            File configDir = FMLPaths.CONFIGDIR.get().toFile();
            File configFile = new File(configDir, "modularwarfare.json");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try {
                if (configFile.exists()) {
                    try (Reader reader = new FileReader(configFile)) {
                        ModConfig config = gson.fromJson(reader, ModConfig.class);
                        if (config != null) {
                            INSTANCE = config;
                        } else {
                            INSTANCE = new ModConfig();
                            saveConfig(configFile, gson, INSTANCE);
                        }
                    }
                } else {
                    INSTANCE = new ModConfig();
                    saveConfig(configFile, gson, INSTANCE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                INSTANCE = new ModConfig();
            }
        }

        private static void saveConfig(File configFile, Gson gson, ModConfig config) throws IOException {
            try (Writer writer = new FileWriter(configFile)) {
                gson.toJson(config, writer);
            }
        }
    }
}