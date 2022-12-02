package dev.mrviper111.playercontrol.commands;

import dev.mrviper111.playercontrol.PlayerControl;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CloseControlLinkCommand implements CommandExecutor {

    private PlayerControl plugin;

    public CloseControlLinkCommand(PlayerControl plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can run this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("playercontrol.commands.closecontrollink")) {
            player.sendMessage(ChatColor.RED + "Sorry! You don't have permission to run this command.");
            return true;
        }

        // Actual command

        if (!plugin.getLinkedPlayers().containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "It doesn't look like you're linked with any players.");
        }

        player.sendMessage(ChatColor.GRAY + "Breaking neural link...");

        plugin.getLinkedPlayers().remove(player.getUniqueId());

        player.sendMessage(ChatColor.GREEN + "Neural link broken!");

        return false;
    }
}
