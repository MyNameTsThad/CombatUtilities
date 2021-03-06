package xyz.thaddev.combatutilities.mixin.hitboxcolor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.thaddev.combatutilities.CU;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "renderHitbox", at = {@At("HEAD")}, cancellable = true)
    private static void renderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = CU.i.mc;

        if (client.player != null && (entity instanceof VillagerEntity | entity instanceof PlayerEntity)) {
            double distance = Math.cbrt(client.player.squaredDistanceTo(entity));
            if (distance <= 2.5d) {
                if (!(entity instanceof PlayerEntity) || !entity.getUuidAsString().equals(client.player.getUuidAsString())) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "renderFire", at = {@At("HEAD")}, cancellable = true)
    private void renderFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, CallbackInfo ci) {
        MinecraftClient client = CU.i.mc;

        if (client.player != null && (entity instanceof VillagerEntity | entity instanceof PlayerEntity)) {
            double distance = Math.cbrt(client.player.squaredDistanceTo(entity));
            if (distance <= 2.5d) {
                if (!(entity instanceof PlayerEntity) || !entity.getUuidAsString().equals(client.player.getUuidAsString())) {
                    ci.cancel();
                }
            }
        }
    }
}
