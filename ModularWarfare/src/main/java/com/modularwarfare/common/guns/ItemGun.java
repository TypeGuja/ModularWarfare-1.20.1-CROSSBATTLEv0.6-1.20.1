package com.modularwarfare.common.guns;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.ClientRenderHooks;
import com.modularwarfare.client.handler.ClientTickHandler;
import com.modularwarfare.client.model.renders.RenderParameters;
import com.modularwarfare.common.handler.ServerTickHandler;
import com.modularwarfare.common.network.PacketGunFire;
import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemGun extends BaseItem {
    public GunType type;
    public static boolean canDryFire = true;
    public static boolean fireButtonHeld = false;
    public static boolean lastFireButtonHeld = false;
    protected static final UUID MOVEMENT_SPEED_MODIFIER = UUID.fromString("99999999-4180-4865-B01B-BCCE9785ACA3");

    public ItemGun(GunType type) {
        super(type);
        this.type = type;
    }

    @Override
    public void setType(BaseType type) {
        this.type = (GunType) type;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player) {
            if (level.isClientSide) {
                onUpdateClient(player, level, stack, this, type);
            } else {
                onUpdateServer(player, level, stack, this, type);
            }

            if (stack.getTag() == null) {
                var tag = stack.getOrCreateTag();
                tag.putString("firemode", type.fireModes[0].name().toLowerCase());
                tag.putInt("skinId", 0);
                tag.putBoolean("punched", type.isEnergyGun);
            }
        }
    }

    public void onUpdateClient(Player player, Level level, ItemStack stack, ItemGun itemGun, GunType gunType) {
        if (RenderParameters.switchDelay > 0) {
            RenderParameters.switchDelay--;
        }

        if (fireButtonHeld && level.isClientSide) {
            WeaponFireMode fireMode = GunType.getFireMode(stack);
            if (fireMode == WeaponFireMode.FULL) {
                fireClient(player, level, stack, itemGun, fireMode);
            }
        } else if (!lastFireButtonHeld && fireButtonHeld && level.isClientSide) {
            WeaponFireMode fireMode = GunType.getFireMode(stack);
            if (fireMode == WeaponFireMode.SEMI) {
                fireClient(player, level, stack, itemGun, fireMode);
            } else if (fireMode == WeaponFireMode.BURST) {
                var tag = stack.getOrCreateTag();
                int shotsRemaining = tag.contains("shotsremaining") ? tag.getInt("shotsremaining") : 0;
                if (shotsRemaining > 0) {
                    fireClient(player, level, stack, itemGun, fireMode);
                } else if (fireButtonHeld && !lastFireButtonHeld) {
                    fireClient(player, level, stack, itemGun, fireMode);
                }
            }
        }

        lastFireButtonHeld = fireButtonHeld;
    }

    public void onUpdateServer(Player player, Level level, ItemStack stack, ItemGun itemGun, GunType gunType) {
        // Server-side update logic
    }

    public void fireClient(Player player, Level level, ItemStack stack, ItemGun itemGun, WeaponFireMode fireMode) {
        GunType gunType = itemGun.type;

        if (isOnShootCooldown(player.getUUID()) || isClientReloading(player) ||
                ClientRenderHooks.getAnimMachine(player).attachmentMode) {
            return;
        }

        if (!gunType.allowSprintFiring && player.isSprinting()) {
            return;
        }

        if (!gunType.hasFireMode(fireMode)) {
            return;
        }

        int shotCount = 1;
        if (fireMode == WeaponFireMode.BURST) {
            shotCount = stack.getOrCreateTag().getInt("shotsremaining");
            if (shotCount <= 0) {
                shotCount = gunType.numBurstRounds;
            }
        }

        if (!hasNextShot(stack)) {
            if (canDryFire) {
                gunType.playClientSound(player, WeaponSoundType.DryFire);
                gunType.playClientSound(player, WeaponSoundType.FireLast);
                canDryFire = false;
            }
            if (fireMode == WeaponFireMode.BURST) {
                stack.getOrCreateTag().putInt("shotsremaining", 0);
            }
            return;
        }

        ModularWarfare.NETWORK.sendToServer(new PacketGunFire(gunType.internalName, gunType.fireTickDelay,
                gunType.recoilPitch, gunType.recoilYaw, gunType.recoilAimReducer, gunType.bulletSpread));

        ModularWarfare.PROXY.onShootAnimation(player, gunType.internalName, gunType.fireTickDelay,
                this.type.recoilPitch, this.type.recoilYaw);

        canDryFire = true;

        ItemStack barrelAttachment = GunType.getAttachment(stack, AttachmentEnum.Barrel);
        if (barrelAttachment != null && barrelAttachment.getItem() instanceof ItemAttachment attachment) {
            if (attachment.type.barrel.isSuppressor) {
                gunType.playClientSound(player, WeaponSoundType.FireSuppressed);
            } else {
                gunType.playClientSound(player, WeaponSoundType.Fire);
            }
        } else if (GunType.isPackAPunched(stack)) {
            gunType.playClientSound(player, WeaponSoundType.Punched);
            gunType.playClientSound(player, WeaponSoundType.Fire);
        } else {
            gunType.playClientSound(player, WeaponSoundType.Fire);
        }

        if (gunType.weaponType != null &&
                gunType.weaponType != WeaponType.BOLT_SNIPER &&
                gunType.weaponType != WeaponType.SHOTGUN) {
            gunType.playClientSound(player, WeaponSoundType.Pump);
        }

        if (fireMode == WeaponFireMode.BURST) {
            stack.getOrCreateTag().putInt("shotsremaining", --shotCount);
        }

        ClientTickHandler.playerShootCooldown.put(player.getUUID(), gunType.fireTickDelay);
    }

    public void fireServer(Player player, Level level, ItemStack stack, ItemGun itemGun, WeaponFireMode fireMode,
                           int fireTickDelay, float recoilPitch, float recoilYaw, float recoilAimReducer, float bulletSpread) {
        // Server-side fire logic - to be implemented
        if (hasNextShot(stack)) {
            consumeShot(stack);
            // Apply damage to entities, etc.
        }
    }

    public void onGunSwitchMode(Player player, Level level, ItemStack stack, ItemGun itemGun, WeaponFireMode newMode) {
        GunType.setFireMode(stack, newMode);
        itemGun.type.playClientSound(player, WeaponSoundType.ModeSwitch);
    }

    public void playImpactSound(Level level, BlockPos pos, GunType gunType) {
        BlockState state = level.getBlockState(pos);

        if (state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.STONE_ORE_REPLACEABLES)) {
            gunType.playSoundPos(pos, level, WeaponSoundType.ImpactStone, null, 1.0f);
        } else if (state.is(BlockTags.DIRT)) {
            gunType.playSoundPos(pos, level, WeaponSoundType.ImpactDirt, null, 1.0f);
        } else if (state.is(BlockTags.PLANKS) || state.is(BlockTags.WOODEN_FENCES) ||
                state.is(BlockTags.WOODEN_DOORS) || state.is(BlockTags.WOODEN_SLABS) ||
                state.is(BlockTags.WOODEN_STAIRS)) {
            gunType.playSoundPos(pos, level, WeaponSoundType.ImpactWood, null, 1.0f);
        } else if (state.is(BlockTags.ICE) || state.is(BlockTags.WOOL)) {
            String blockName = state.getBlock().getDescriptionId().toLowerCase();
            if (blockName.contains("glass")) {
                gunType.playSoundPos(pos, level, WeaponSoundType.ImpactGlass, null, 1.0f);
            } else {
                gunType.playSoundPos(pos, level, WeaponSoundType.ImpactDirt, null, 1.0f);
            }
        } else if (state.liquid()) {
            gunType.playSoundPos(pos, level, WeaponSoundType.ImpactWater, null, 1.0f);
        } else if (state.is(BlockTags.IRON_ORES) || state.is(BlockTags.COPPER_ORES) ||
                state.getBlock() == Blocks.IRON_BLOCK || state.getBlock() == Blocks.GOLD_BLOCK ||
                state.is(BlockTags.COAL_ORES) || state.is(BlockTags.DIAMOND_ORES)) {
            gunType.playSoundPos(pos, level, WeaponSoundType.ImpactMetal, null, 1.0f);
        } else {
            gunType.playSoundPos(pos, level, WeaponSoundType.ImpactDirt, null, 1.0f);
        }
    }

    public static boolean isOnShootCooldown(UUID uuid) {
        return ClientTickHandler.playerShootCooldown.containsKey(uuid);
    }

    public static boolean isClientReloading(Player player) {
        return ClientTickHandler.playerReloadCooldown.containsKey(player.getUUID());
    }

    public static boolean isServerReloading(Player player) {
        return ServerTickHandler.playerReloadCooldown.containsKey(player.getUUID());
    }

    public static boolean hasAmmoLoaded(ItemStack stack) {
        if (stack.isEmpty()) return false;
        var tag = stack.getTag();
        return tag != null && tag.contains("ammo");
    }

    public static int getMagazineBullets(ItemStack stack) {
        if (!hasAmmoLoaded(stack)) return 0;
        CompoundTag ammoTag = stack.getOrCreateTag().getCompound("ammo");
        ItemStack ammoStack = ItemStack.of(ammoTag);
        if (ammoStack.getItem() instanceof ItemAmmo itemAmmo) {
            var ammoItemTag = ammoStack.getTag();
            if (ammoItemTag != null) {
                String key = itemAmmo.type.magazineCount > 1 ?
                        "ammocount" + ammoItemTag.getInt("magcount") : "ammocount";
                return ammoItemTag.getInt(key);
            }
        }
        return 0;
    }

    public static boolean hasNextShot(ItemStack stack) {
        if (hasAmmoLoaded(stack)) {
            CompoundTag ammoTag = stack.getOrCreateTag().getCompound("ammo");
            ItemStack ammoStack = ItemStack.of(ammoTag);
            if (ammoStack.getItem() instanceof ItemAmmo itemAmmo && ammoStack.hasTag()) {
                var tag = ammoStack.getTag();
                String key = itemAmmo.type.magazineCount > 1 ?
                        "ammocount" + tag.getInt("magcount") : "ammocount";
                int ammoCount = tag.getInt(key);
                return ammoCount > 0;
            }
        } else if (stack.hasTag() && stack.getTag().contains("ammocount")) {
            return stack.getTag().getInt("ammocount") > 0;
        }
        return false;
    }

    public static void consumeShot(ItemStack stack) {
        if (hasAmmoLoaded(stack)) {
            CompoundTag ammoTag = stack.getOrCreateTag().getCompound("ammo");
            ItemStack ammoStack = ItemStack.of(ammoTag);
            if (ammoStack.getItem() instanceof ItemAmmo itemAmmo && ammoStack.hasTag()) {
                var tag = ammoStack.getTag();
                String key = itemAmmo.type.magazineCount > 1 ?
                        "ammocount" + tag.getInt("magcount") : "ammocount";
                tag.putInt(key, tag.getInt(key) - 1);
                stack.getOrCreateTag().put("ammo", ammoStack.save(new CompoundTag()));
            }
        } else if (stack.hasTag() && stack.getTag().contains("ammocount")) {
            int ammoCount = stack.getTag().getInt("ammocount");
            stack.getOrCreateTag().putInt("ammocount", ammoCount - 1);
        }
    }

    public static ItemBullet getUsedBullet(ItemStack stack, GunType gunType) {
        if (gunType.acceptedAmmo != null) {
            return ItemAmmo.getUsedBullet(stack);
        }
        if (gunType.acceptedBullets != null && stack.hasTag() && stack.getTag().contains("bullet")) {
            CompoundTag bulletTag = stack.getTag().getCompound("bullet");
            ItemStack bulletStack = ItemStack.of(bulletTag);
            if (bulletStack.getItem() instanceof ItemBullet bullet) {
                return bullet;
            }
        }
        return null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        GunType gunType = this.type;

        if (hasAmmoLoaded(stack)) {
            CompoundTag ammoTag = stack.getOrCreateTag().getCompound("ammo");
            ItemStack ammoStack = ItemStack.of(ammoTag);
            if (ammoStack.getItem() instanceof ItemAmmo itemAmmo) {
                int currentAmmo = getMagazineBullets(stack);
                tooltip.add(Component.literal("§bAmmo: §f" + currentAmmo + "/" + itemAmmo.type.ammoCapacity));
            }
        }

        WeaponFireMode fireMode = GunType.getFireMode(stack);
        if (fireMode != null) {
            tooltip.add(Component.literal("§bFire Mode: §f" + fireMode));
        }

        tooltip.add(Component.literal("§bDamage: §f" + gunType.gunDamage));
        tooltip.add(Component.literal("§bAccuracy: §f" + String.format("%.0f%%", 1.0f / gunType.bulletSpread * 100.0f)));
    }
}