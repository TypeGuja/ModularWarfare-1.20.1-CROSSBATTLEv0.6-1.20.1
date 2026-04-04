package com.modularwarfare.client;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.api.WeaponAnimations;
import com.modularwarfare.client.handler.ClientTickHandler;
import com.modularwarfare.client.handler.KeyInputHandler;
import com.modularwarfare.client.handler.RenderGuiHandler;
import com.modularwarfare.client.hud.AttachmentUI;
import com.modularwarfare.client.hud.GunUI;
import com.modularwarfare.client.model.animations.*;
import com.modularwarfare.client.model.layers.RenderLayerBackpack;
import com.modularwarfare.client.model.layers.RenderLayerBody;
import com.modularwarfare.client.model.renders.RenderAmmo;
import com.modularwarfare.client.model.renders.RenderAttachment;
import com.modularwarfare.client.model.renders.RenderGunStatic;
import com.modularwarfare.client.scope.ScopeUtils;
import com.modularwarfare.common.CommonProxy;
import com.modularwarfare.common.extra.ItemLight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ModularWarfare.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy extends CommonProxy {

    public static RenderGunStatic gunStaticRenderer;
    public static RenderAmmo ammoRenderer;
    public static RenderAttachment attachmentRenderer;
    public static ScopeUtils scopeUtils;
    public static ItemLight itemLight;
    public static ClientRenderHooks renderHooks;
    public static AttachmentUI attachmentUI;
    public static GunUI gunUI;

    @Override
    public void preload() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void load() {
        new KeyInputHandler();
        new ClientTickHandler();
        new RenderGuiHandler();

        renderHooks = new ClientRenderHooks();
        MinecraftForge.EVENT_BUS.register(renderHooks);

        // ScopeUtils создаётся, но не инициализирует OpenGL до первого использования
        scopeUtils = new ScopeUtils();
        MinecraftForge.EVENT_BUS.register(scopeUtils);

        attachmentUI = new AttachmentUI();
        MinecraftForge.EVENT_BUS.register(attachmentUI);

        gunUI = new GunUI();
        MinecraftForge.EVENT_BUS.register(gunUI);

        registerAnimations();
    }

    private void registerAnimations() {
        WeaponAnimations.registerAnimation(WeaponAnimations.RIFLE, new AnimationRifle());
        WeaponAnimations.registerAnimation(WeaponAnimations.RIFLE2, new AnimationRifle2());
        WeaponAnimations.registerAnimation(WeaponAnimations.RIFLE3, new AnimationRifle3());
        WeaponAnimations.registerAnimation(WeaponAnimations.RIFLE4, new AnimationRifle4());
        WeaponAnimations.registerAnimation(WeaponAnimations.PISTOL, new AnimationPistol());
        WeaponAnimations.registerAnimation(WeaponAnimations.SHOTGUN, new AnimationShotgun());
        WeaponAnimations.registerAnimation(WeaponAnimations.SNIPER, new AnimationSniperBottom());
        WeaponAnimations.registerAnimation(WeaponAnimations.SNIPER_TOP, new AnimationSniperTop());
        WeaponAnimations.registerAnimation(WeaponAnimations.SIDE_CLIP, new AnimationSideClip());
        WeaponAnimations.registerAnimation(WeaponAnimations.TOP_RIFLE, new AnimationTopRifle());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            gunStaticRenderer = new RenderGunStatic();
            ammoRenderer = new RenderAmmo();
            attachmentRenderer = new RenderAttachment();

            Minecraft mc = Minecraft.getInstance();

            PlayerRenderer defaultRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getSkinMap().get("default");
            if (defaultRenderer != null) {
                defaultRenderer.addLayer(new RenderLayerBackpack(defaultRenderer));
                defaultRenderer.addLayer(new RenderLayerBody(defaultRenderer));
            }

            PlayerRenderer slimRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getSkinMap().get("slim");
            if (slimRenderer != null) {
                slimRenderer.addLayer(new RenderLayerBackpack(slimRenderer));
                slimRenderer.addLayer(new RenderLayerBody(slimRenderer));
            }
        });
    }
}