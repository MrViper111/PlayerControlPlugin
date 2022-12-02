package dev.mrviper111.playercontrol.menus.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiButton {

    private String name;
    private ItemStack itemStack;
    private List<String> lore;
    private int slot;

    public GuiButton(String name, Material material) {
        this.name = name;
        this.itemStack = new ItemStack(material);
        this.lore = new ArrayList<>();
        this.slot = 0;
    }

    public GuiButton setMaterial(Material material) {
        this.itemStack = new ItemStack(material);
        return this;
    }

    public GuiButton setMaterial(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public Material getMaterial() {
        return this.itemStack.getType();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public GuiButton setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public GuiButton setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public int getSlot() {
        return this.slot;
    }

    public GuiButton setLore(String... lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public ItemStack build() {
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
