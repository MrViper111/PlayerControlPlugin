package dev.mrviper111.playercontrol.commands;

import dev.mrviper111.playercontrol.PlayerControl;
import dev.mrviper111.playercontrol.menus.guis.MainMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ControlPlayerCommand implements CommandExecutor {

    private PlayerControl plugin;
    private boolean alertTarget;


    public ControlPlayerCommand(PlayerControl plugin) {
        this.plugin = plugin;
        this.alertTarget = true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can run this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("playercontrol.commands.controlplayer") || player.getName().contains("FlamesHyper")) {
            player.sendMessage(ChatColor.RED + "Sorry! You don't have permission to run this command.");
            return true;
        }

        // Actual command

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Invalid arguments! Usage: /controlplayer <player> [alertTarget]");
            return true;
        }

        if (args.length > 1) {
            alertTarget = Boolean.parseBoolean(args[1]);
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "This player could not be found. Are they offline?");
            return true;
        }

        plugin.getGuiManager().displayMenu(player, new MainMenu(plugin, target));
        plugin.getControllingPlayers().put(player.getUniqueId(), target.getUniqueId());

        player.sendMessage(ChatColor.GRAY + "Attaching control antenna to " + target.getName() + "...");
        player.sendMessage(ChatColor.GRAY + "Establishing remote connection...");
        player.sendMessage(ChatColor.GREEN + "You are now controlling " + target.getName() + "!");

        if (alertTarget) target.sendMessage(ChatColor.GREEN + "BEEP BOOP... " + ChatColor.GRAY + "A foreign presence has taken control.");

        return false;
    }

}
