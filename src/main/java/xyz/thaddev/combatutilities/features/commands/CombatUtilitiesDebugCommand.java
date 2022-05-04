package xyz.thaddev.combatutilities.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import xyz.thaddev.combatutilities.CU;
import xyz.thaddev.combatutilities.util.ColorHelper;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class CombatUtilitiesDebugCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("debug")
                .then(literal("servercheck").executes(context -> {
                    if (!CU.i.mc.isInSingleplayer() && CU.i.mc.interactionManager != null && CU.i.mc.player != null) {
                        ServerInfo serverInfo = CU.i.mc.getCurrentServerEntry();
                        if (serverInfo != null) {
                            CU.i.printMessage(ColorHelper.from("(%$dark_gray)Server Check:"));
                            CU.i.printMessage(ColorHelper.from("(%$dark_gray)    Server IP: (%$gold)" + serverInfo.address.contains("play.pvplegacy.net")));
                            CU.i.printMessage(ColorHelper.from("(%$dark_gray)    (Server IP: (%$gold)" + serverInfo.address + " (%$dark_gray))"));
                            boolean is = CU.i.mc.player.world.getBlockState(new BlockPos(69762, 14, 70101)).getBlock() == Blocks.WARPED_WALL_SIGN;
                            boolean is2 = CU.i.mc.interactionManager.getCurrentGameMode() == GameMode.ADVENTURE;
                            CU.i.printMessage(ColorHelper.from("(%$dark_gray)    Sign Location: (%$gold)" + is));
                            CU.i.printMessage(ColorHelper.from("(%$dark_gray)    Is Adventure Mode: (%$gold)" + is2));
                            CU.i.printMessage(ColorHelper.from("(%$dark_gray)    WorldDetector: (%$gold)" + CU.i.worldDetector.isPVPLegacyLobby()));
                        }
                    }
                    return 0;
                }))
                .then(literal("getblock").executes(context -> {
                    if (CU.i.mc.world != null && CU.i.mc.crosshairTarget != null && CU.i.mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                        BlockPos pos = new BlockPos(CU.i.mc.crosshairTarget.getPos());
                        CU.i.printMessage(ColorHelper.from("(%$dark_gray)Block: (%$gold)" + CU.i.mc.world.getBlockState(pos).getBlock()));
                    }
                    return 0;
                })));
    }
}
