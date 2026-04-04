package nazzy.battle.item;

import nazzy.battle.Battle;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemKnife {

    public static class RangedItem extends BowItem {

        public RangedItem(Properties properties) {
            super(properties);
        }

        @Override
        public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
            int i = this.getUseDuration(stack) - timeLeft;
            float f = getPowerForTime(i);

            if (!level.isClientSide() && entity instanceof Player player) {
                if (f >= 0.1) {
                    KnifeProjectile projectile = new KnifeProjectile(level, player);
                    projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
                    projectile.setBaseDamage(6.0);

                    level.addFreshEntity(projectile);

                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.SNOWBALL_THROW, player.getSoundSource(), 1.0F, 1.0F);

                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                }
            }
        }

        public static float getPowerForTime(int time) {
            float f = (float)time / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;
            if (f > 1.0F) {
                f = 1.0F;
            }
            return f;
        }

        @Override
        public int getUseDuration(ItemStack stack) {
            return 72000;
        }
    }
}