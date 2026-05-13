package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.entity.Player;

public class BlinkCheck extends Check {

    public BlinkCheck(GuardAC plugin) { super(plugin, "Blink", "A"); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.blink.enabled", true)) return;
        if (player.isFlying() || player.getAllowFlight()) return;
        if (data.getLastLocation() == null) return;

        double dx   = player.getLocation().getX() - data.getLastLocation().getX();
        double dz   = player.getLocation().getZ() - data.getLastLocation().getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        double max  = plugin.getConfig().getDouble("checks.blink.max-position-diff", 10.0);

        if (dist > max) {
            flag(player, data, String.format("dist=%.2f max=%.2f", dist, max));
        }
    }
}
