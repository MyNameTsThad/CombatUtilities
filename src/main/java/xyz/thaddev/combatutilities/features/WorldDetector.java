package xyz.thaddev.combatutilities.features;

import net.minecraft.block.Blocks;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import xyz.thaddev.combatutilities.CU;

import java.util.Objects;

public class WorldDetector {
    public WorldDetector() {
    }

    public boolean isPVPLegacyLobby() {
        //if the player is on a remote server
        if (CU.i.mc.player != null && CU.i.mc.player.getServer() != null) {
            MinecraftServer server = CU.i.mc.player.getServer();
            if (server.getServerIp().contains("play.pvplegacy.net")) {
                PlayerListEntry playerListEntry = Objects.requireNonNull(CU.i.mc.getNetworkHandler()).getPlayerListEntry(CU.i.mc.player.getGameProfile().getId());
                return CU.i.mc.player.world.getBlockState(new BlockPos(69762, 14, 70101)).getBlock() == Blocks.WARPED_WALL_SIGN && (playerListEntry != null && playerListEntry.getGameMode() == GameMode.ADVENTURE);
            }
        }
        return false;
    }
}
