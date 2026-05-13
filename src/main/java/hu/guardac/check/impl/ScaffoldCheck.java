package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.entity.Player;

public class ScaffoldCheck extends Check {

    public ScaffoldCheck(GuardAC plugin) { super(plugin, "Scaffold", "A"); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.scaffold.enabled", true)) return;

        float pitch = player.getLocation().getPitch();
        boolean moving = Math.abs(data.getLastDeltaX()) > 0.05
                      || Math.abs(data.getLastDeltaZ()) > 0.05;

        if (moving && pitch < 60 && !player.isOnGround()) {
            data.setScaffoldTicks(data.getScaffoldTicks() + 1);
        } else {
            if (data.getScaffoldTicks() > 0) data.setScaffoldTicks(data.getScaffoldTicks() - 1);
        }

        if (data.getScaffoldTicks() > 8) {
            flag(player, data, String.format("pitch=%.1f ticks=%d", pitch, data.getScaffoldTicks()));
        }
    }
}
