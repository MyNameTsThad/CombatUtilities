package xyz.thaddev.combatutilities;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.thaddev.combatutilities.features.WorldDetector;

import java.util.ArrayList;
import java.util.UUID;

public class CombatUtilities implements ClientModInitializer {
    public static final String MOD_ID = "combatutilities";
    public static final String MOD_NAME = "Combat Utilities";
    public static final String MOD_SNAME = "CombatUtilities";
    public static final String MOD_VERSION = "0.1.0";

    public static CombatUtilities instance;
    public MinecraftClient mc;
    public WorldDetector worldDetector;

    //global features
    public ArrayList<BlockPos> selectedSigns;

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing " + MOD_SNAME + " " + MOD_VERSION);
        instance = this;
        mc = MinecraftClient.getInstance();
        new CU();
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


}
