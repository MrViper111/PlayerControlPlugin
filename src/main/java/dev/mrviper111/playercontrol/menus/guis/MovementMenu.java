package dev.mrviper111.playercontrol.menus.guis;

import dev.mrviper111.playercontrol.PlayerControl;
import dev.mrviper111.playercontrol.menus.utils.GuiButton;
import dev.mrviper111.playercontrol.menus.utils.GuiMenu;
import dev.mrviper111.playercontrol.menus.utils.GuiType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class MovementMenu extends GuiMenu {

    private PlayerControl plugin;
    private Player target;

    private long targetX;
    private long targetY;
    private long targetZ;

    public MovementMenu(PlayerControl plugin, Player target) {
        this.plugin = plugin;
        this.target = target;

        ItemStack enabledFeather = new ItemStack(Material.FEATHER);
        ItemStack enabledSugar = new ItemStack(Material.SUGAR);
        ItemStack enabledIce = new ItemStack(Material.PACKED_ICE);

        if (target.isFlying()) {
            ItemMeta featherMeta = enabledFeather.getItemMeta();
            featherMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            featherMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            enabledFeather.setItemMeta(featherMeta);
        }

        if (plugin.getJitteringPlayers().contains(target.getUniqueId())) {
            ItemMeta sugarMeta = enabledSugar.getItemMeta();
            sugarMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            sugarMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            enabledSugar.setItemMeta(sugarMeta);
        }

        if (plugin.getFrozenPlayers().contains(target.getUniqueId())) {
            ItemMeta iceMeta = enabledIce.getItemMeta();
            iceMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            iceMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            enabledIce.setItemMeta(iceMeta);
        }

        targetX = Math.round(target.getLocation().getX());
        targetY = Math.round(target.getLocation().getY());
        targetZ = Math.round(target.getLocation().getZ());

        GuiButton locationButton = new GuiButton(ChatColor.GREEN + target.getName(), Material.PAPER)
                .setLore(ChatColor.GRAY + "Current location: " + ChatColor.WHITE + targetX + ", " + targetY + ", " + targetZ)
                .setSlot(4);

        addButton(locationButton);

         new BukkitRunnable() {
            @Override
            public void run() {
                Player player = plugin.getServer().getPlayer(target.getUniqueId());
                ItemStack locationItem = locationButton.getItemStack();
                ItemMeta locationMeta = locationItem.getItemMeta();

                if (player == null || !player.isOnline()) {
                    locationMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Current location: " + ChatColor.RED + "OFFLINE"));
                    locationItem.setItemMeta(locationMeta);

                    cancel();
                    return;
                }

                targetX = Math.round(player.getLocation().getX());
                targetY = Math.round(player.getLocation().getY());
                targetZ = Math.round(player.getLocation().getZ());

                locationMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Current location: " + ChatColor.WHITE + targetX + ", " + targetY + ", " + targetZ));
                locationItem.setItemMeta(locationMeta);

                getInventory().setItem(locationButton.getSlot(), locationItem);
            }
        }.runTaskTimerAsynchronously(plugin, 20L, 20L);

        addButton(new GuiButton(ChatColor.GREEN + "Freeze", Material.PACKED_ICE)
                .setLore(
                        ChatColor.GRAY + "Click to freeze " + target.getName() + ". Doing",
                        ChatColor.GRAY + "this will not allow the player to move.",
                        "",
                        ChatColor.DARK_GRAY + "This option can be toggled."
                )
                .setSlot(10)
                .setMaterial(enabledIce)
        );
        addButton(new GuiButton(ChatColor.GREEN + "North", Material.WHITE_STAINED_GLASS)
                .setLore(ChatColor.GRAY + "Click to move " + target.getName() + " 1 block north.")
                .setSlot(15)
        );
        addButton(new GuiButton(ChatColor.GREEN + "Jitter", Material.SUGAR)
                .setLore(
                        ChatColor.GRAY + "Click to make " + target.getName() + " jitter.",
                        ChatColor.GRAY + "They will erratically move around",
                        ChatColor.GRAY + "for 10 seconds.",
                        "",
                        ChatColor.DARK_GRAY + "This option can be toggled."
                )
                .setSlot(19)
                .setMaterial(enabledSugar)
        );
        addButton(new GuiButton(ChatColor.GREEN + "Fly", Material.FEATHER)
                .setLore(
                        ChatColor.GRAY + "Click to toggle flight for " + target.getName() + ".",
                        "",
                        ChatColor.DARK_GRAY + "This option can be toggled."
                )
                .setSlot(21)
                .setMaterial(enabledFeather)
        );
        addButton(new GuiButton(ChatColor.GREEN + "West", Material.WHITE_STAINED_GLASS)
                .setLore(ChatColor.GRAY + "Click to move " + target.getName() + " 1 block west.")
                .setSlot(23)
        );
        addButton(new GuiButton(ChatColor.GREEN + "Up", Material.NETHER_STAR)
                .setLore(ChatColor.GRAY + "Click to move " + target.getName() + " 1 block up.")
                .setSlot(24)
        );
        addButton(new GuiButton(ChatColor.GREEN + "East", Material.WHITE_STAINED_GLASS)
                .setLore(ChatColor.GRAY + "Click to move " + target.getName() + " 1 block east.")
                .setSlot(25)
        );
        addButton(new GuiButton(ChatColor.GREEN + "Fling", Material.RABBIT_HIDE)
                .setLore(ChatColor.GRAY + "Click to fling " + target.getName() + " upwards.")
                .setSlot(30)
        );
        addButton(new GuiButton(ChatColor.GREEN + "South", Material.WHITE_STAINED_GLASS)
                .setLore(ChatColor.GRAY + "Click to move " + target.getName() + " 1 block south.")
                .setSlot(33)
        );
        addButton(new GuiButton(ChatColor.GREEN + "Teleport Here", Material.ENDER_PEARL)
                .setLore(ChatColor.GRAY + "Click to teleport " + target.getName() + " to you.")
                .setSlot(37)
        );
        addButton(new GuiButton(ChatColor.GREEN + "Back", Material.ARROW)
                .setLore(ChatColor.GRAY + "Click to return back to the main menu.")
                .setSlot(48)
        );
        addButton(new GuiButton(ChatColor.RED + "Close", Material.BARRIER)
                .setLore(
                        ChatColor.GRAY + "Click to close the menu.")
                .setSlot(49)
        );

    }

    @Override
    public String getName() {
        return ChatColor.DARK_GRAY + "Movement Menu | " + target.getName();
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        switch (event.getRawSlot()) {

            case 10: { // Freeze
                if (!plugin.getFrozenPlayers().contains(target.getUniqueId())) {
                    plugin.getControlManager().setFrozen(target, true);
                    event.getWhoClicked().sendMessage(ChatColor.GREEN + target.getName() + " is now frozen!");

                } else {
                    plugin.getControlManager().setFrozen(target, false);
                    event.getWhoClicked().sendMessage(ChatColor.GREEN + target.getName() + " is no longer frozen.");
                }

                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new MovementMenu(plugin, target));
                break;
            }

            case 15: { // North
                target.setVelocity(new Vector(0, 0 , -1/2.6));
                break;
            }

            case 19: { // Jitter
                if (!plugin.getJitteringPlayers().contains(target.getUniqueId())) {
                    plugin.getJitteringPlayers().add(target.getUniqueId());

                    BukkitTask task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                        target.setVelocity(new Vector(
                                ThreadLocalRandom.current().nextDouble(-0.5, 0.5),
                                0,
                                ThreadLocalRandom.current().nextDouble(-0.5, 0.5)
                        ));
                    }, 0, 10L);

                    plugin.getJitteringTasks().put(target.getUniqueId(), task);

                    event.getWhoClicked().sendMessage(ChatColor.GREEN + target.getName() + " is now jittering!");

                } else {
                    plugin.getJitteringTasks().get(target.getUniqueId()).cancel();
                    plugin.getJitteringPlayers().remove(target.getUniqueId());

                    event.getWhoClicked().sendMessage(ChatColor.GREEN + target.getName() + " is no longer jittering.");
                }

                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new MovementMenu(plugin, target));
                break;
            }

            case 21: { // Fly
                target.setFlying(!target.isFlying());
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new MovementMenu(plugin, target));
                break;
            }

            case 23: { // West
                target.setVelocity(new Vector(-1/2.6, 0, 0));
                break;
            }

            case 24: { // Up
                target.setVelocity(new Vector(0, 1/2.6, 0));
                break;
            }

            case 25: { // East
                target.setVelocity(new Vector(1/2.6, 0, 0));
                break;
            }

            case 30: { // Fling
                target.setVelocity(new Vector(0, 30, 0));
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "You have flung " + target.getName() + "!");
                break;
            }

            case 33: { // South
                target.setVelocity(new Vector(0, 0, 1/2.6));
                break;
            }

            case 37: { // Teleport Here
                target.teleport(event.getWhoClicked().getLocation());
                event.getWhoClicked().sendMessage(ChatColor.GREEN + target.getName() + " has been teleported to you!");
                break;
            }

            case 48: { // Back
                event.getWhoClicked().closeInventory();
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new MainMenu(plugin, target));
                break;

            }
            case 49: { // Close
                event.getWhoClicked().closeInventory();
                break;
            }

        }
    }

    @Override
    public GuiType getType() {
        return GuiType.FILLED;
    }
}
