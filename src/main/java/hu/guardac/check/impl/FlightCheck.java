package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class FlightCheck extends Check {

    public FlightCheck(GuardAC plugin) { super(plugin, "Flight", "A"); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.flight.enabled", true)) return;
        // Jogos repülés → nem flageljük
        if (player.isFlying() || player.getAllowFlight()) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (player.isInsideVehicle()) return;
        if (player.hasPotionEffect(PotionEffectType.SLOW_FALLING)) return;
        if (player.hasPotionEffect(PotionEffectType.JUMP)) return;

        boolean onGround = player.isOnGround();
        double dy = player.getLocation().getY() - data.getLastLocation().getY();

        if (!onGround) {
            data.setAirTicks(data.getAirTicks() + 1);
        } else {
            data.setAirTicks(0);
        }

        int maxAir = plugin.getConfig().getInt("checks.flight.max-air-ticks", 10);

        if (data.getAirTicks() > maxAir && dy > 0.04) {
            flag(player, data, String.format("airTicks=%d dy=%.3f", data.getAirTicks(), dy));
        }

        data.setLastDeltaY(dy);
        data.setLastOnGround(onGround);
    }
}
