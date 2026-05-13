package hu.guardac.manager;

import hu.guardac.GuardAC;
import hu.guardac.check.impl.*;
import hu.guardac.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CheckManager implements Listener {

    private final GuardAC plugin;
    private final Map<UUID, PlayerData> dataMap = new ConcurrentHashMap<>();

    private final SpeedCheck       speed;
    private final FlightCheck      flight;
    private final KillAuraCheck    killaura;
    private final ReachCheck       reach;
    private final NoFallCheck      nofall;
    private final TimerCheck       timer;
    private final ScaffoldCheck    scaffold;
    private final AutoClickerCheck autoclicker;
    private final VelocityCheck    velocity;
    private final PhaseCheck       phase;
    private final InventoryCheck   inventory;
    private final AimbotCheck      aimbot;
    private final BlinkCheck       blink;

    public CheckManager(GuardAC plugin) {
        this.plugin      = plugin;
        speed       = new SpeedCheck(plugin);
        flight      = new FlightCheck(plugin);
        killaura    = new KillAuraCheck(plugin);
        reach       = new ReachCheck(plugin);
        nofall      = new NoFallCheck(plugin);
        timer       = new TimerCheck(plugin);
        scaffold    = new ScaffoldCheck(plugin);
        autoclicker = new AutoClickerCheck(plugin);
        velocity    = new VelocityCheck(plugin);
        phase       = new PhaseCheck(plugin);
        inventory   = new InventoryCheck(plugin);
        aimbot      = new AimbotCheck(plugin);
        blink       = new BlinkCheck(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        dataMap.put(e.getPlayer().getUniqueId(), new PlayerData(e.getPlayer()));
        if (e.getPlayer().isOp()) {
            plugin.getAlertManager().addAlertReceiver(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        dataMap.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData d = dataMap.get(p.getUniqueId());
        if (d == null || d.isBypassing()) return;

        // Csak pozíció változásnál
        if (e.getFrom().getX() == e.getTo().getX()
         && e.getFrom().getY() == e.getTo().getY()
         && e.getFrom().getZ() == e.getTo().getZ()
         && e.getFrom().getYaw() == e.getTo().getYaw()
         && e.getFrom().getPitch() == e.getTo().getPitch()) return;

        d.setLastDeltaX(e.getTo().getX() - e.getFrom().getX());
        d.setLastDeltaY(e.getTo().getY() - e.getFrom().getY());
        d.setLastDeltaZ(e.getTo().getZ() - e.getFrom().getZ());

        timer.handle(p, d);
        speed.handle(p, d);
        flight.handle(p, d);
        nofall.handle(p, d);
        phase.handle(p, d);
        blink.handle(p, d);
        aimbot.handle(p, d);
        inventory.handle(p, d);

        d.setLastLocation(e.getTo().clone());
        d.setLastOnGround(p.isOnGround());
        d.setLastMoveTime(System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) return;
        PlayerData d = dataMap.get(p.getUniqueId());
        if (d == null || d.isBypassing()) return;

        d.setLastTarget(e.getEntity());
        killaura.handle(p, d);
        autoclicker.handle(p, d);
        reach.setTarget(e.getEntity());
        reach.handle(p, d);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVelocity(PlayerVelocityEvent e) {
        PlayerData d = dataMap.get(e.getPlayer().getUniqueId());
        if (d == null) return;
        d.setExpectingVelocity(true);
        d.setVelocitySentTime(System.currentTimeMillis());
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (e.getPlayer().isOnline()) velocity.handle(e.getPlayer(), d);
        }, 2L);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        PlayerData d = dataMap.get(e.getPlayer().getUniqueId());
        if (d == null || d.isBypassing()) return;
        scaffold.handle(e.getPlayer(), d);
    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent e) {
        if (!(e.getPlayer() instanceof Player p)) return;
        PlayerData d = dataMap.get(p.getUniqueId());
        if (d == null) return;
        d.setInventoryOpen(true);
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player p)) return;
        PlayerData d = dataMap.get(p.getUniqueId());
        if (d == null) return;
        d.setInventoryOpen(false);
    }

    public PlayerData getData(Player p)  { return dataMap.get(p.getUniqueId()); }
    public PlayerData getData(UUID uuid) { return dataMap.get(uuid); }
    public Map<UUID, PlayerData> getAll(){ return dataMap; }
}
