package xyz.thaddev.combatutilities.mixin.bettergameselector;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.thaddev.combatutilities.CU;
import xyz.thaddev.combatutilities.util.RenderHelper;

@Mixin(BlockEntityRenderDispatcher.class)
public class SignRendererMixin {
    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("HEAD"))
    private void render(BlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (CU.i.signManager != null && CU.i.signManager.getSignPos() != null && blockEntity.getPos().toShortString().equals(CU.i.signManager.getSignPos().toShortString())) {
            if (CU.i.signManager.checkSign()) {
                RenderHelper.drawSignBox(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), blockEntity.getPos(), CU.i.signManager.getSignColor()/*, ((SignBlock)blockEntity.getWorld().getBlockState(blockEntity.getPos()).getBlock()).ROTATION.get*/);
            }
        }
    }
}
