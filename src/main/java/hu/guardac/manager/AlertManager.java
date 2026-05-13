package hu.guardac.manager;

import hu.guardac.GuardAC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AlertManager {

    private final GuardAC plugin;
    private final Set<UUID> alertReceivers = ConcurrentHashMap.newKeySet();
    private final Set<UUID> verbosePlayers = ConcurrentHashMap.newKeySet();

    private static final Map<String, String> DISPLAY = Map.ofEntries(
        Map.entry("Speed",       "§bSebesség-hack §7(Speed)"),
        Map.entry("Flight",      "§bRepülés §7(Flight)"),
        Map.entry("KillAura",    "§bAuto-támadás §7(KillAura)"),
        Map.entry("Reach",       "§bTávolsági ütés §7(Reach)"),
        Map.entry("NoFall",      "§bEsés-tagadás §7(NoFall)"),
        Map.entry("Timer",       "§bPacket-gyorsítás §7(Timer)"),
        Map.entry("Scaffold",    "§bAuto-Scaffold §7(Scaffold)"),
        Map.entry("AutoClicker", "§bAuto-kattintás §7(AutoClicker)"),
        Map.entry("Blink",       "§bBlink/Teleport §7(Blink)"),
        Map.entry("Velocity",    "§bKnockback-tagadás §7(Velocity)"),
        Map.entry("Phase",       "§bFalonjárás §7(Phase)"),
        Map.entry("Inventory",   "§bInventory-hack §7(Inventory)"),
        Map.entry("Aimbot",      "§bAimbot §7(Aimbot)")
    );

    public AlertManager(GuardAC plugin) {
        this.plugin = plugin;
        Bukkit.getOnlinePlayers().stream()
            .filter(Player::isOp)
            .forEach(p -> alertReceivers.add(p.getUniqueId()));
    }

    public void sendAlert(Player flagged, String check, String type, int vl, String info) {
        if (!plugin.getConfig().getBoolean("alerts.enabled", true)) return;

        String prefix = c(plugin.getConfig().getString("prefix", "&8[&bGuardAC&8]&r"));
        String display = DISPLAY.getOrDefault(check, "§b" + check);

        String msg = prefix
            + " §c" + flagged.getName()
            + " §7cheatel: " + display
            + " §7| vl:§f" + vl
            + " §8[" + type + "]";

        for (UUID id : alertReceivers) {
            Player p = Bukkit.getPlayer(id);
            if (p != null) p.sendMessage(msg);
        }

        for (UUID id : verbosePlayers) {
            Player p = Bukkit.getPlayer(id);
            if (p != null) p.sendMessage("§8[Verbose] §7" + check + " | " + info);
        }

        if (plugin.getConfig().getBoolean("alerts.log-to-console", true)) {
            plugin.getLogger().info("[Alert] " + flagged.getName()
                + " | " + check + " vl/" + vl + " | " + info);
        }
    }

    private String c(String s) { return s.replace("&", "§"); }

    public void addAlertReceiver(UUID id)    { alertReceivers.add(id); }
    public void removeAlertReceiver(UUID id) { alertReceivers.remove(id); }
    public boolean isReceiving(UUID id)      { return alertReceivers.contains(id); }
    public void addVerbose(UUID id)          { verbosePlayers.add(id); }
    public void removeVerbose(UUID id)       { verbosePlayers.remove(id); }
    public boolean isVerbose(UUID id)        { return verbosePlayers.contains(id); }
}
