package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class NoFallCheck extends Check {

    public NoFallCheck(GuardAC plugin) { super(plugin, "NoFall", "A"); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.nofall.enabled", true)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (player.isFlying() || player.getAllowFlight()) return;

        if (data.getAirTicks() > 15 && player.isOnGround()) {
            double fall = data.getLastDeltaY();
            if (fall < -0.5 && player.getHealth() >= player.getMaxHealth() - 0.5) {
                flag(player, data, String.format("airTicks=%d fallDelta=%.3f", data.getAirTicks(), fall));
            }
        }
    }
}
