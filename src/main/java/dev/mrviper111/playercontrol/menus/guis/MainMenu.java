package dev.mrviper111.playercontrol.menus.guis;

import dev.mrviper111.playercontrol.PlayerControl;
import dev.mrviper111.playercontrol.menus.utils.GuiButton;
import dev.mrviper111.playercontrol.menus.utils.GuiMenu;
import dev.mrviper111.playercontrol.menus.utils.GuiType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MainMenu extends GuiMenu {

    private final PlayerControl plugin;
    private Player target;

    public MainMenu(PlayerControl plugin, Player target) {

        this.plugin = plugin;
        this.target = target;

        addButton(new GuiButton(ChatColor.GREEN + "Control", Material.REDSTONE_TORCH)
                .setLore(
                        ChatColor.GRAY + "Click to control the player.",
                        "",
                        ChatColor.GRAY + "By doing this you will \"become\" the",
                        ChatColor.GRAY + "player and control many aspects of",
                        ChatColor.GRAY + "their gameplay."
                )
                .setSlot(13)
        );
        addButton(new GuiButton(ChatColor.GREEN + "Movement", Material.NETHER_STAR)
                .setLore(
                        ChatColor.GRAY + "Click to view all movement related",
                        ChatColor.GRAY + "options."
                )
                .setSlot(19)
        );
        addButton(new GuiButton(ChatColor.GREEN + "Effects", Material.BLAZE_POWDER)
                .setLore(
                        ChatColor.GRAY + "Click to view all effects that can",
                        ChatColor.GRAY + "be applied to the player."
                )
                .setSlot(25)
        );
        addButton(new GuiButton(ChatColor.GREEN + "Misc", Material.SLIME_BALL)
                .setLore(ChatColor.GRAY + "Click to view all other options.")
                .setSlot(31)
        );
        addButton(new GuiButton(ChatColor.RED + "Close", Material.BARRIER)
                .setLore(ChatColor.GRAY + "Click to close the menu.")
                .setSlot(49)
        );
    }

    @Override
    public String getName() {
        return ChatColor.DARK_GRAY + "Controlling " + target.getName() + "...";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        switch (event.getRawSlot()) {

            case 13: {
                HumanEntity player = event.getWhoClicked();
                player.closeInventory();

                player.sendMessage(ChatColor.GRAY + "Redirecting primary injector drone to " + ChatColor.RESET + Math.round(player.getLocation().getX()) + ", " + Math.round(player.getLocation().getY()) + ", " + Math.round(player.getLocation().getZ()) + ChatColor.GRAY + "...");

                Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getControlManager().implantAntenna((Player) event.getWhoClicked(), target), 40L);
                break;
            }

            case 19: {
                event.getWhoClicked().closeInventory();
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new MovementMenu(plugin, target));
                break;
            }
            case 31: {
                event.getWhoClicked().closeInventory();
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new MiscMenu(plugin, target));
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
