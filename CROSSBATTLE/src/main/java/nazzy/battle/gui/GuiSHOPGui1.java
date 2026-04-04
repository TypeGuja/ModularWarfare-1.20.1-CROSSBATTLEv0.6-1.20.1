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

public class GuiSHOPGui1 {

    private static final ResourceLocation CURRENCY_TEXT = ResourceLocation.tryBuild(Battle.MOD_ID, "textures/currency_text.png");
    private static final ResourceLocation SHOP_TEXTURE = ResourceLocation.tryBuild(Battle.MOD_ID, "textures/shop_texture.png");
    private static final ResourceLocation VIGNETTE = ResourceLocation.tryBuild(Battle.MOD_ID, "textures/vignette.png");
    private static final ResourceLocation BUT_TEXT = ResourceLocation.tryBuild(Battle.MOD_ID, "textures/but_text.png");
    private static final ResourceLocation SELL_TEXT = ResourceLocation.tryBuild(Battle.MOD_ID, "textures/sell_text.png");

    public static class GuiContainerMod extends AbstractContainerMenu {
        public Level level;
        public int x, y, z;
        public Player player;

        public GuiContainerMod(int id, Level level, int x, int y, int z, Player player) {
            super(InitMenus.SHOP_MAIN_MENU.get(), id);
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

        public static GuiWindow create(GuiContainerMod container, Inventory inventory, Component title) {
            return new GuiWindow(container, inventory, title);
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

            // Close button - закрывает GUI
            this.addRenderableWidget(Button.builder(Component.literal("x"), (button) -> {
                entity.closeContainer();
            }).bounds(leftPos + 160, topPos + 3, 10, 20).build());

            // Buy button - открывает меню покупки
            this.addRenderableWidget(Button.builder(Component.literal(" "), (button) -> {
                nazzy.battle.procedure.ProcedureShopGui_buy1.executeProcedure(new java.util.HashMap<>() {{
                    put("entity", entity);
                    put("x", x);
                    put("y", y);
                    put("z", z);
                    put("world", world);
                }});
            }).bounds(leftPos + 25, topPos + 102, 40, 20).build());

            // Sell button - открывает меню продажи
            this.addRenderableWidget(Button.builder(Component.literal("  "), (button) -> {
                nazzy.battle.procedure.ProcedureSHOP_gui_sellGUI.executeProcedure(new java.util.HashMap<>() {{
                    put("entity", entity);
                    put("x", x);
                    put("y", y);
                    put("z", z);
                    put("world", world);
                }});
            }).bounds(leftPos + 106, topPos + 102, 40, 20).build());
        }

        @Override
        protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int leftPos = this.leftPos;
            int topPos = this.topPos;

            if (CURRENCY_TEXT != null) {
                graphics.blit(CURRENCY_TEXT, leftPos - 2, topPos + 2, 0, 0, 256, 256, 256, 256);
            }
            if (SHOP_TEXTURE != null) {
                graphics.blit(SHOP_TEXTURE, leftPos + 34, topPos + 2, 0, 0, 256, 256, 256, 256);
            }
            if (VIGNETTE != null) {
                graphics.blit(VIGNETTE, leftPos - 2, topPos - 1, 0, 0, 256, 256, 256, 256);
            }
            if (BUT_TEXT != null) {
                graphics.blit(BUT_TEXT, leftPos + 30, topPos + 65, 0, 0, 256, 256, 256, 256);
            }
            if (SELL_TEXT != null) {
                graphics.blit(SELL_TEXT, leftPos + 111, topPos + 65, 0, 0, 256, 256, 256, 256);
            }
        }

        @Override
        protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
            BlockEntity tileEntity = world.getBlockEntity(new BlockPos(x, y, z));
            int money = 0;
            if (tileEntity instanceof ShopBlockEntity shop) {
                money = (int) shop.getMoney();
            }
            graphics.drawString(this.font, "" + money, 13, 4, 0xFFFFFF, false);
            graphics.drawString(this.font, Component.translatable("Здесь можно купить предметы"), 25, 39, 0xFFFFFF, false);
            graphics.drawString(this.font, Component.translatable("и продать руду."), 25, 48, 0xFFFFFF, false);
            graphics.drawString(this.font, Component.translatable("КУПИТЬ"), 31, 107, 0xFFFFFF, false);
            graphics.drawString(this.font, Component.translatable("ПРОДАТЬ"), 109, 107, 0xE04040, false);
            graphics.drawString(this.font, Component.translatable("Валюта в команде является общей."), 3, 137, 0xA6A6A6, false);
        }
    }
}