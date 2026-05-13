package hu.guardac.check;

import hu.guardac.GuardAC;
import hu.guardac.data.PlayerData;
import org.bukkit.entity.Player;

public abstract class Check {

    protected final GuardAC plugin;
    protected final String name;
    protected final String type;

    public Check(GuardAC plugin, String name, String type) {
        this.plugin = plugin;
        this.name   = name;
        this.type   = type;
    }

    public abstract void handle(Player player, PlayerData data);

    protected void flag(Player player, PlayerData data, String detail) {
        if (data.isBypassing()) return;

        data.addViolation(name);
        int vl = data.getViolations(name);

        plugin.getAlertManager().sendAlert(player, name, type, vl, detail);

        int kickVl = plugin.getConfig().getInt("punishments.kick-threshold", 25);
        int banVl  = plugin.getConfig().getInt("punishments.ban-threshold", 60);

        if (vl >= banVl) {
            String cmd = plugin.getConfig()
                .getString("punishments.ban-command", "ban {player}")
                .replace("{player}", player.getName())
                .replace("{check}", name);
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd);
        } else if (vl >= kickVl) {
            player.kickPlayer("§cGuardAC: Cheat észlelve: §b" + name);
        }
    }

    public String getName() { return name; }
    public String getType() { return type; }
}
