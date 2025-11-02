package me.duart.confettiExplosions.commands;

import me.duart.confettiExplosions.ConfettiExplosions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public class ReloadCommand implements CommandExecutor {

    private final ConfettiExplosions plugin;

    public ReloadCommand(ConfettiExplosions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command,@Nonnull String s, @Nonnull String[] strings) {

        if (!sender.hasPermission("confetti.reload")) {
            sender.sendMessage(ChatColor.GOLD + "[" + plugin.getName() + "] " +
                    ChatColor.GREEN + "v: " + plugin.getDescription().getVersion());
            return false;
        }

        plugin.reloadConfig();
        plugin.reloadConfigCache();
        sender.sendMessage(ChatColor.GOLD + "[" + plugin.getName() + "] " + ChatColor.GREEN + "config reloaded.");
        return true;
    }
}
