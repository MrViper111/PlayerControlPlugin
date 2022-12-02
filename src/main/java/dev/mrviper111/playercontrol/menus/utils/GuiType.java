package dev.mrviper111.playercontrol.menus.utils;

import dev.mrviper111.playercontrol.PlayerControl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;

public enum GuiType {

    FILLED {
        @Override
        public Inventory getInventory(GuiMenu menu) {
            Inventory gui = Bukkit.createInventory(null, menu.getSlots(), menu.getName());

            for (int i = 0; i < menu.getSlots(); i++) {
                gui.setItem(i, fillerItem);
            }

            for (GuiButton button : menu.getButtons()) {
                gui.setItem(button.getSlot(), button.build());
            }

            return gui;
        }
    },
    PAGED {
        @Override
        public Inventory getInventory(GuiMenu menu) {
            Inventory gui = Bukkit.createInventory(null, menu.getSlots(), menu.getName());

            plugin.getGuiManager().getGuiBorder(gui).forEach(slot -> gui.setItem(slot, fillerItem));

            for (GuiButton button : menu.getButtons()) {
                if (button.getSlot() == -1) {
                    gui.addItem(button.build());
                    button.setSlot(gui.first(button.build()));

                    continue;
                }

                gui.setItem(button.getSlot(), button.build());
            }

            if (Arrays.stream(gui.getContents()).filter(Objects::nonNull).count() == gui.getSize()) {
                gui.setItem(nextPageItem.getSlot(), nextPageItem.build());
                gui.setItem(previousPageItem.getSlot(), previousPageItem.build());
            }

            return gui;
        }
    };

    final PlayerControl plugin;
    final GuiButton nextPageItem;
    final GuiButton previousPageItem;
    final ItemStack fillerItem;

    GuiType() {
        plugin = JavaPlugin.getPlugin(PlayerControl.class);

        fillerItem = new GuiButton(ChatColor.RESET + "", Material.GRAY_STAINED_GLASS_PANE).build();

        nextPageItem = new GuiButton(ChatColor.GREEN + "Next Page", Material.ARROW)
                .setLore(ChatColor.GRAY + "Click to go to the next page.")
                .setSlot(53);

        previousPageItem = new GuiButton(ChatColor.GREEN + "Previous Page", Material.ARROW)
                .setLore(ChatColor.GRAY + "Click to go to the previous page.")
                .setSlot(45);
    }

    public abstract Inventory getInventory(GuiMenu menu);

}
