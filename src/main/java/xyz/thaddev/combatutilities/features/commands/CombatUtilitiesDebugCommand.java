package xyz.thaddev.combatutilities.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.property.Properties;
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
                            CU.i.printMessage(ColorHelper.from("(%$dark_gray)    WorldDetector: (%$gold)" + CU.i.worldDetector.isInPVPLegacyLobby()));
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
                }))
                .then(literal("getdistance").executes(context -> {
                    if (CU.i.mc.world != null && CU.i.mc.crosshairTarget != null && CU.i.mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
                        if (CU.i.mc.player != null) {
                            if (CU.i.mc.targetedEntity != null) {
                                double dist = CU.i.mc.player.squaredDistanceTo(CU.i.mc.targetedEntity);
                                double distSquareRoot = Math.sqrt(dist);
                                double distCubeRoot = Math.cbrt(dist);
                                CU.i.printMessage(ColorHelper.from("(%$dark_gray)Distance to " + CU.i.mc.targetedEntity.getName().getString() + ": (%$gold)" + dist + " " + distSquareRoot + " " + distCubeRoot));
                            }
                        }
                    }
                    return 0;
                }))
                .then(literal("getsign").executes(context -> {
                    if (CU.i.mc.world != null && CU.i.mc.crosshairTarget != null && CU.i.mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                        BlockPos pos = new BlockPos(CU.i.mc.crosshairTarget.getPos());
                        if (CU.i.mc.world.getBlockState(pos).getBlock() == Blocks.WARPED_WALL_SIGN) {
                            CU.i.printMessage(ColorHelper.from("(%$dark_gray)Facing: (%$gold)" + CU.i.mc.world.getBlockState(pos).getProperties().contains(Properties.FACING)));
                        }
                    }
                    return 0;
                }))
                .then(literal("entitydata").executes(context -> {
                    if (CU.i.mc.targetedEntity instanceof LivingEntity entity && !entity.isDead()) {
                        double health = entity.getHealth();
                        double maxHealth = entity.getMaxHealth();
                        double absorption = entity.getAbsorptionAmount();
                        CU.i.printMessage(ColorHelper.from("(%$dark_gray)Data for entity: (%$gold)" + CU.i.mc.targetedEntity));
                        CU.i.printMessage(ColorHelper.from("    (%$dark_gray)Health: (%$gold)" + health));
                        CU.i.printMessage(ColorHelper.from("    (%$dark_gray)Max Health: (%$gold)" + maxHealth));
                        CU.i.printMessage(ColorHelper.from("    (%$dark_gray)Absorption: (%$gold)" + absorption));
                    }
                    return 0;
                })));
    }
}
