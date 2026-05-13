package hu.guardac.command;

import hu.guardac.GuardAC;
import hu.guardac.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuardCommand implements CommandExecutor {

    private final GuardAC plugin;
    private static final String P = "§8[§bGuardAC§8]§r ";

    public GuardCommand(GuardAC plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (args.length == 0) {
            s.sendMessage(P + "§bGuardAC §7v1.0 — parancsok:");
            s.sendMessage("§7/guardac alerts §8— §falert be/ki");
            s.sendMessage("§7/guardac verbose §8— §frészletes log be/ki");
            s.sendMessage("§7/guardac bypass <játékos> §8— §fbypass be/ki");
            s.sendMessage("§7/guardac kick <játékos> §8— §fmanuális kick");
            s.sendMessage("§7/guardac info <játékos> §8— §fviolation statisztika");
            s.sendMessage("§7/guardac reset <játékos> §8— §fviolation törlés");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "alerts" -> {
                if (!s.hasPermission("guardac.alerts")) { s.sendMessage(P + "§cNincs jogod!"); return true; }
                if (!(s instanceof Player p)) { s.sendMessage(P + "§cCsak játékosként!"); return true; }
                boolean on = !plugin.getAlertManager().isReceiving(p.getUniqueId());
                if (on) plugin.getAlertManager().addAlertReceiver(p.getUniqueId());
                else    plugin.getAlertManager().removeAlertReceiver(p.getUniqueId());
                p.sendMessage(P + "Alertek: " + (on ? "§abekapcsolva" : "§ckikapcsolva"));
            }
            case "verbose" -> {
                if (!s.hasPermission("guardac.verbose")) { s.sendMessage(P + "§cNincs jogod!"); return true; }
                if (!(s instanceof Player p)) { s.sendMessage(P + "§cCsak játékosként!"); return true; }
                boolean on = !plugin.getAlertManager().isVerbose(p.getUniqueId());
                if (on) plugin.getAlertManager().addVerbose(p.getUniqueId());
                else    plugin.getAlertManager().removeVerbose(p.getUniqueId());
                p.sendMessage(P + "Verbose: " + (on ? "§abekapcsolva" : "§ckikapcsolva"));
            }
            case "bypass" -> {
                if (!s.hasPermission("guardac.bypass")) { s.sendMessage(P + "§cNincs jogod!"); return true; }
                if (args.length < 2) { s.sendMessage(P + "§fHasználat: /guardac bypass <játékos>"); return true; }
                Player t = Bukkit.getPlayer(args[1]);
                if (t == null) { s.sendMessage(P + "§cNem található!"); return true; }
                PlayerData d = plugin.getCheckManager().getData(t);
                if (d == null) return true;
                d.setBypassing(!d.isBypassing());
                s.sendMessage(P + t.getName() + " bypass: " + (d.isBypassing() ? "§abekapcsolva" : "§ckikapcsolva"));
            }
            case "kick" -> {
                if (!s.hasPermission("guardac.kick")) { s.sendMessage(P + "§cNincs jogod!"); return true; }
                if (args.length < 2) { s.sendMessage(P + "§fHasználat: /guardac kick <játékos>"); return true; }
                Player t = Bukkit.getPlayer(args[1]);
                if (t == null) { s.sendMessage(P + "§cNem található!"); return true; }
                t.kickPlayer("§cGuardAC: Staff kirúgta");
                s.sendMessage(P + "§a" + t.getName() + " kirúgva.");
            }
            case "info" -> {
                if (!s.hasPermission("guardac.alerts")) { s.sendMessage(P + "§cNincs jogod!"); return true; }
                if (args.length < 2) { s.sendMessage(P + "§fHasználat: /guardac info <játékos>"); return true; }
                Player t = Bukkit.getPlayer(args[1]);
                if (t == null) { s.sendMessage(P + "§cNem található!"); return true; }
                PlayerData d = plugin.getCheckManager().getData(t);
                if (d == null) return true;
                s.sendMessage("§8§m══════════════════════════");
                s.sendMessage(P + "§f" + t.getName() + " §7cheat statisztika:");
                var vls = d.getAllViolations();
                if (vls.isEmpty()) s.sendMessage("  §7Nincs flag.");
                else vls.forEach((chk, vl) -> s.sendMessage("  §b" + chk + " §8» §fvl/" + vl));
                s.sendMessage("§8§m══════════════════════════");
            }
            case "reset" -> {
                if (!s.hasPermission("guardac.kick")) { s.sendMessage(P + "§cNincs jogod!"); return true; }
                if (args.length < 2) { s.sendMessage(P + "§fHasználat: /guardac reset <játékos>"); return true; }
                Player t = Bukkit.getPlayer(args[1]);
                if (t == null) { s.sendMessage(P + "§cNem található!"); return true; }
                PlayerData d = plugin.getCheckManager().getData(t);
                if (d == null) return true;
                d.resetAllViolations();
                s.sendMessage(P + "§a" + t.getName() + " összes flagje törölve.");
            }
            default -> s.sendMessage(P + "§fIsmeretlen parancs. /guardac");
        }
        return true;
    }
}
