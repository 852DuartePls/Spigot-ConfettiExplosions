package me.duart.confettiExplosions;

import me.duart.confettiExplosions.commands.ReloadCommand;
import me.duart.confettiExplosions.listener.ExplosionListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfettiExplosions extends JavaPlugin {

    public boolean creepersEnabled;
    public boolean tntEnabled;
    public boolean tntMinecartEnabled;
    public boolean fireballEnabled;
    public boolean enderCrystalEnabled;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfigCache();

        PluginCommand command = getCommand("confettireload");
        if (command != null) command.setExecutor(new ReloadCommand(this));

        getServer().getPluginManager().registerEvents(new ExplosionListener(this), this);
        this.getLogger().info("Confetti Explosions enabled!");
    }

    public void reloadConfigCache() {
        creepersEnabled = getConfig().getBoolean("explosions.creeper", true);
        tntEnabled = getConfig().getBoolean("explosions.tnt", true);
        tntMinecartEnabled = getConfig().getBoolean("explosions.tnt-minecart", true);
        fireballEnabled = getConfig().getBoolean("explosions.fireball", true);
        enderCrystalEnabled = getConfig().getBoolean("explosions.ender-crystal", true);

        if (!creepersEnabled && !tntEnabled && !tntMinecartEnabled && !fireballEnabled && !enderCrystalEnabled) {
            this.getLogger().warning("No explosion types are enabled. Plugin is as good as disabled.");
        }
    }
}
