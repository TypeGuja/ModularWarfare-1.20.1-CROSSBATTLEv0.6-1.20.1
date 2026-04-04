package nazzy.battle.item;

import nazzy.battle.Battle;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class ItemLedorub {

    public static class RangedItem extends PickaxeItem {

        public RangedItem(Properties properties) {
            super(new Tier() {
                @Override
                public int getUses() {
                    return 1561;
                }

                @Override
                public float getSpeed() {
                    return 8.0F;
                }

                @Override
                public float getAttackDamageBonus() {
                    return 3.0F;
                }

                @Override
                public TagKey<Block> getTag() {
                    return BlockTags.create(new ResourceLocation(Battle.MOD_ID, "mineable/ledorub"));
                }

                @Override
                public int getEnchantmentValue() {
                    return 15;
                }

                @Override
                public Ingredient getRepairIngredient() {
                    return Ingredient.EMPTY;
                }

                @Override
                public int getLevel() {
                    return 3; // Алмазный уровень (3)
                }
            }, 1, -2.8F, properties);
        }
    }
}