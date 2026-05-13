package hu.guardac;

import com.github.retrooper.packetevents.PacketEvents;
import hu.guardac.manager.AlertManager;
import hu.guardac.manager.CheckManager;
import hu.guardac.command.GuardCommand;
import hu.guardac.listener.PacketListener;
import org.bukkit.plugin.java.JavaPlugin;

public class GuardAC extends JavaPlugin {

    private static GuardAC instance;
    private CheckManager checkManager;
    private AlertManager alertManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.alertManager = new AlertManager(this);
        this.checkManager = new CheckManager(this);

        PacketEvents.getAPI().getEventManager().registerListener(new PacketListener(this));
        PacketEvents.getAPI().init();

        getCommand("guardac").setExecutor(new GuardCommand(this));

        getLogger().info("GuardAC sikeresen elindult!");
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
        getLogger().info("GuardAC leállt.");
    }

    public static GuardAC getInstance() {
        return instance;
    }

    public CheckManager getCheckManager() {
        return checkManager;
    }

    public AlertManager getAlertManager() {
        return alertManager;
    }
}
