package org.kauazs.batatinhafrita.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kauazs.batatinhafrita.managers.GameManager;
import org.kauazs.batatinhafrita.managers.GameState;
import org.kauazs.batatinhafrita.utils.Colorize;

import java.util.List;

public class MenuGUI implements GUI {
    private GameManager gameManager;
    public MenuGUI(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    @Override
    public Inventory getInvetory() {
        Inventory inventory = Bukkit.createInventory(null, 9, getName());
        ItemStack itemStack = new ItemStack(Material.REDSTONE_TORCH, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setItemName(ChatColor.GREEN + "Iniciar Partida");
        itemMeta.setLore(List.of(
                ChatColor.GRAY + "Comece a partida clicando aqui."
        ));
        itemStack.setItemMeta(itemMeta);

        ItemStack swordStack = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta swordMeta = swordStack.getItemMeta();
        swordMeta.setItemName(ChatColor.GREEN + "Guardas");
        String guardsEnabled = gameManager.guardsEnabled ? Colorize.format("&a&l- Ativado") : Colorize.format("&c&l- Desativado");
        swordMeta.setLore(List.of(
                ChatColor.GRAY + "Ative/Desative os guardas clicando aqui",
                "",
                guardsEnabled
        ));
        swordStack.setItemMeta(swordMeta);

        inventory.addItem(itemStack);
        inventory.addItem(swordStack);
        return inventory;
    }

    @Override
    public String getName() {
        return "Menu - Admin";
    }

    @Override
    public GUI handleClick(Player player, ItemStack itemStack, InventoryView view) {
        if (Colorize.stripColor(itemStack.getItemMeta().getItemName()).equalsIgnoreCase("iniciar partida")) {
            if (gameManager.playersPlaying.size() == 0) {
                player.sendMessage(ChatColor.RED + "Não é possível iniciar a partida! Não há jogadores.");
                return null;
            }
            gameManager.setGameState(GameState.STARTING);
            player.sendMessage(ChatColor.GRAY + "[Iniciando partida]");
        } else if (Colorize.stripColor(itemStack.getItemMeta().getItemName()).equalsIgnoreCase("guardas")) {
            if (gameManager.guardsEnabled) {
                gameManager.guardsEnabled = false;
                player.sendMessage(Colorize.format("&cGuardas desativados."));
            } else {
                gameManager.guardsEnabled = true;
                player.sendMessage(Colorize.format("&aGuardas ativados. &eUtilize /guarda add (nick)"));
            }
        }
        return null;
    }

    @Override
    public boolean isInvetory(InventoryView view) {
        return false;
    }
}
