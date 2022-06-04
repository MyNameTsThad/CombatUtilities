package xyz.thaddev.combatutilities.features.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import xyz.thaddev.combatutilities.CU;

import java.util.concurrent.atomic.AtomicBoolean;

public class ArmorDurabilityWarnOverlay extends DrawableHelper {
    private static final Identifier VIGNETTE = new Identifier("minecraft", "textures/misc/vignette.png");

    public static void registerHUD() {
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            AtomicBoolean armorDurabilityWarn = new AtomicBoolean(false);
            if (CU.i.mc.player != null) {
                CU.i.mc.player.getArmorItems().forEach(itemStack -> {
                    if (itemStack.isDamageable() && itemStack.getDamage() > 0 && ((float) itemStack.getDamage() / itemStack.getMaxDamage()) >= (CU.i.conf.warnPercent / 100f)) {
                        //CU.l.info(ColorHelper.from("(%$dark_gray)Damage for (&%gold)" + itemStack.getItem() + " (%$dark_gray) is (%$gold)" + ((float)itemStack.getDamage() / itemStack.getMaxDamage())));
                        armorDurabilityWarn.set(true);
                    }
                });
            }
            boolean shouldShow = false;
            if (armorDurabilityWarn.get() && !(CU.i.mc.currentScreen instanceof ChatScreen) && shouldShow) {
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
                RenderSystem.setShaderColor(0f, 0f, 1f, 1f);
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, VIGNETTE);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
                bufferBuilder.vertex(0.0, CU.i.mc.getWindow().getScaledHeight(), -180.0).texture(0.0f, 1.0f).next();
                bufferBuilder.vertex(CU.i.mc.getWindow().getScaledWidth(), CU.i.mc.getWindow().getScaledHeight(), -180.0).texture(1.0f, 1.0f).next();
                bufferBuilder.vertex(CU.i.mc.getWindow().getScaledWidth(), 0.0, -180.0).texture(1.0f, 0.0f).next();
                bufferBuilder.vertex(0.0, 0.0, -180.0).texture(0.0f, 0.0f).next();
                tessellator.draw();
//                RenderSystem.depthMask(true);
//                RenderSystem.enableDepthTest();
//                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//                RenderSystem.defaultBlendFunc();
            }
        });
    }
}
