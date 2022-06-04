package xyz.thaddev.combatutilities.util;

import me.x150.renderer.renderer.Renderer3d;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
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
        Renderer3d.startRenderingThroughWalls();
        Renderer3d.renderFilled(matrix, new Vec3d(entity.getPos().x - (box.maxX - box.minX), entity.getPos().y, entity.getPos().z - (box.maxZ - box.minZ)), new Vec3d((box.maxX - box.minX) * 2, (box.maxY - box.minY) * 2, (box.maxZ - box.minZ) * 2), new Color(red, green, blue, alpha));
        Renderer3d.stopRenderingThroughWalls(); //its the only to make it render properly
    }

    public static void drawSignBox(MatrixStack matrix, VertexConsumer vertices, BlockPos blockPos, Color color) {
        if (CU.i.mc.world != null) {
            float red = color.getRed() / 255f;
            float green = color.getGreen() / 255f;
            float blue = color.getBlue() / 255f;
            float alpha = color.getAlpha() / 255f;
            Box box = CU.i.mc.world.getBlockState(blockPos).getOutlineShape(CU.i.mc.world, blockPos).getBoundingBox();
            WorldRenderer.drawBox(matrix, vertices, box, red, green, blue, alpha);
        }
    }

    public static int x(int gridSlot, int gridSize, int width) {
        return ((CU.i.mc.getWindow().getScaledWidth() / gridSize) * gridSlot) - (width / 2);
    }

    public static int x(int gridSlot, int gridSize, int width, int maxDistanceFromEdge) {
        if (gridSlot > (gridSize / 2)) {
            if (CU.i.mc.getWindow().getScaledWidth() - (((CU.i.mc.getWindow().getScaledWidth() / gridSize) * gridSlot) - (width / 2) + width) > maxDistanceFromEdge) {
                return CU.i.mc.getWindow().getScaledWidth() - maxDistanceFromEdge - width;
            } else {
                return ((CU.i.mc.getWindow().getScaledWidth() / gridSize) * gridSlot) - (width / 2);
            }
        } else {
            return Math.min(((CU.i.mc.getWindow().getScaledWidth() / gridSize) * gridSlot) - (width / 2), maxDistanceFromEdge);
        }

    }

    public static int y(int gridSlot, int gridSize, int height) {
        return ((CU.i.mc.getWindow().getScaledHeight() / gridSize) * gridSlot) - (height / 2);
    }

    public static int y(int gridSlot, int gridSize, int height, int maxDistanceFromEdge) {
        if (gridSlot > (gridSize / 2)) {
            if (CU.i.mc.getWindow().getScaledHeight() - (((CU.i.mc.getWindow().getScaledHeight() / gridSize) * gridSlot) - (height / 2) + height) > maxDistanceFromEdge) {
                //if the distance from the edge is more than the maxDistanceFromEdge
                return CU.i.mc.getWindow().getScaledHeight() - maxDistanceFromEdge - (height / 2);
            } else {
                return ((CU.i.mc.getWindow().getScaledHeight() / gridSize) * gridSlot) - (height / 2);
            }
        } else {
            return Math.min(((CU.i.mc.getWindow().getScaledHeight() / gridSize) * gridSlot) - (height / 2), maxDistanceFromEdge);
        }
    }
}
