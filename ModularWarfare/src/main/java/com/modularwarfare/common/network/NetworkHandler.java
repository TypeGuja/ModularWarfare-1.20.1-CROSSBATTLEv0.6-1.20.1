package com.modularwarfare.common.network;

import com.modularwarfare.ModularWarfare;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ModularWarfare.MOD_ID, "main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    private static int packetId = 0;

    public void initialise() {
        registerPacket(PacketGunFire.class, PacketGunFire::new);
        registerPacket(PacketPlaySound.class, PacketPlaySound::new);
        registerPacket(PacketPlayHitmarker.class, PacketPlayHitmarker::new);
        registerPacket(PacketGunSwitchMode.class, PacketGunSwitchMode::new);
        registerPacket(PacketGunReload.class, PacketGunReload::new);
        registerPacket(PacketGunReloadSound.class, PacketGunReloadSound::new);
        registerPacket(PacketGunAddAttachment.class, PacketGunAddAttachment::new);
        registerPacket(PacketGunUnloadAttachment.class, PacketGunUnloadAttachment::new);
        registerPacket(PacketClientAnimation.class, PacketClientAnimation::new);
        registerPacket(PacketGunTrail.class, PacketGunTrail::new);
        registerPacket(PacketAimingRequest.class, PacketAimingRequest::new);
        registerPacket(PacketAimingReponse.class, PacketAimingReponse::new);
        registerPacket(PacketDecal.class, PacketDecal::new);
        registerPacket(PacketSyncBackWeapons.class, PacketSyncBackWeapons::new);
        registerPacket(PacketBulletSnap.class, PacketBulletSnap::new);
        registerPacket(PacketSyncExtraSlot.class, PacketSyncExtraSlot::new);
        registerPacket(PacketOpenGui.class, PacketOpenGui::new);
    }

    private <T extends PacketBase> void registerPacket(Class<T> clazz, Supplier<T> factory) {
        CHANNEL.registerMessage(packetId++, clazz,
                (packet, buffer) -> packet.encodeInto(buffer),
                buffer -> {
                    T packet = factory.get();
                    packet.decodeInto(buffer);
                    return packet;
                },
                (packet, context) -> {
                    context.get().enqueueWork(() -> {
                        if (context.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                            packet.handleServerSide(context.get().getSender());
                        } else {
                            packet.handleClientSide(ModularWarfare.PROXY.getClientPlayer());
                        }
                    });
                    context.get().setPacketHandled(true);
                });
    }

    public void sendToServer(PacketBase packet) {
        CHANNEL.sendToServer(packet);
    }

    public void sendTo(PacketBase packet, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public void sendToAll(PacketBase packet) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }

    public void sendToAllAround(PacketBase packet, double x, double y, double z, float range, ResourceKey<Level> dimension) {
        CHANNEL.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(x, y, z, range, dimension)), packet);
    }

    public void sendToAllTracking(PacketBase packet, Entity entity) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), packet);
    }

    public void sendToDimension(PacketBase packet, ResourceKey<Level> dimension) {
        CHANNEL.send(PacketDistributor.DIMENSION.with(() -> dimension), packet);
    }

    public void handleClientPackets() {
        // Process client packets if needed
    }

    public void handleServerPackets() {
        // Process server packets if needed
    }
}