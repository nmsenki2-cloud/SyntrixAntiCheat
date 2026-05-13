package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PhaseCheck extends Check {

    public PhaseCheck(GuardAC plugin) { super(plugin, "Phase", "A"); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.phase.enabled", true)) return;
        if (player.isFlying() || player.getAllowFlight()) return;

        Location loc  = player.getLocation();
        Material feet = loc.getBlock().getType();
        Material head = loc.clone().add(0, 1, 0).getBlock().getType();

        if (isSolid(feet) || isSolid(head)) {
            flag(player, data, String.format("feet=%s head=%s", feet, head));
        }
    }

    private boolean isSolid(Material m) {
        return m.isSolid()
            && m != Material.LADDER && m != Material.VINE
            && m != Material.WATER  && m != Material.LAVA
            && !m.name().contains("SIGN") && !m.name().contains("DOOR")
            && !m.name().contains("FENCE_GATE") && !m.name().contains("TRAPDOOR");
    }
}
