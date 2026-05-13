package hu.guardac;

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

        getServer().getPluginManager().registerEvents(new PacketListener(this), this);

        getCommand("guardac").setExecutor(new GuardCommand(this));

        getLogger().info("GuardAC sikeresen elindult!");
    }

    @Override
    public void onDisable() {
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
