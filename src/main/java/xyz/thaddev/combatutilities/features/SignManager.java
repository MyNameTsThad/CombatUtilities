package xyz.thaddev.combatutilities.features;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.util.math.BlockPos;
import xyz.thaddev.combatutilities.CU;

import java.awt.*;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SignManager {

    private BlockPos signPos;

    public SignManager() {
        signPos = null;
        new Timer("checkScreen-SignManager").scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (CU.i.mc != null && signPos != null) {
                    if (CU.i.mc.currentScreen instanceof ProgressScreen | CU.i.mc.currentScreen instanceof DownloadingTerrainScreen) {
                        signPos = null;
                    }
                }
            }
        }, 50, 50);
    }

    public void setSignPos(BlockPos pos) {
        if (pos == null) {
            signPos = null;
            return;
        }
        if (CU.i.mc.player != null && CU.i.mc.world != null) {
            if (CU.i.worldDetector.isInPVPLegacyLobby() && CU.i.mc.world.getBlockState(pos).getBlock() == Blocks.WARPED_WALL_SIGN) {
                SignBlockEntity entity = (SignBlockEntity) CU.i.mc.world.getBlockEntity(pos);
                if (entity != null && entity.getTextOnRow(3, false).getSiblings().size() > 0 && entity.getTextOnRow(3, false).getSiblings().get(0).asString().equalsIgnoreCase("Click to join")) {
                    signPos = pos;
                }
            }
        }
    }

    public boolean checkSign() {
        if (signPos != null) {
            if (CU.i.mc.player != null && CU.i.mc.world != null) {
                if (CU.i.worldDetector.isInPVPLegacyLobby() && CU.i.mc.world.getBlockState(signPos).getBlock() == Blocks.WARPED_WALL_SIGN) {
                    SignBlockEntity entity = (SignBlockEntity) CU.i.mc.world.getBlockEntity(signPos);
                    return entity != null &&
                            entity.getTextOnRow(3, false).getSiblings().size() > 0 &&
                            entity.getTextOnRow(3, false).getSiblings().get(0).asString().equalsIgnoreCase("Click to join") &&
                            Objects.requireNonNull(entity.getTextOnRow(0, false).getSiblings().get(1).getStyle().getColor()).getName().equals("aqua");
                }
            }
        }
        signPos = null;
        return false;
    }

    public Color getSignColor() {
        if (signPos != null) {
            if (CU.i.mc.player != null && CU.i.mc.world != null) {
                if (CU.i.worldDetector.isInPVPLegacyLobby() && CU.i.mc.world.getBlockState(signPos).getBlock() == Blocks.WARPED_WALL_SIGN) {
                    SignBlockEntity entity = (SignBlockEntity) CU.i.mc.world.getBlockEntity(signPos);
                    if (entity != null && entity.getTextOnRow(3, false).getSiblings().size() > 0) {
                        float players = Float.parseFloat(String.valueOf(entity.getTextOnRow(2, false).getSiblings().get(0).asString().charAt(0)));
                        float maxPlayers = Float.parseFloat(String.valueOf(entity.getTextOnRow(2, false).getSiblings().get(0).asString().charAt(2)));
                        try {
                            float maxPlayers2 = Float.parseFloat(String.valueOf(entity.getTextOnRow(2, false).getSiblings().get(0).asString().charAt(3)));
                            maxPlayers *= 10;
                            maxPlayers += maxPlayers2;
                        } catch (NumberFormatException ignored) {
                        }
                        if ((players / maxPlayers) > 0.5) {
                            return new Color(1f - (players / maxPlayers), 1f, 0f, 0.6f);
                        } else {
                            return new Color(1f, (players / maxPlayers) * 2, 0f, 0.6f);
                        }
                    }
                }
            }
        }
        return new Color(1f, 1f, 1f, 0.6f);
    }

    public BlockPos getSignPos() {
        return signPos;
    }
}
