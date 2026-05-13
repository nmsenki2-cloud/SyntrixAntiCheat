package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.entity.Player;

public class AimbotCheck extends Check {

    public AimbotCheck(GuardAC plugin) { super(plugin, "Aimbot", "A"); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.aimbot.enabled", true)) return;

        float curYaw   = player.getLocation().getYaw();
        float curPitch = player.getLocation().getPitch();
        float dYaw     = Math.abs(curYaw   - data.getLastYaw());
        float dPitch   = Math.abs(curPitch - data.getLastPitch());

        double max = plugin.getConfig().getDouble("checks.aimbot.max-rotation-speed", 180.0);

        if (dYaw > max || dPitch > max) {
            flag(player, data, String.format("dYaw=%.1f dPitch=%.1f", dYaw, dPitch));
        }

        data.setLastYaw(curYaw);
        data.setLastPitch(curPitch);
        data.setDeltaYaw(dYaw);
        data.setDeltaPitch(dPitch);
    }
}
