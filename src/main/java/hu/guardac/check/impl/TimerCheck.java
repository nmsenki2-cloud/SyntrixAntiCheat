package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.entity.Player;

public class TimerCheck extends Check {

    public TimerCheck(GuardAC plugin) { super(plugin, "Timer", "A"); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.timer.enabled", true)) return;

        long now = System.currentTimeMillis();
        int max  = plugin.getConfig().getInt("checks.timer.max-packets-per-second", 22);

        if (now - data.getPacketResetTime() > 1000) {
            data.setPacketCount(0);
            data.setPacketResetTime(now);
        }

        data.setPacketCount(data.getPacketCount() + 1);

        if (data.getPacketCount() > max) {
            flag(player, data, String.format("pps=%d max=%d", data.getPacketCount(), max));
        }
    }
}
