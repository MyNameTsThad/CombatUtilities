package xyz.thaddev.combatutilities.mixin.bettergameselector;

import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.thaddev.combatutilities.CU;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "interactBlock", at = @At("HEAD"))
    public void interactBlock(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        BlockPos blockPos = hitResult.getBlockPos();
        if (CU.i.worldDetector.isPVPLegacyLobby() && world.getBlockState(blockPos).getBlock() == Blocks.WARPED_WALL_SIGN) {
            if (!(player.shouldCancelInteraction() && (!player.getMainHandStack().isEmpty() || !player.getOffHandStack().isEmpty())) && (world.getBlockState(blockPos).onUse(world, player, hand, hitResult)).isAccepted()) {
                if (!CU.i.selectedSigns.contains(blockPos)) CU.i.selectedSigns.add(blockPos);
                else CU.i.selectedSigns.remove(blockPos);
            }
        }
    }
}
