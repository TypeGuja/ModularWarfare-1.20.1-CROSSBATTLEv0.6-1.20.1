package nazzy.battle.item;

import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class KnifeProjectile extends AbstractArrow {

    public KnifeProjectile(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public KnifeProjectile(Level level, LivingEntity shooter) {
        super(EntityType.ARROW, shooter, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        Entity shooter = this.getOwner();

        if (target instanceof LivingEntity livingTarget) {
            float damage = 8.0F;
            livingTarget.hurt(this.damageSources().arrow(this, shooter), damage);

            if (livingTarget.getAirSupply() > 0) {
                livingTarget.setAirSupply(livingTarget.getAirSupply() - 1);
            }
        }

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.discard();
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}