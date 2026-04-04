package nazzy.battle.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import nazzy.battle.Battle;
import nazzy.battle.block.entity.ShopBlockEntity;
import nazzy.battle.init.InitMenus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

import java.util.HashMap;

public class GuiSHOP_Gui_Buy3 {

    public static class GuiContainerMod extends AbstractContainerMenu {
        public Level level;
        public int x, y, z;
        public Player player;

        public GuiContainerMod(int id, Level level, int x, int y, int z, Player player) {
            super(InitMenus.SHOP_BUY3_MENU.get(), id);
            this.level = level;
            this.x = x;
            this.y = y;
            this.z = z;
            this.player = player;
        }

        @Override
        public ItemStack quickMoveStack(Player player, int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }
    }

    public static class GuiWindow extends AbstractContainerScreen<GuiContainerMod> {
        private Level world;
        private int x, y, z;
        private Player entity;

        public GuiWindow(GuiContainerMod container, Inventory inventory, Component title) {
            super(container, inventory, title);
            this.world = container.level;
            this.x = container.x;
            this.y = container.y;
            this.z = container.z;
            this.entity = container.player;
            this.imageWidth = 176;
            this.imageHeight = 166;
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            this.renderBackground(graphics);
            super.render(graphics, mouseX, mouseY, partialTicks);
            this.renderTooltip(graphics, mouseX, mouseY);
        }

        @Override
        protected void init() {
            super.init();
            int leftPos = this.leftPos;
            int topPos = this.topPos;
            BlockPos blockPos = new BlockPos(x, y, z);

            // Back to main menu button
            this.addRenderableWidget(Button.builder(Component.literal("x"), (button) -> {
                nazzy.battle.procedure.ProcedureSHOPgui.executeProcedure(new HashMap<>() {{
                    put("entity", entity);
                    put("x", x);
                    put("y", y);
                    put("z", z);
                    put("world", world);
                }});
            }).bounds(leftPos + 160, topPos + 2, 10, 20).build());

            // Buy Medkit
            this.addRenderableWidget(Button.builder(Component.literal(" "), (button) -> {
                nazzy.battle.procedure.ProcedureBuyMedkit.executeProcedure(new HashMap<>() {{
                    put("entity", entity);
                    put("x", x);
                    put("y", y);
                    put("z", z);
                    put("world", world);
                }});
            }).bounds(leftPos + 124, topPos + 43, 30, 20).build());

            // Buy Claymore
            this.addRenderableWidget(Button.builder(Component.literal("  "), (button) -> {
                nazzy.battle.procedure.ProcedureBuyClaymore.executeProcedure(new HashMap<>() {{
                    put("entity", entity);
                    put("x", x);
                    put("y", y);
                    put("z", z);
                    put("world", world);
                }});
            }).bounds(leftPos + 124, topPos + 70, 30, 20).build());

            // Buy Sandbags
            this.addRenderableWidget(Button.builder(Component.literal("   "), (button) -> {
                nazzy.battle.procedure.ProcedureBuySandbags.executeProcedure(new HashMap<>() {{
                    put("entity", entity);
                    put("x", x);
                    put("y", y);
                    put("z", z);
                    put("world", world);
                }});
            }).bounds(leftPos + 124, topPos + 97, 30, 20).build());

            // Buy Knife
            this.addRenderableWidget(Button.builder(Component.literal("    "), (button) -> {
                nazzy.battle.procedure.ProcedureBuyKnife.executeProcedure(new HashMap<>() {{
                    put("entity", entity);
                    put("x", x);
                    put("y", y);
                    put("z", z);
                    put("world", world);
                }});
            }).bounds(leftPos + 124, topPos + 124, 30, 20).build());

            // Back to buy2 button
            this.addRenderableWidget(Button.builder(Component.literal("<"), (button) -> {
                nazzy.battle.procedure.ProcedureShopGui_buy2.executeProcedure(new HashMap<>() {{
                    put("entity", entity);
                    put("x", x);
                    put("y", y);
                    put("z", z);
                    put("world", world);
                }});
            }).bounds(leftPos + 142, topPos + 2, 10, 20).build());
        }

        @Override
        protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int leftPos = this.leftPos;
            int topPos = this.topPos;

            graphics.blit(new ResourceLocation(Battle.MOD_ID, "textures/currency_text.png"), leftPos - 2, topPos + 2, 0, 0, 256, 256, 256, 256);
            graphics.blit(new ResourceLocation(Battle.MOD_ID, "textures/vignette.png"), leftPos - 2, topPos - 1, 0, 0, 256, 256, 256, 256);
            graphics.blit(new ResourceLocation(Battle.MOD_ID, "textures/buy_texture.png"), leftPos + 19, topPos - 3, 0, 0, 256, 256, 256, 256);
            graphics.blit(new ResourceLocation(Battle.MOD_ID, "textures/buy_3.png"), leftPos - 58, topPos + 19, 0, 0, 256, 256, 256, 256);
        }

        @Override
        protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
            BlockEntity tileEntity = world.getBlockEntity(new BlockPos(x, y, z));
            int money = 0;
            if (tileEntity instanceof ShopBlockEntity shop) {
                money = (int) shop.getMoney();
            }
            graphics.drawString(this.font, "" + money, 13, 4, 0xFFFFFF, false);
            graphics.drawString(this.font, "=   119", 79, 43, 0xFFFFFF, false);
            graphics.drawString(this.font, "=   99", 79, 70, 0xFFFFFF, false);
            graphics.drawString(this.font, "=   25", 79, 97, 0xFFFFFF, false);
            graphics.drawString(this.font, "=   149", 79, 130, 0xFFFFFF, false);
            graphics.drawString(this.font, Component.translatable("Аптечка"), 10, 45, 0xC8C8C8, false);
            graphics.drawString(this.font, Component.translatable("Мина \"Клеймор\""), 7, 79, 0xC8C8C8, false);
            graphics.drawString(this.font, Component.translatable("Мешки с песком"), 7, 115, 0xC8C8C8, false);
            graphics.drawString(this.font, Component.translatable("Нож"), 28, 150, 0xC8C8C8, false);
            graphics.drawString(this.font, "$", 136, 48, 0xE04040, false);
            graphics.drawString(this.font, "$", 131, 75, 0xE04040, false);
            graphics.drawString(this.font, "$", 136, 102, 0xE04040, false);
            graphics.drawString(this.font, "$", 136, 129, 0xE04040, false);
        }
    }
}