package xyz.thaddev.combatutilities.features;

import me.x150.renderer.renderer.Renderer3d;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class RenderHelper extends DrawableHelper {
    private VertexBuffer solidBox;
    private VertexBuffer outlinedBox;

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
}
