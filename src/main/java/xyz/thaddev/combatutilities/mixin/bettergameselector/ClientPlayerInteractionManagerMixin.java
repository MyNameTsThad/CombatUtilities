package xyz.thaddev.combatutilities.mixin.bettergameselector;

import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.thaddev.combatutilities.CU;

import java.util.Objects;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    public void interactBlock(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        BlockPos blockPos = hitResult.getBlockPos();
        if (CU.i.signManager.getSignPos() != null && CU.i.signManager.getSignPos().toShortString().equals(blockPos.toShortString())) {
            if (CU.i.mc.player != null && !CU.i.mc.isInSingleplayer()) {
                Objects.requireNonNull(CU.i.mc.getNetworkHandler()).sendPacket(new ChatMessageC2SPacket("/leave"));
                CU.i.signManager.setSignPos(null);
            }
            cir.setReturnValue(ActionResult.SUCCESS);
            cir.cancel();
            return;
        }
        if (CU.i.worldDetector.isPVPLegacyLobby() && world.getBlockState(blockPos).getBlock() == Blocks.WARPED_WALL_SIGN) {
            CU.i.signManager.setSignPos(blockPos);
        }
    }
}
