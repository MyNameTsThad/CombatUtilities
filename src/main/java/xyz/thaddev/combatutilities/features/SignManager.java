package xyz.thaddev.combatutilities.features;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;
import xyz.thaddev.combatutilities.CU;

public class SignManager {

    private BlockPos signPos;

    public SignManager() {
        signPos = null;
    }

    public void setSignPos(BlockPos pos) {
        if (pos == null) {
            signPos = null;
            return;
        }
        if (CU.i.mc.player != null && CU.i.mc.world != null) {
            //CU.i.printMessage(ColorHelper.from("(%$gold)(%$bold)Updated sign to: " + pos.toShortString()));
            if (CU.i.worldDetector.isPVPLegacyLobby() && CU.i.mc.world.getBlockState(pos).getBlock() == Blocks.WARPED_WALL_SIGN) {
                SignBlockEntity entity = (SignBlockEntity) CU.i.mc.world.getBlockEntity(pos);
                //CU.i.printMessage(ColorHelper.from("(%$gold)(%$bold)Updated sign to: " + pos.toShortString()));
                if (entity != null && entity.getTextOnRow(3, false).getSiblings().get(0).asString().equalsIgnoreCase("Click to join")) {
                    //CU.i.printMessage(ColorHelper.from("(%$gold)(%$bold)Updated sign to: " + pos.toShortString()));
                    signPos = pos;
                }
            }
        }
    }

    public int getSignState() {
        return 0;
    }

    public BlockPos getSignPos() {
        return signPos;
    }
}
