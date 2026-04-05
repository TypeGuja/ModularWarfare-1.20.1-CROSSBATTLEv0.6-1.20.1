package com.modularwarfare;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.modularwarfare.client.ClientProxy;
import com.modularwarfare.client.config.GunRenderConfig;
import com.modularwarfare.client.model.ModelGun;
import com.modularwarfare.common.CommonProxy;
import com.modularwarfare.common.MWTab;
import com.modularwarfare.common.entity.ModEntities;
import com.modularwarfare.common.guns.*;
import com.modularwarfare.common.handler.PlayerHandler;
import com.modularwarfare.common.network.NetworkHandler;
import com.modularwarfare.common.protector.ModularProtector;
import com.modularwarfare.common.type.ContentTypes;
import com.modularwarfare.common.type.BaseType;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
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

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static Map<String, RegistryObject<Item>> gunRegistry = new HashMap<>();
    public static Map<String, RegistryObject<Item>> ammoRegistry = new HashMap<>();
    public static Map<String, RegistryObject<Item>> attachmentRegistry = new HashMap<>();

    public ModularWarfare() {
        INSTANCE = this;
        ModConfig.init();

        loadAndRegisterItems();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITIES.register(modEventBus);
        MWTab.TABS.register(modEventBus);
        ITEMS.register(modEventBus);

        PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        MOD_DIR = new File(FMLPaths.CONFIGDIR.get().toFile().getParentFile(), "ModularWarfare");
        if (!MOD_DIR.exists()) {
            MOD_DIR.mkdirs();
            LOGGER.info("Created ModularWarfare folder");
        }

        ContentTypes.registerTypes();
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void loadAndRegisterItems() {
        LOGGER.info("Loading and registering items from JSON...");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // ТОЛЬКО AK-47 ДЛЯ ТЕСТА
        String[] gunFiles = {
                "guns/prototype.ak47.json"
        };

        for (String file : gunFiles) {
            try (InputStream is = ModularWarfare.class.getClassLoader().getResourceAsStream(file)) {
                if (is != null) {
                    String json = new String(is.readAllBytes());
                    String internalName = file.substring(file.lastIndexOf('/') + 1, file.lastIndexOf('.'));

                    GunType gunType = gson.fromJson(json, GunType.class);
                    gunType.internalName = internalName;
                    if (gunType.displayName == null) gunType.displayName = internalName;
                    if (gunType.maxStackSize == null) gunType.maxStackSize = 1;

                    // ========== КЛЮЧЕВОЕ ИСПРАВЛЕНИЕ: ЗАГРУЖАЕМ RENDER CONFIG ==========
                    String renderFilePath = file.replace(".json", ".render.json");
                    try (InputStream renderIs = ModularWarfare.class.getClassLoader().getResourceAsStream(renderFilePath)) {
                        if (renderIs != null) {
                            String renderJson = new String(renderIs.readAllBytes());
                            GunRenderConfig renderConfig = gson.fromJson(renderJson, GunRenderConfig.class);
                            gunType.model = new ModelGun(renderConfig, gunType);
                            LOGGER.info("✅ Loaded render config for: " + internalName);
                            LOGGER.info("   modelFileName: " + renderConfig.modelFileName);
                        } else {
                            LOGGER.warn("❌ No render config for " + internalName);
                        }
                    } catch (Exception e) {
                        LOGGER.error("Failed to load render config for " + internalName, e);
                    }
                    // =================================================================

                    // ТЕПЕРЬ ВЫЗЫВАЕМ loadExtraValues - модель уже есть, не перезапишется
                    gunType.loadExtraValues();

                    final GunType finalGunType = gunType;
                    RegistryObject<Item> registryObject = ITEMS.register(internalName,
                            () -> new ItemGun(finalGunType));

                    gunRegistry.put(internalName, registryObject);
                    LOGGER.info("✅ Registered gun: " + internalName);
                }
            } catch (Exception e) {
                LOGGER.error("Failed to register gun from " + file, e);
            }
        }

        // Патроны для AK-47
        String[] ammoFiles = {
                "ammo/prototype.ak47ammo.json"
        };

        for (String file : ammoFiles) {
            try (InputStream is = ModularWarfare.class.getClassLoader().getResourceAsStream(file)) {
                if (is != null) {
                    String internalName = file.substring(file.lastIndexOf('/') + 1, file.lastIndexOf('.'));

                    AmmoType ammoType = gson.fromJson(new InputStreamReader(is), AmmoType.class);
                    ammoType.internalName = internalName;
                    if (ammoType.displayName == null) ammoType.displayName = internalName;
                    if (ammoType.maxStackSize == null) ammoType.maxStackSize = 64;

                    final AmmoType finalAmmoType = ammoType;
                    RegistryObject<Item> registryObject = ITEMS.register(internalName,
                            () -> new ItemAmmo(finalAmmoType));

                    ammoRegistry.put(internalName, registryObject);
                    LOGGER.info("✅ Registered ammo: " + internalName);
                }
            } catch (Exception e) {
                LOGGER.error("Failed to register ammo from " + file, e);
            }
        }

        LOGGER.info("Registration complete. Guns: " + gunRegistry.size() + ", Ammo: " + ammoRegistry.size());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NETWORK = new NetworkHandler();
            NETWORK.initialise();
            PLAYERHANDLER = new PlayerHandler();
            MOD_TAB = MWTab.MOD_TAB.get();
        });
        PROXY.registerEventHandlers();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        PROXY.load();
        PROXY.init();
    }

    public static void loadContentPacks(boolean reload) {}

    public static <T> T getRenderConfig(BaseType baseType, Gson gson, Class<T> typeClass) {
        // ПЫТАЕМСЯ ЗАГРУЗИТЬ РЕАЛЬНЫЙ КОНФИГ
        try {
            String configPath = String.format("%s/%s.render.json", baseType.getAssetDir(), baseType.internalName);
            InputStream is = ModularWarfare.class.getClassLoader().getResourceAsStream(configPath);
            if (is != null) {
                String json = new String(is.readAllBytes());
                LOGGER.info("Loaded render config from: " + configPath);
                return gson.fromJson(json, typeClass);
            }
        } catch (Exception e) {
            LOGGER.warn("Could not load render config for " + baseType.internalName);
        }

        // ФОЛБЭК - пустой конфиг
        try {
            return typeClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    // ======================= КОНФИГ =======================
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
                        INSTANCE = config != null ? config : new ModConfig();
                    }
                } else {
                    INSTANCE = new ModConfig();
                }
                saveConfig(configFile, gson, INSTANCE);
            } catch (Exception e) {
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