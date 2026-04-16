package com.modularwarfare.client;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.anim.AnimStateMachine;
import com.modularwarfare.client.model.objects.CustomItemRenderType;
import com.modularwarfare.client.model.objects.CustomItemRenderer;
import com.modularwarfare.client.model.renders.RenderAmmo;
import com.modularwarfare.client.model.renders.RenderAttachment;
import com.modularwarfare.client.model.renders.RenderGunStatic;
import com.modularwarfare.client.model.renders.RenderParameters;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.network.BackWeaponsManager;
import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public class ClientRenderHooks {
    public static HashMap<LivingEntity, AnimStateMachine> weaponAnimations = new HashMap<>();
    private Minecraft mc = Minecraft.getInstance();
    public static CustomItemRenderer[] customRenderers = new CustomItemRenderer[6];
    public float partialTicks;
    public static boolean isAimingScope;
    public static boolean isAiming;

    public ClientRenderHooks() {
        ClientProxy.gunStaticRenderer = new RenderGunStatic();
        customRenderers[0] = ClientProxy.gunStaticRenderer;
        ClientProxy.ammoRenderer = new RenderAmmo();
        customRenderers[1] = ClientProxy.ammoRenderer;
        ClientProxy.attachmentRenderer = new RenderAttachment();
        customRenderers[2] = ClientProxy.attachmentRenderer;
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            RenderParameters.smoothing = event.renderTickTime;
            this.partialTicks = event.renderTickTime;
        }
    }

    @SubscribeEvent
    public void renderHand(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) return;

        ItemStack heldItem = mc.player.getMainHandItem();

        if (heldItem.isEmpty()) return;

        if (heldItem.getItem() instanceof ItemGun) {
            event.setCanceled(true);

            // ВАЖНО: Получаем AnimStateMachine
            AnimStateMachine anim = getAnimMachine(mc.player);

            ModularWarfare.LOGGER.info("RenderHand: rendering gun with anim=" + anim);

            if (ClientProxy.gunStaticRenderer != null) {
                ClientProxy.gunStaticRenderer.renderItem(
                        CustomItemRenderType.EQUIPPED_FIRST_PERSON,
                        heldItem,
                        mc.level,
                        mc.player,
                        anim,                   // <-- ВАЖНО!
                        event.getPoseStack(),
                        event.getMultiBufferSource(),
                        event.getPackedLight()
                );
            } else {
                ModularWarfare.LOGGER.error("gunStaticRenderer is null!");
            }
        }
    }

    @SubscribeEvent
    public void renderPlayer(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();

        if (player instanceof net.minecraft.client.player.AbstractClientPlayer abstractClientPlayer) {
            ItemStack backWeapon = BackWeaponsManager.INSTANCE.getItemToRender(abstractClientPlayer);

            if (!backWeapon.isEmpty() && backWeapon.getItem() instanceof BaseItem baseItem) {
                BaseType type = baseItem.baseType;

                event.getPoseStack().pushPose();
                if (type != null && type.id < customRenderers.length && customRenderers[type.id] != null) {
                    customRenderers[type.id].renderItem(
                            CustomItemRenderType.BACK,
                            backWeapon,
                            mc.level,
                            player,
                            partialTicks
                    );
                }
                event.getPoseStack().popPose();
            }
        }
    }

    public static AnimStateMachine getAnimMachine(Player player) {
        return weaponAnimations.computeIfAbsent(player, k -> new AnimStateMachine());
    }
}