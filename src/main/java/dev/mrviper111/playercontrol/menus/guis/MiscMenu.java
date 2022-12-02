package dev.mrviper111.playercontrol.menus.guis;

import dev.mrviper111.playercontrol.PlayerControl;
import dev.mrviper111.playercontrol.menus.utils.GuiButton;
import dev.mrviper111.playercontrol.menus.utils.GuiMenu;
import dev.mrviper111.playercontrol.menus.utils.GuiType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MiscMenu extends GuiMenu {

    private final PlayerControl plugin;
    private final Player target;

    public MiscMenu(PlayerControl plugin, Player target) {

        this.plugin = plugin;
        this.target = target;

        addButton(new GuiButton(ChatColor.GREEN + "Kill Player", Material.DIAMOND_SWORD)
                .setLore(ChatColor.GRAY + "Click to instantly kill the player.")
                .setSlot(11)
        );
        addButton(new GuiButton(ChatColor.GREEN + "Teleport To", Material.ENDER_PEARL)
                .setLore(ChatColor.GRAY + "Click to teleport to the player.")
                .setSlot(13)
        );
        addButton(new GuiButton(ChatColor.GREEN + "Back", Material.ARROW)
                .setLore(ChatColor.GRAY + "Click to return back to the main menu.")
                .setSlot(48)
        );
        addButton(new GuiButton(ChatColor.RED + "Close", Material.BARRIER)
                .setLore(ChatColor.GRAY + "Click to close the menu.")
                .setSlot(49)
        );
    }

    @Override
    public String getName() {
        return ChatColor.DARK_GRAY + "Misc | " + target.getName();
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        switch (event.getRawSlot()) {
            case 11: {
                target.setHealth(0);
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "You have killed " + target.getName() + "!");
                break;
            }
            case 13: {
                event.getWhoClicked().teleport(target.getLocation());
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "You have teleported to " + target.getName() + "!");
                break;
            }
            case 48: {
                event.getWhoClicked().closeInventory();
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new MainMenu(plugin, target));
                break;
            }
            case 49: {
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
