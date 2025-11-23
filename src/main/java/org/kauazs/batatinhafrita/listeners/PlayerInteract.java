package org.kauazs.batatinhafrita.listeners;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.kauazs.batatinhafrita.gui.GUI;
import org.kauazs.batatinhafrita.gui.MenuGUI;
import org.kauazs.batatinhafrita.managers.GameManager;
import org.kauazs.batatinhafrita.utils.Colorize;

public class PlayerInteract implements Listener {

    private GameManager gameManager;
    public PlayerInteract(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (Colorize.stripColor(event.getItem().getItemMeta().getItemName()).equals("MENU")) {
            gameManager.guiManager.setPlayerGUI(event.getPlayer(), new MenuGUI(gameManager));
        }
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;
        Player player = (Player) event.getWhoClicked();

        GUI gui = gameManager.guiManager.getOpenGUI(player);
        if (gui == null) return;

        event.setCancelled(true);
        GUI newGui = gui.handleClick(player, event.getCurrentItem(), event.getView());
        event.getView().close();

        if (newGui != null) {
            gameManager.guiManager.setPlayerGUI(player, newGui);
        }
    }

    @EventHandler
    public void onCloseInvetory(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        GUI gui = gameManager.guiManager.getOpenGUI(player);
        if (gui == null) return;

        gameManager.guiManager.clear(player);
    }
}
