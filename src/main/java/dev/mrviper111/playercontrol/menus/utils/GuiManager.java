package dev.mrviper111.playercontrol.menus.utils;

import dev.mrviper111.playercontrol.PlayerControl;
import dev.mrviper111.playercontrol.utils.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class GuiManager implements Listener {

    private final PlayerControl plugin;
    private final Map<UUID, GuiMenu> menus;

    public GuiManager(PlayerControl plugin) {
        this.plugin = plugin;
        this.menus = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void displayMenu(HumanEntity player, GuiMenu menu) {
        try {
            Inventory inventory = menu.getInventory();

            player.openInventory(inventory);
            menu.setInventory(inventory);

            menus.remove(player.getUniqueId());
            menus.put(player.getUniqueId(), menu);
        } catch (Exception error) {
            Logger.logError("An error occurred whilst attempting to open the " + menu.getName() + " menu.", error);
            player.sendMessage(ChatColor.RED + "Uh oh! It seems something went wrong while opening this menu.");
        }
    }

    public Set<Integer> getGuiBorder(Inventory menu) {
        Set<Integer> slots = new HashSet<>();
        AtomicInteger slot = new AtomicInteger(-1);

        IntStream.range(0, menu.getSize() / 9).forEach(row -> IntStream.range(0, 9).forEach(column -> {
            slot.incrementAndGet();

            if (row != 0 && row != ((menu.getSize() / 9) - 1) && column != 0 && column != 8) {
                return;
            }

            slots.add(slot.get());
        }));

        return slots;
    }

    @EventHandler(ignoreCancelled=true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isInventory(event.getWhoClicked().getUniqueId(), event.getInventory())) {
            return;
        }

        event.setCancelled(true);

        callButton(event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!isInventory(event.getWhoClicked().getUniqueId(), event.getInventory())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        menus.remove(event.getPlayer().getUniqueId());
    }

    private void callButton(InventoryClickEvent event) {
        menus.get(event.getWhoClicked().getUniqueId()).callButton(event);
    }

    private boolean isInventory(UUID player, Inventory inventory) {
        return menus.get(player) == null ? false : menus.get(player).getInventory().equals(inventory);
    }

}
