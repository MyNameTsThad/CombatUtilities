package xyz.thaddev.combatutilities;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.thaddev.combatutilities.features.Events;
import xyz.thaddev.combatutilities.features.SignManager;
import xyz.thaddev.combatutilities.features.WorldDetector;
import xyz.thaddev.combatutilities.features.commands.CombatUtilitiesDebugCommand;

import java.util.UUID;

public class CombatUtilities implements ClientModInitializer {
    public static final String MOD_ID = "combatutilities";
    public static final String MOD_NAME = "Combat Utilities";
    public static final String MOD_SNAME = "CombatUtilities";
    public static final String MOD_VERSION = "0.1.0";

    public static CombatUtilities instance;
    public MinecraftClient mc;
    public WorldDetector worldDetector;
    public SignManager signManager;

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing " + MOD_SNAME + " " + MOD_VERSION);
        instance = this;
        mc = MinecraftClient.getInstance();
        new CU();
        worldDetector = new WorldDetector();
        signManager = new SignManager();
        Events.register();
        registerCommands(ClientCommandManager.DISPATCHER);
    }

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        CombatUtilitiesDebugCommand.register(dispatcher);
    }

    public String getUUIDAsString() {
        if (mc.player != null) {
            return mc.player.getUuidAsString();
        }
        return "";
    }

    public UUID getUUID() {
        if (mc.player != null) {
            return mc.player.getUuid();
        }
        return null;
    }

    public void printMessage(String message) {
        if (mc != null && mc.inGameHud != null && mc.player != null) {
            mc.inGameHud.addChatMessage(MessageType.CHAT, Text.of(message), getUUID());
        }
    }
}
