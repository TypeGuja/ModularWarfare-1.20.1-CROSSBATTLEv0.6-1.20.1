package nazzy.battle.item;

import nazzy.battle.Battle;
import nazzy.battle.procedure.ProcedureMedkitUsed;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class ItemMedkit {

    public static class ItemFoodCustom extends Item {

        public ItemFoodCustom(Properties properties) {
            super(properties.food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationMod(0.3f)
                    .alwaysEat()
                    .build()));
        }

        @Override
        public UseAnim getUseAnimation(ItemStack stack) {
            return UseAnim.DRINK;
        }

        @Override
        public int getUseDuration(ItemStack stack) {
            return 32;
        }

        @Override
        public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
            if (!level.isClientSide() && entity instanceof Player) {
                HashMap<String, Object> dependencies = new HashMap<>();
                dependencies.put("entity", entity);
                ProcedureMedkitUsed.executeProcedure(dependencies);
            }
            return super.finishUsingItem(stack, level, entity);
        }
    }
}