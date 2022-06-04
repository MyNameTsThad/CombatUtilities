package xyz.thaddev.combatutilities.mixin.hitboxcolor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.thaddev.combatutilities.CU;
import xyz.thaddev.combatutilities.util.RenderHelper;

import java.awt.*;

@Mixin(MobEntityRenderer.class)
public class MobEntityRendererMixin {
    @Inject(method = "render(Lnet/minecraft/entity/mob/MobEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = {@At("HEAD")})
    private void render(MobEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        MinecraftClient client = CU.i.mc;
        if (mobEntity instanceof VillagerEntity && client.player != null && mobEntity.isAlive()) {
            double distance = Math.cbrt(client.player.squaredDistanceTo(mobEntity));
            if (distance <= 2.20d && client.player.canSee(mobEntity)) {
                if (client.targetedEntity != null && client.targetedEntity.getUuidAsString().equals(mobEntity.getUuidAsString())) { // is targeted
                    Vec3d vec3d = client.player.getPos();
                    Vec3d vec3d2 = mobEntity.getRotationVec(1.0f);
                    Vec3d vec3d3 = vec3d.relativize(mobEntity.getPos()).normalize();
                    vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z);
                    boolean canBeBlocked = mobEntity.isBlocking() && vec3d3.dotProduct(vec3d2) < 0.0;
                    if (canBeBlocked) { // has their shield up
                        if (client.player.getMainHandStack().getItem() instanceof AxeItem) { // is using an axe
                            RenderHelper.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), mobEntity, new Color(1.0f, 1.0f, 0.0f, 0.8f));
                        } else {
                            RenderHelper.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), mobEntity, new Color(1.0f, 0.85f, 0.0f, 0.8f));
                        }
                    } else { // has their shield down
                        if (client.player.getAttackCooldownProgress(0.5f) > 0.9f) { // is fully charged
                            if (client.player.fallDistance > 0.0f && !client.player.isOnGround() && !client.player.isClimbing() && !client.player.isTouchingWater() && !client.player.hasStatusEffect(StatusEffects.BLINDNESS) && !client.player.hasVehicle() && !client.player.isSprinting()) { // is falling
                                RenderHelper.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), mobEntity, new Color(0.0f, 1.0f, 0.0f, 0.8f));
                            } else {
                                RenderHelper.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), mobEntity, new Color(1.0f, 1.0f, 0.0f, 0.8f));
                            }
                        } else {
                            RenderHelper.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), mobEntity, new Color(1.0f, 0.65f, 0.0f, 0.8f));
                        }
                    }
                } else {
                    //RenderHelper.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), mobEntity, new Color(1.0f, 0.0f, 0.0f, 0.8f));
                }
            }
        }
    }
}
