package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class SpeedCheck extends Check {

    public SpeedCheck(GuardAC plugin) { super(plugin, "Speed", "A"); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.speed.enabled", true)) return;
        if (player.isFlying() || player.getAllowFlight() || player.isInsideVehicle()) return;

        Location now  = player.getLocation();
        Location last = data.getLastLocation();
        if (last == null) return;

        double dx = now.getX() - last.getX();
        double dz = now.getZ() - last.getZ();
        double speed = Math.sqrt(dx * dx + dz * dz);

        double max = plugin.getConfig().getDouble("checks.speed.max-speed", 0.36);
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            int amp = player.getPotionEffect(PotionEffectType.SPEED).getAmplifier();
            max += (amp + 1) * 0.085;
        }

        if (speed > max && player.isOnGround()) {
            flag(player, data, String.format("speed=%.3f max=%.3f", speed, max));
        }
    }
}
