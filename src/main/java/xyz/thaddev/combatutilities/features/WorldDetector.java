package xyz.thaddev.combatutilities.features;

import net.minecraft.block.Blocks;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import xyz.thaddev.combatutilities.CU;

public class WorldDetector {
    public WorldDetector() {
    }

    public boolean isPVPLegacyLobby() {
        if (CU.i.mc.player != null && !CU.i.mc.isInSingleplayer()) {
            ServerInfo serverInfo = CU.i.mc.getCurrentServerEntry();
            if (serverInfo != null && serverInfo.address.contains("play.pvplegacy.net") && CU.i.mc.interactionManager != null) {
                return (CU.i.mc.player.world.getBlockState(new BlockPos(69762, 14, 70101)).getBlock() == Blocks.WARPED_WALL_SIGN) && (CU.i.mc.interactionManager.getCurrentGameMode() == GameMode.ADVENTURE);
            }
        }
        return false;
    }
}
