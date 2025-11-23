package org.kauazs.batatinhafrita.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public interface GUI {
    Inventory getInvetory();
    String getName();
    GUI handleClick(Player player, ItemStack itemStack, InventoryView view);
    boolean isInvetory(InventoryView view);
}
