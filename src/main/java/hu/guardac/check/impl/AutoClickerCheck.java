package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.entity.Player;

public class AutoClickerCheck extends Check {

    public AutoClickerCheck(GuardAC plugin) { super(plugin, "AutoClicker", "A"); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.autoclicker.enabled", true)) return;

        long now = System.currentTimeMillis();
        int max  = plugin.getConfig().getInt("checks.autoclicker.max-cps", 16);

        if (now - data.getCpsResetTime() > 1000) {
            data.setCpsCount(0);
            data.setCpsResetTime(now);
        }

        data.setCpsCount(data.getCpsCount() + 1);

        if (data.getCpsCount() > max) {
            flag(player, data, String.format("cps=%d max=%d", data.getCpsCount(), max));
        }
    }
}
