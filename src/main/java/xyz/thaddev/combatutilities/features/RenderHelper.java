package xyz.thaddev.combatutilities.features;

import me.x150.renderer.renderer.Renderer3d;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class RenderHelper extends DrawableHelper {
    private VertexBuffer solidBox;
    private VertexBuffer outlinedBox;

    public static void drawBox(MatrixStack matrix, VertexConsumer vertices, Entity entity, float red, float green, float blue, float alpha) {
        Box box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ());
        WorldRenderer.drawBox(matrix, vertices, box, red, green, blue, alpha);
        //CU.i.mc.inGameHud.addChatMessage(MessageType.CHAT, Text.of(ColorHelper.from("%$gold [CombatUtilities] %$aqua Is Rendering through walls: " + Renderer3d.rendersThroughWalls())), CU.i.getUUID());
        Renderer3d.renderFilled(matrix, new Vec3d(entity.getPos().x - (box.maxX - box.minX), entity.getPos().y, entity.getPos().z - (box.maxZ - box.minZ)), new Vec3d((box.maxX - box.minX) * 2, (box.maxY - box.minY) * 2, (box.maxZ - box.minZ) * 2), new Color(red, green, blue, alpha));
    }
}
