package xyz.thaddev.combatutilities.features.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import xyz.thaddev.combatutilities.CU;

public class AttackingHud extends DrawableHelper {
    static Identifier BARS = new Identifier("combatutilities", "hud/bars.png");
    static int TEX_SIZE = 256;
    static int WIDTH = 88;
    static int HEIGHT = 5;

    public static void registerHUD() {
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            if (CU.i.mc.targetedEntity instanceof LivingEntity entity && !entity.isDead() && (CU.i.attackManager.getLastAttacked(entity) != null || CU.i.attackManager.getLastAttackedBy(entity) != null)) {
                double healthPercent = entity.getHealth() / entity.getMaxHealth();
                double absorption = entity.getAbsorptionAmount();
                RenderSystem.setShaderTexture(0, BARS);
                int shift = absorption > 0 ? CU.i.mc.textRenderer.getWidth(" + " + absorption) + 2 : 0;
                DrawableHelper.drawTexture(matrixStack, (CU.i.mc.getWindow().getScaledWidth() / 2) - (WIDTH / 2) - shift, (CU.i.mc.getWindow().getScaledHeight() / 2) - (HEIGHT / 2) - 15, (int) (WIDTH * healthPercent), HEIGHT, 0, 5, (int) (WIDTH * healthPercent), HEIGHT, TEX_SIZE, TEX_SIZE);
                //bar
                DrawableHelper.drawTexture(matrixStack, (CU.i.mc.getWindow().getScaledWidth() / 2) - (WIDTH / 2) - shift, (CU.i.mc.getWindow().getScaledHeight() / 2) - (HEIGHT / 2) - 15, WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT, TEX_SIZE, TEX_SIZE);
                if (absorption > 0) {
                    //absorption
                    CU.i.mc.textRenderer.drawWithShadow(matrixStack, " + " + absorption, (CU.i.mc.getWindow().getScaledWidth() / 2f) + (WIDTH / 2f) - shift, (CU.i.mc.getWindow().getScaledHeight() / 2f) - (HEIGHT / 2f) - 15, 0xFFD4AF37);
                    RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE);
                    DrawableHelper.drawTexture(matrixStack, (CU.i.mc.getWindow().getScaledWidth() / 2) - (WIDTH / 2) - shift - 1 + WIDTH + CU.i.mc.textRenderer.getWidth(" + " + absorption) + 3, (CU.i.mc.getWindow().getScaledHeight() / 2) - (HEIGHT / 2) - 16, 9, 9, 16, 45, 9, 9, TEX_SIZE, TEX_SIZE);
                    DrawableHelper.drawTexture(matrixStack, (CU.i.mc.getWindow().getScaledWidth() / 2) - (WIDTH / 2) - shift + WIDTH + CU.i.mc.textRenderer.getWidth(" + " + absorption) + 3, (CU.i.mc.getWindow().getScaledHeight() / 2) - (HEIGHT / 2) - 15, 7, 7, 161, 1, 7, 7, TEX_SIZE, TEX_SIZE);
                }
                //damage
                if (CU.i.attackManager.getLastAttacked(entity) != null && CU.i.attackManager.getLastAttackedBy(entity) == null) {
                    int textShift = CU.i.mc.textRenderer.getWidth(round(CU.i.attackManager.getLastAttacked(entity).getValue()[1]) + " | " + round(CU.i.attackManager.getLastAttacked(entity).getValue()[2]));
                    CU.i.mc.textRenderer.drawWithShadow(matrixStack, round(CU.i.attackManager.getLastAttacked(entity).getValue()[1]) + " | " + round(CU.i.attackManager.getLastAttacked(entity).getValue()[2]), (CU.i.mc.getWindow().getScaledWidth() / 2f) - (textShift / 2f), (CU.i.mc.getWindow().getScaledHeight() / 2f) - (HEIGHT / 2f) + 13, 0xFFD4AF37);
                } else if (CU.i.attackManager.getLastAttacked(entity) == null && CU.i.attackManager.getLastAttackedBy(entity) != null) {
                    int textShift = CU.i.mc.textRenderer.getWidth(round(CU.i.attackManager.getLastAttackedBy(entity).getValue()[1]) + " | " + round(CU.i.attackManager.getLastAttackedBy(entity).getValue()[2]));
                    CU.i.mc.textRenderer.drawWithShadow(matrixStack, round(CU.i.attackManager.getLastAttackedBy(entity).getValue()[1]) + " | " + round(CU.i.attackManager.getLastAttackedBy(entity).getValue()[2]), (CU.i.mc.getWindow().getScaledWidth() / 2f) - (textShift / 2f), (CU.i.mc.getWindow().getScaledHeight() / 2f) - (HEIGHT / 2f) + 13, 0xFFD4AF37);
                } else if (CU.i.attackManager.getLastAttacked(entity) != null && CU.i.attackManager.getLastAttackedBy(entity) != null) {
                    int textShift = CU.i.mc.textRenderer.getWidth(round(CU.i.attackManager.getLastAttacked(entity).getValue()[1]) + " | " + round(CU.i.attackManager.getLastAttacked(entity).getValue()[2]) + " | " + round(CU.i.attackManager.getLastAttackedBy(entity).getValue()[1]) + " | " + round(CU.i.attackManager.getLastAttackedBy(entity).getValue()[2]));
                    CU.i.mc.textRenderer.drawWithShadow(matrixStack, round(CU.i.attackManager.getLastAttacked(entity).getValue()[1]) + " | " + round(CU.i.attackManager.getLastAttacked(entity).getValue()[2]) + " | " + round(CU.i.attackManager.getLastAttackedBy(entity).getValue()[1]) + " | " + round(CU.i.attackManager.getLastAttackedBy(entity).getValue()[2]), (CU.i.mc.getWindow().getScaledWidth() / 2f) - (textShift / 2f), (CU.i.mc.getWindow().getScaledHeight() / 2f) - (HEIGHT / 2f) + 13, 0xFFD4AF37);
                }
            }
            //Renderer2d.renderTexture(matrixStack, BARS, 0, 0, 88, 5);
        });
    }

    private static double round(double value) {
        int scale = (int) Math.pow(10, 1);
        return (double) Math.round(value * scale) / scale;
    }
}
