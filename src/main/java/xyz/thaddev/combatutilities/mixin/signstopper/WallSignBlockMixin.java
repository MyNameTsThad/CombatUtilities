package xyz.thaddev.combatutilities.mixin.signstopper;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.thaddev.combatutilities.CU;

@Mixin(AbstractBlock.class)
public class WallSignBlockMixin {
    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (state.getBlock() == Blocks.WARPED_WALL_SIGN && CU.i.worldDetector.isInPVPLegacyLobby()) {
            Box box = state.getOutlineShape(world, pos, context).getBoundingBox().expand(0.75, 0, 0.75);
            cir.setReturnValue(VoxelShapes.cuboid(box));
        }
    }
}
