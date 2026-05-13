package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.entity.Player;

public class InventoryCheck extends Check {

    public InventoryCheck(GuardAC plugin) { super(plugin, "Inventory", "A"); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.inventory.enabled", true)) return;

        boolean moving = Math.abs(data.getLastDeltaX()) > 0.05
                      || Math.abs(data.getLastDeltaZ()) > 0.05;

        if (data.isInventoryOpen() && moving) {
            flag(player, data, "moving while inventory open");
        }
    }
}
