package xyz.thaddev.combatutilities;

import org.slf4j.Logger;

/*
 * Shorthand class for <code>CombatUtilities</code>.
 */
public class CU {
    public static CombatUtilities i;
    public static Logger l;

    /*
     * Shorthand class for <code>CombatUtilities</code>.
     */
    public CU() {
        i = CombatUtilities.instance;
        l = CombatUtilities.LOGGER;
    }
}
