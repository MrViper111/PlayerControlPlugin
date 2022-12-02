package dev.mrviper111.playercontrol.utils;

import dev.mrviper111.playercontrol.PlayerControl;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ControlManager implements Listener {

    private final PlayerControl plugin;

    public ControlManager(PlayerControl plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void setFrozen(Player target, boolean frozen) {
        if (frozen) {
            plugin.getFrozenPlayers().add(target.getUniqueId());
        } else {
            plugin.getFrozenPlayers().remove(target.getUniqueId());
        }
    }

    public void stopControlling(UUID uuid) {
        plugin.getControllingPlayers().remove(getController(uuid));
    }

    public UUID getController(UUID uuid) {
        UUID target = plugin.getControllingPlayers().values().stream().filter(id -> id.equals(uuid)).findFirst().orElse(null);
        return plugin.getControllingPlayers().keySet().stream().filter(key ->  plugin.getControllingPlayers().get(key).equals(target)).findFirst().orElse(null);
    }

    public boolean isPlayerControlled(UUID uuid) {
        return  plugin.getControllingPlayers().values().stream().filter(id -> id.equals(uuid)).findFirst().orElse(null) != null;
    }

    public UUID getLinkController(UUID uuid) {
        UUID target = plugin.getLinkedPlayers().values().stream().filter(id -> id.equals(uuid)).findFirst().orElse(null);
        return plugin.getLinkedPlayers().keySet().stream().filter(key -> plugin.getControllingPlayers().get(key).equals(target)).findFirst().orElse(null);
    }

    public UUID getLinkTarget(UUID controller) {
        return plugin.getLinkedPlayers().get(controller);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (plugin.getFrozenPlayers().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }

        // Controller
        if (plugin.getLinkedPlayers().containsKey(event.getPlayer().getUniqueId())) {
            Player target = plugin.getServer().getPlayer(plugin.getLinkedPlayers().get(event.getPlayer().getUniqueId()));
            target.teleport(event.getPlayer().getLocation());
        }

        // Controlee
        if (plugin.getLinkedPlayers().containsValue(event.getPlayer().getUniqueId())) {

        }
    }


    public void implantAntenna(Player controller, Player player) { // TODO: finish this, runnable not really working
        plugin.getControlManager().setFrozen(player, true);

        Location droneTopSpawnLocation = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 50, player.getLocation().getZ());
        Location droneBottomSpawnLocation = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 49.5, player.getLocation().getZ());

        ArmorStand droneTop = (ArmorStand) player.getWorld().spawnEntity(droneTopSpawnLocation, EntityType.ARMOR_STAND);
        ArmorStand droneBottom = (ArmorStand) player.getWorld().spawnEntity(droneBottomSpawnLocation, EntityType.ARMOR_STAND);

        droneTop.setInvulnerable(true);
        droneTop.setVisible(false);
        droneTop.setMarker(true);
        droneTop.getEquipment().setHelmet(new ItemStack(Material.IRON_BLOCK));

        droneBottom.setInvulnerable(true);
        droneBottom.setVisible(false);
        droneBottom.setMarker(true);
        droneBottom.getEquipment().setHelmet(new ItemStack(Material.ANVIL));

        final AtomicBoolean hasReachedGround = new AtomicBoolean(false);

        new BukkitRunnable() {
            @Override
            public void run() {
                droneTop.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, droneTopSpawnLocation, 1);
                droneTop.getWorld().spawnParticle(Particle.SMOKE_NORMAL, droneBottomSpawnLocation, 2);

                if (!hasReachedGround.get()) {
                    droneTopSpawnLocation.setY(droneTopSpawnLocation.getY() - 0.4);
                    droneBottomSpawnLocation.setY(droneBottomSpawnLocation.getY() - 0.4);
                } else {
                    droneTopSpawnLocation.setY(droneTopSpawnLocation.getY() + 0.6);
                    droneBottomSpawnLocation.setY(droneBottomSpawnLocation.getY() + 0.6);
                }

                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    droneTop.teleport(droneTopSpawnLocation);
                    droneBottom.teleport(droneBottomSpawnLocation);
                });

                if (droneBottom.getLocation().getY() < (player.getLocation().getY() + 0.3)) {
                    hasReachedGround.set(true);

                    player.getWorld().playSound(droneBottomSpawnLocation, Sound.BLOCK_PISTON_CONTRACT, 100, 2);
                    player.getWorld().playSound(droneBottomSpawnLocation, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 100, 2);

                    controller.sendMessage(ChatColor.GRAY + "Drone reached target injection site. Implanting antenna...");
                    player.getInventory().setHelmet(new ItemStack(Material.REDSTONE_TORCH));

                    controller.sendMessage(ChatColor.GRAY + "Establishing neural link...");

                    controller.sendMessage(ChatColor.GREEN + "Neural link with " + player.getName() + " has been established!");
                    player.sendMessage(ChatColor.GREEN + "BEEP BOOP... " + ChatColor.GRAY + "A foreign presence has taken over your entire body.");
                }

                if (droneTop.getLocation().getY() > (player.getLocation().getY() + 50)) {
                    plugin.getControlManager().setFrozen(player, false);

                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        droneBottom.remove();
                        droneTop.remove();
                    });

                    cancel();
                }

            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1L);
    }


    public void establishNeuralLink(Player player, Player target) { // TODO: finish this
        plugin.getLinkedPlayers().put(player.getUniqueId(), target.getUniqueId());

        player.sendMessage(ChatColor.GRAY + "To destroy the neural link, run /closecontrollink.");
    }

    public void destroyNeuralLink(Player player, Player target, Location previousLocation) { // TODO: finish this
        plugin.getLinkedPlayers().remove(player.getUniqueId());
    }

}
