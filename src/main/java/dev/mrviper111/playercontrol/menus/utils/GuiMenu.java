package dev.mrviper111.playercontrol.menus.utils;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;
import java.util.Set;

public abstract class GuiMenu {
    private GuiType type;
    private final Set<GuiButton> buttons;
    private Inventory inventory;

    public GuiMenu() {
        this.buttons = new HashSet<>();
        this.inventory = null;
    }

    public abstract String getName();

    public abstract int getSlots();

    public abstract void callButton(InventoryClickEvent event);

    public abstract GuiType getType();

    public void addButton(GuiButton button) {
        this.buttons.add(button);
    }

    public Set<GuiButton> getButtons() {
        return this.buttons;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory != null ? inventory : getType().getInventory(this);
    }

}
