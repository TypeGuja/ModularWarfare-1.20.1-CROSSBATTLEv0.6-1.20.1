package com.modularwarfare.common.guns;

import com.google.gson.Gson;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.ClientProxy;
import com.modularwarfare.client.config.GunRenderConfig;
import com.modularwarfare.client.model.ModelGun;
import com.modularwarfare.common.network.PacketPlaySound;
import com.modularwarfare.common.type.BaseType;
import com.modularwarfare.objects.SoundEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GunType extends BaseType {
    public WeaponType weaponType;
    public WeaponScopeType scopeType = WeaponScopeType.DEFAULT;
    public float gunDamage = 0.0f;
    public float moveSpeedModifier = 1.0f;
    public float gunDamageHeadshotBonus = 0.0f;
    public int weaponMaxRange = 200;
    public int weaponEffectiveRange = 50;
    public int numBullets = 1;
    public float bulletSpread;
    public int roundsPerMin = 1;
    public transient int fireTickDelay = 0;
    public int numBurstRounds = 3;
    public boolean isEnergyGun = false;
    public float recoilPitch = 10.0f;
    public float recoilYaw = 1.0f;
    public float accuracySneakFactor = 0.75f;
    public float randomRecoilPitch = 0.5f;
    public float randomRecoilYaw = 0.5f;
    public float recoilAimReducer = 0.8f;
    public WeaponFireMode[] fireModes = new WeaponFireMode[]{WeaponFireMode.SEMI};
    public HashMap<AttachmentEnum, ArrayList<String>> acceptedAttachments;
    public int reloadTime = 40;
    public Integer offhandReloadTime;
    public String[] acceptedAmmo;
    public boolean dynamicAmmo = false;
    public Integer internalAmmoStorage;
    public String[] acceptedBullets;
    public boolean allowSprintFiring = true;
    public boolean allowDefaultSounds = true;
    public Vec3 shellEjectOffsetNormal = new Vec3(-1.0, 0.0, 1.0);
    public Vec3 shellEjectOffsetAiming = new Vec3(0.0, 0.12, 1.0);
    private SoundEntry[] weaponSounds;
    public float emptyPitch = 0.05f;
    public HashMap<WeaponSoundType, ArrayList<SoundEntry>> weaponSoundMap;

    @Override
    public void loadExtraValues() {
        if (maxStackSize == null) {
            maxStackSize = 1;
        }
        loadBaseValues();
        fireTickDelay = 1200 / roundsPerMin;

        try {
            if (weaponSoundMap != null) {
                for (ArrayList<SoundEntry> entryList : weaponSoundMap.values()) {
                    for (SoundEntry soundEntry : entryList) {
                        if (soundEntry.soundName != null) {
                            ModularWarfare.PROXY.registerSound(soundEntry.soundName);
                            if (soundEntry.soundNameDistant != null) {
                                ModularWarfare.PROXY.registerSound(soundEntry.soundNameDistant);
                            }
                        } else {
                            ModularWarfare.LOGGER.error(String.format("Sound entry event '%s' has null soundName for type '%s'",
                                    soundEntry.soundEvent, this.internalName));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reloadModel() {
        this.model = new ModelGun(ModularWarfare.getRenderConfig(this, new Gson(), GunRenderConfig.class), this);
    }

    public void playClientSound(Player player, WeaponSoundType weaponSoundType) {
        if (weaponSoundMap != null && weaponSoundMap.containsKey(weaponSoundType)) {
            for (SoundEntry soundEntry : weaponSoundMap.get(weaponSoundType)) {
                if (soundEntry.soundName != null) {
                    // Альтернативный способ для 1.20.1
                    net.minecraft.resources.ResourceLocation soundLocation =
                            net.minecraft.resources.ResourceLocation.tryBuild(ModularWarfare.MOD_ID, soundEntry.soundName);

                    if (soundLocation != null) {
                        net.minecraft.sounds.SoundEvent soundEvent =
                                net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS.getValue(soundLocation);

                        if (soundEvent != null) {
                            player.level().playSound(player, player.blockPosition(),
                                    soundEvent, SoundSource.PLAYERS, 1.0f, 1.0f);
                        }
                    }
                }
            }
        }
    }

    public void playSoundPos(BlockPos pos, Level level, WeaponSoundType weaponSoundType) {
        playSoundPos(pos, level, weaponSoundType, null, 1.0f);
    }

    public void playSoundPos(BlockPos pos, Level level, WeaponSoundType weaponSoundType, @Nullable Player excluded, float volume) {
        if (weaponSoundType == null) return;

        if (weaponSoundMap != null && weaponSoundMap.containsKey(weaponSoundType)) {
            Random random = new Random();
            for (SoundEntry soundEntry : weaponSoundMap.get(weaponSoundType)) {
                int soundRange = soundEntry.soundRange != null ? soundEntry.soundRange : weaponSoundType.defaultRange;

                for (Player hearingPlayer : level.players()) {
                    double distance = hearingPlayer.blockPosition().distSqr(pos);
                    if (distance <= soundRange * soundRange) {
                        if (excluded != null && hearingPlayer.equals(excluded)) continue;

                        float volumeMultiplier = (float) (soundRange / 16.0) * soundEntry.soundVolumeMultiplier * volume;
                        float pitch = random.nextFloat() / soundEntry.soundRandomPitch + soundEntry.soundPitch;

                        if (hearingPlayer instanceof ServerPlayer serverPlayer) {
                            ModularWarfare.NETWORK.sendTo(new PacketPlaySound(pos, soundEntry.soundName, volumeMultiplier, pitch), serverPlayer);
                        }
                    }
                }
            }
        } else if (allowDefaultSounds && weaponSoundType.defaultSound != null) {
            Random random = new Random();
            String soundName = weaponSoundType.defaultSound;
            float soundRange = weaponSoundType.defaultRange;

            for (Player hearingPlayer : level.players()) {
                double distance = hearingPlayer.blockPosition().distSqr(pos);
                if (distance <= soundRange * soundRange) {
                    float volumeMultiplier = soundRange / 16.0f;
                    float pitch = random.nextFloat() / 5.0f + 1.0f;

                    if (hearingPlayer instanceof ServerPlayer serverPlayer) {
                        ModularWarfare.NETWORK.sendTo(new PacketPlaySound(pos, soundName, volumeMultiplier, pitch), serverPlayer);
                    }
                }
            }
        }
    }

    public void playSound(LivingEntity entity, WeaponSoundType weaponSoundType, ItemStack gunStack) {
        playSound(entity, weaponSoundType, gunStack, null);
    }

    public void playSound(LivingEntity entity, WeaponSoundType weaponSoundType, ItemStack gunStack, @Nullable Player excluded) {
        if (weaponSoundType == null) return;

        BlockPos pos = entity.blockPosition();
        Level level = entity.level();

        if (weaponSoundMap != null && weaponSoundMap.containsKey(weaponSoundType)) {
            Random random = new Random();
            for (SoundEntry soundEntry : weaponSoundMap.get(weaponSoundType)) {
                int soundRange = soundEntry.soundRange != null ? soundEntry.soundRange : weaponSoundType.defaultRange;

                if (soundEntry.soundNameDistant != null && soundEntry.soundMaxRange != null) {
                    int maxSoundRange = soundEntry.soundMaxRange;

                    for (Player hearingPlayer : level.players()) {
                        double distance = hearingPlayer.blockPosition().distSqr(pos);
                        if (excluded != null && hearingPlayer.equals(excluded)) continue;

                        String soundName;
                        float volume;

                        if (distance <= soundRange * soundRange) {
                            soundName = soundEntry.soundName;
                            volume = (float) ((distance + maxSoundRange / 6.0) / 16.0) * soundEntry.soundVolumeMultiplier;
                        } else if (distance <= maxSoundRange * maxSoundRange) {
                            soundName = soundEntry.soundNameDistant;
                            volume = (float) ((distance + maxSoundRange / 6.0) / 16.0) * soundEntry.soundFarVolumeMultiplier;
                        } else {
                            continue;
                        }

                        float customPitch = random.nextFloat() / soundEntry.soundRandomPitch + soundEntry.soundPitch;
                        float modifyPitch = 0.0f;

                        if (ItemGun.getMagazineBullets(gunStack) <= 5 && emptyPitch > 0.0f) {
                            modifyPitch = 0.3f - emptyPitch * ItemGun.getMagazineBullets(gunStack);
                            customPitch += modifyPitch;
                        }

                        if (hearingPlayer instanceof ServerPlayer serverPlayer) {
                            ModularWarfare.NETWORK.sendTo(new PacketPlaySound(pos, soundName, volume, customPitch), serverPlayer);
                        }
                    }
                } else {
                    for (Player hearingPlayer : level.players()) {
                        double distance = hearingPlayer.blockPosition().distSqr(pos);
                        if (distance <= soundRange * soundRange) {
                            if (excluded != null && hearingPlayer.equals(excluded)) continue;

                            float volumeMultiplier = (float) (soundRange / 16.0) * soundEntry.soundVolumeMultiplier;
                            float pitch = random.nextFloat() / soundEntry.soundRandomPitch + soundEntry.soundPitch;

                            if (hearingPlayer instanceof ServerPlayer serverPlayer) {
                                ModularWarfare.NETWORK.sendTo(new PacketPlaySound(pos, soundEntry.soundName, volumeMultiplier, pitch), serverPlayer);
                            }
                        }
                    }
                }
            }
        } else if (allowDefaultSounds && weaponSoundType.defaultSound != null) {
            Random random = new Random();
            String soundName = weaponSoundType.defaultSound;
            float soundRange = weaponSoundType.defaultRange;

            for (Player hearingPlayer : level.players()) {
                double distance = hearingPlayer.blockPosition().distSqr(pos);
                if (distance <= soundRange * soundRange) {
                    float volumeMultiplier = soundRange / 16.0f;
                    float pitch = random.nextFloat() / 5.0f + 1.0f;

                    if (hearingPlayer instanceof ServerPlayer serverPlayer) {
                        ModularWarfare.NETWORK.sendTo(new PacketPlaySound(pos, soundName, volumeMultiplier, pitch), serverPlayer);
                    }
                }
            }
        }
    }

    public boolean hasFireMode(WeaponFireMode fireMode) {
        if (fireModes != null) {
            for (WeaponFireMode mode : fireModes) {
                if (mode == fireMode) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPackAPunched(ItemStack heldStack) {
        if (heldStack.hasTag() && heldStack.getTag().contains("punched")) {
            return heldStack.getTag().getBoolean("punched");
        }
        return false;
    }

    public static void setPackAPunched(ItemStack heldStack, boolean bool) {
        heldStack.getOrCreateTag().putBoolean("punched", bool);
    }

    public static WeaponFireMode getFireMode(ItemStack heldStack) {
        if (heldStack.hasTag() && heldStack.getTag().contains("firemode")) {
            return WeaponFireMode.fromString(heldStack.getTag().getString("firemode"));
        }
        return null;
    }

    public static void setFireMode(ItemStack heldStack, WeaponFireMode fireMode) {
        heldStack.getOrCreateTag().putString("firemode", fireMode.name().toLowerCase());
    }

    public static ItemStack getAttachment(ItemStack heldStack, AttachmentEnum type) {
        if (heldStack.hasTag()) {
            String key = "attachment_" + type.typeName;
            if (heldStack.getTag().contains(key)) {
                CompoundTag tag = heldStack.getTag().getCompound(key);
                return ItemStack.of(tag);
            }
        }
        return null;
    }

    public static void addAttachment(ItemStack heldStack, AttachmentEnum type, ItemStack attachment) {
        heldStack.getOrCreateTag().put("attachment_" + type.typeName, attachment.save(new CompoundTag()));
    }

    public static void removeAttachment(ItemStack heldStack, AttachmentEnum type) {
        if (heldStack.hasTag()) {
            heldStack.getTag().remove("attachment_" + type.typeName);
        }
    }

    @Override
    public String getAssetDir() {
        return "guns";
    }
}