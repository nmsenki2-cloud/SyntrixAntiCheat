package hu.guardac.check.impl;

import hu.guardac.GuardAC;
import hu.guardac.check.Check;
import hu.guardac.data.PlayerData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ReachCheck extends Check {

    private final ThreadLocal<Entity> target = new ThreadLocal<>();

    public ReachCheck(GuardAC plugin) { super(plugin, "Reach", "A"); }

    public void setTarget(Entity e) { target.set(e); }

    @Override
    public void handle(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("checks.reach.enabled", true)) return;

        Entity t = target.get();
        if (t == null) return;

        double dist = player.getLocation().distance(t.getLocation());
        double max  = plugin.getConfig().getDouble("checks.reach.max-distance", 3.2);

        if (dist > max) {
            flag(player, data, String.format("dist=%.2f max=%.2f", dist, max));
        }

        target.remove();
    }
}
