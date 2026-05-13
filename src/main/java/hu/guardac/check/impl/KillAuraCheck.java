package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.entity.Player;

public class KillAuraCheck extends Check {

    public KillAuraCheck(GuardAC plugin) { super(plugin, "KillAura", "A"); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.killaura.enabled", true)) return;

        // Gyanús: ütés közben nem mozog a kamera
        float dYaw   = data.getDeltaYaw();
        float dPitch = data.getDeltaPitch();

        // Ha valaki minden tickben ugyanolyan szögből üt, az gépies
        if (dYaw == 0f && dPitch == 0f && data.getCpsCount() > 5) {
            flag(player, data, String.format("staticAngle cps=%d", data.getCpsCount()));
        }
    }
}
