package org.kauazs.batatinhafrita.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class GuiManager {
    private HashMap<Player, GUI> playerGUI = new HashMap<>();

    public void setPlayerGUI(Player player, GUI gui) {
        if (gui == null) {
            player.closeInventory();
        }
        playerGUI.put(player, gui);
        player.openInventory(gui.getInvetory());
    }

    public GUI getOpenGUI(Player player) {
        return playerGUI.get(player);
    }

    public void clear(Player player) {
        playerGUI.put(player, null);
    }
}
