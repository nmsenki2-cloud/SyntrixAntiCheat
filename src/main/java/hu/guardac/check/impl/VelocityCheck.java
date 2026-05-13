package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.entity.Player;

public class VelocityCheck extends Check {

    public VelocityCheck(GuardAC plugin) { super(plugin, "Velocity", "A"); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.velocity.enabled", true)) return;
        if (!data.isExpectingVelocity()) return;

        long elapsed = System.currentTimeMillis() - data.getVelocitySentTime();
        if (elapsed > 1500) { data.setExpectingVelocity(false); return; }

        double hVel = Math.sqrt(
            player.getVelocity().getX() * player.getVelocity().getX() +
            player.getVelocity().getZ() * player.getVelocity().getZ()
        );

        if (hVel < 0.05 && elapsed < 600) {
            flag(player, data, String.format("hVel=%.3f elapsed=%dms", hVel, elapsed));
        }

        data.setExpectingVelocity(false);
    }
}
