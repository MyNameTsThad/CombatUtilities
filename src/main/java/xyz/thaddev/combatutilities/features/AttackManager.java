package xyz.thaddev.combatutilities.features;

import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.entity.LivingEntity;
import xyz.thaddev.combatutilities.CU;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AttackManager {
    private List<Map.Entry<LivingEntity, double[]>> lastAttacked;
    private List<Map.Entry<LivingEntity, double[]>> lastAttackedBy;

    public AttackManager() {
        this.lastAttacked = new ArrayList<>();
        this.lastAttackedBy = new ArrayList<>();
        new Timer("checkScreen-AttackManager").scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (CU.i.mc != null) {
                    checkLastAttacked();
                    checkLastAttackedBy();
                    if (CU.i.mc.currentScreen instanceof ProgressScreen | CU.i.mc.currentScreen instanceof DownloadingTerrainScreen) {
                        lastAttacked = new ArrayList<>();
                        lastAttackedBy = new ArrayList<>();
                    }
                }
            }
        }, 50, 50);
    }

    private void addLastAttacked(LivingEntity entity, double initDamage, double initRawDamage) {
        this.lastAttacked.add(Map.entry(entity, new double[]{System.currentTimeMillis() + 15000, initDamage, initRawDamage}));
    }

    private void addLastAttackedBy(LivingEntity entity, double initDamage, double initRawDamage) {
        this.lastAttackedBy.add(Map.entry(entity, new double[]{System.currentTimeMillis() + 15000, initDamage, initRawDamage}));
    }

    public Map.Entry<LivingEntity, double[]> getLastAttacked(LivingEntity base) {
        if (this.lastAttacked.isEmpty()) return null;
        checkLastAttacked();
        for (Map.Entry<LivingEntity, double[]> entry : this.lastAttacked) {
            if (entry.getKey().getUuidAsString().equals(base.getUuidAsString())) {
                return entry;
            }
        }
        return null;
    }

    public Map.Entry<LivingEntity, double[]> getLastAttackedBy(LivingEntity base) {
        if (this.lastAttackedBy.isEmpty()) return null;
        checkLastAttackedBy();
        for (Map.Entry<LivingEntity, double[]> entry : this.lastAttackedBy) {
            if (entry.getKey().getUuidAsString().equals(base.getUuidAsString())) {
                return entry;
            }
        }
        return null;
    }

    public void damageLastAttacked(LivingEntity base, double damage, double rawDamage) {
        boolean isFound = false;
        checkLastAttacked();
        for (Map.Entry<LivingEntity, double[]> entry : this.lastAttacked) {
            if (entry.getKey().getUuidAsString().equals(base.getUuidAsString())) {
                entry.getValue()[0] = System.currentTimeMillis() + 15000;
                entry.getValue()[1] += damage;
                entry.getValue()[2] += rawDamage;
                isFound = true;
                //CU.i.printMessage(ColorHelper.from("(%$green)You are attacking (%$gold)" + base.getName().getString() + "(%$green). Your total damage to them is now (%$gold)" + entry.getValue()[1] + "(%$green)."));
            }
        }
        if (!isFound) {
            addLastAttacked(base, damage, rawDamage);
            //CU.i.printMessage(ColorHelper.from("(%$green)You are attacking (%$gold)" + base.getName().getString() + "(%$green). Your total damage to them is now (%$gold)" + damage + "(%$green)."));
        }
    }

    public void damageLastAttackedBy(LivingEntity base, double damage, double rawDamage) {
        boolean isFound = false;
        checkLastAttackedBy();
        for (Map.Entry<LivingEntity, double[]> entry : this.lastAttackedBy) {
            if (entry.getKey().getUuidAsString().equals(base.getUuidAsString())) {
                entry.getValue()[0] = System.currentTimeMillis() + 15000;
                entry.getValue()[1] += damage;
                entry.getValue()[2] += rawDamage;
                isFound = true;
                //CU.i.printMessage(ColorHelper.from("(%$gold)" + base.getName().getString() + "(%$green)has attacked you for (%$yellow)" + damage + " (%$green)damage. Their total damage to you is now (%$gold)" + entry.getValue()[1] + "(%$green)."));
            }
        }
        if (!isFound) {
            addLastAttackedBy(base, damage, rawDamage);
            //CU.i.printMessage(ColorHelper.from("(%$gold)" + base.getName().getString() + "(%$green)has attacked you for (%$yellow)" + damage + " (%$green)damage. Their total damage to you is now (%$gold)" + damage + "(%$green)."));
        }
    }

    public void checkLastAttacked() {
        lastAttacked.removeIf(entry -> !entry.getKey().isAlive() || entry.getValue()[0] < System.currentTimeMillis());
    }

    public void checkLastAttackedBy() {
        lastAttackedBy.removeIf(entry -> !entry.getKey().isAlive() || entry.getValue()[0] < System.currentTimeMillis());
    }
}
