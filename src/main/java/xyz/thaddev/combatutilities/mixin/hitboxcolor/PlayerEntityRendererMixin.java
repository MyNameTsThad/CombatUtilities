package xyz.thaddev.combatutilities.mixin.hitboxcolor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.thaddev.combatutilities.CU;
import xyz.thaddev.combatutilities.util.RenderHelper;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = {@At("HEAD")})
    private void render(AbstractClientPlayerEntity targetPlayer, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        MinecraftClient client = CU.i.mc;

        if (client.player != null && !targetPlayer.getUuidAsString().equals(client.player.getUuidAsString())) {
            double distance = Math.cbrt(client.player.squaredDistanceTo(targetPlayer));
            if (distance <= 2.20d && client.player.canSee(targetPlayer) && !targetPlayer.isSpectator()) { //is in reach
                if (client.targetedEntity != null && client.targetedEntity.getUuidAsString().equals(targetPlayer.getUuidAsString())) { // is targeted
                    Vec3d vec3d = client.player.getPos();
                    Vec3d vec3d2 = targetPlayer.getRotationVec(1.0f);
                    Vec3d vec3d3 = vec3d.relativize(targetPlayer.getPos()).normalize();
                    vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z);
                    boolean canBeBlocked = targetPlayer.isBlocking() && vec3d3.dotProduct(vec3d2) < 0.0;
                    if (canBeBlocked) { // has their shield up
                        if (client.player.getMainHandStack().getItem() instanceof AxeItem) { // is using an axe
                            RenderHelper.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), targetPlayer, CU.i.conf.readyColor);
                        } else {
                            RenderHelper.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), targetPlayer, CU.i.conf.badShieldColor);
                        }
                    } else { // has their shield down
                        if (client.player.getAttackCooldownProgress(0.5f) > 0.9f) { // is fully charged
                            if (client.player.fallDistance > 0.0f && !client.player.isOnGround() && !client.player.isClimbing() && !client.player.isTouchingWater() && !client.player.hasStatusEffect(StatusEffects.BLINDNESS) && !client.player.hasVehicle() && !client.player.isSprinting()) { // is falling
                                RenderHelper.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), targetPlayer, CU.i.conf.critColor);
                            } else {
                                RenderHelper.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), targetPlayer, CU.i.conf.readyColor);
                            }
                        } else {
                            RenderHelper.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), targetPlayer, CU.i.conf.chargingColor);
                        }
                    }
                }  //RenderHelper.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), targetPlayer, CU.i.conf.baseColor);

            }
        }
    }
}
