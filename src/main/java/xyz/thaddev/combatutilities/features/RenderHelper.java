package xyz.thaddev.combatutilities.features;

import com.mojang.blaze3d.systems.RenderSystem;
import me.x150.renderer.renderer.Renderer3d;
import me.x150.renderer.renderer.RendererUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import xyz.thaddev.combatutilities.CU;

import java.awt.*;

public class RenderHelper extends DrawableHelper {
    public static void drawBox(MatrixStack matrix, VertexConsumer vertices, Entity entity, Color color) {
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Box box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ());
        WorldRenderer.drawBox(matrix, vertices, box, red, green, blue, alpha);
        //CU.i.mc.inGameHud.addChatMessage(MessageType.CHAT, Text.of(ColorHelper.from("%$gold [CombatUtilities] %$aqua Is Rendering through walls: " + Renderer3d.rendersThroughWalls())), CU.i.getUUID());
        Renderer3d.renderFilled(matrix, new Vec3d(entity.getPos().x - (box.maxX - box.minX), entity.getPos().y, entity.getPos().z - (box.maxZ - box.minZ)), new Vec3d((box.maxX - box.minX) * 2, (box.maxY - box.minY) * 2, (box.maxZ - box.minZ) * 2), new Color(red, green, blue, alpha));
    }

    public static void drawBox(MatrixStack matrix, VertexConsumer vertices, BlockPos blockPos, Color color) {
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Box box = new Box(blockPos).offset(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
        Box box1 = new Box(blockPos).offset(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ() + box.maxZ - box.minZ - 1).shrink(0, 0, 0.5);
        WorldRenderer.drawBox(matrix, vertices, box1, red, green, blue, alpha);
        //CU.i.mc.inGameHud.addChatMessage(MessageType.CHAT, Text.of(ColorHelper.from("%$gold [CombatUtilities] %$aqua Is Rendering through walls: " + Renderer3d.rendersThroughWalls())), CU.i.getUUID());
        Renderer3d.renderFilled(matrix, new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), new Vec3d((box.maxX - box.minX) * 2, (box.maxY - box.minY) * 2, (box.maxZ - box.minZ)), new Color(red, green, blue, alpha));
    }

    /**
     * Renders a filled block
     *
     * @param stack      The context MatrixStack
     * @param start      The start coordinate of the block
     * @param dimensions The dimensions of the block
     * @param color      The color of the filling
     */
    public static void renderFilled(MatrixStack stack, Vec3d start, Vec3d dimensions, Color color) {
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = CU.i.mc.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        start = start.subtract(camPos);
        Vec3d end = start.add(dimensions);
        Matrix4f matrix = stack.peek().getPositionMatrix();
        float x1 = (float) start.x;
        float y1 = (float) start.y;
        float z1 = (float) start.z;
        float x2 = (float) end.x;
        float y2 = (float) end.y;
        float z2 = (float) end.z;
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RendererUtils.setupRender();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).next();

        buffer.end();

        CU.i.printMessage("(%$gray)Rendering box");

        BufferRenderer.draw(buffer);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RendererUtils.endRender();
    }

    private static void setAppropiateGlMode() {

    }
}
