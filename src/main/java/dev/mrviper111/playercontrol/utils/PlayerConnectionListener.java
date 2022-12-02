package dev.mrviper111.playercontrol.utils;

import dev.mrviper111.playercontrol.PlayerControl;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final PlayerControl plugin;

    public PlayerConnectionListener(PlayerControl plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getControllingPlayers().containsKey(event.getPlayer().getUniqueId())) {
            plugin.getControlManager().stopControlling(event.getPlayer().getUniqueId());
            plugin.getControllingPlayers().remove(plugin.getControlManager().getController(event.getPlayer().getUniqueId()));
        }

        plugin.getLinkedPlayers().remove(event.getPlayer().getUniqueId());
        plugin.getLinkedPlayers().remove(plugin.getControlManager().getLinkController(event.getPlayer().getUniqueId()));

        if (plugin.getControlManager().isPlayerControlled(event.getPlayer().getUniqueId())) {
            Player controller = plugin.getServer().getPlayer(plugin.getControlManager().getController(event.getPlayer().getUniqueId()));
            controller.sendMessage(ChatColor.RED + "Uh oh! The remote connection to " + event.getPlayer().getName() + " has been spoiled!");
            plugin.getControlManager().stopControlling(event.getPlayer().getUniqueId());
        }

        if (plugin.getFrozenPlayers().contains(event.getPlayer().getUniqueId())) {
            plugin.getFrozenPlayers().remove(event.getPlayer().getUniqueId());
        }

        if (plugin.getJitteringPlayers().contains(event.getPlayer().getUniqueId())) {
            plugin.getJitteringPlayers().remove(event.getPlayer().getUniqueId());
        }

    }

}
