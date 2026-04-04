package nazzy.battle.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import nazzy.battle.Battle;
import nazzy.battle.BattleVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Battle.MOD_ID, value = Dist.CLIENT)
public class OverlayStartgame {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        BattleVariables.MapVariables vars = BattleVariables.MapVariables.get(mc.level);

        if (vars.GameStart > 0 && vars.isPlaying) {
            GuiGraphics graphics = event.getGuiGraphics();
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();
            int posX = screenWidth / 2;
            int posY = screenHeight / 2;

            RenderSystem.enableBlend();
            graphics.drawString(mc.font, Component.translatable("Начало Игры:"), posX - 27, posY + 36, 0xFFFFFF);
            graphics.drawString(mc.font, String.valueOf((int)vars.GameStart), posX - 9, posY + 54, 0xCC0000);
            RenderSystem.disableBlend();
        }
    }
}