package org.kauazs.batatinhafrita.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kauazs.batatinhafrita.managers.GameManager;
import org.kauazs.batatinhafrita.managers.GameState;

public class GuardsCommand implements CommandExecutor {

    private GameManager gameManager;
    public GuardsCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = ((Player) sender).getPlayer();

        if (!player.hasPermission("batatinha.admin")) {
            player.sendMessage(ChatColor.RED + "Sem permissão.");
            return false;
        }
        if (!gameManager.gameState.equals(GameState.WAITING)) {
            player.sendMessage(ChatColor.RED + "Não é possível adicionar um guarda! A partida está em andamento.");
            return false;
        }
        if (!gameManager.guardsEnabled) {
            player.sendMessage(ChatColor.RED + "Os guardas estão desativados! Ative-os pelo menu.");
            return false;
        }
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Uso incorreto, utilize /guardas add (nick) ou /guardas remover (nick)");
            return false;
        }

        if (args[0].equals("add")) {
            if (args[1] == null) {
                player.sendMessage(ChatColor.RED + "Informe o player!");
                return false;
            }

            Player newGuard = Bukkit.getPlayer(args[1]);
            if (newGuard == null) {
                player.sendMessage(ChatColor.RED + "Player inválido!");
                return false;
            }
            if (gameManager.guards.contains(player)) {
                player.sendMessage(ChatColor.RED + "Esse player já é um guarda.");
                return false;
            }

            player.sendMessage(ChatColor.GREEN + "Guarda setado!");
            gameManager.addGuard(newGuard);
            return true;
        } else if (args[0].equals("remove")) {
            if (args[1] == null) {
                player.sendMessage(ChatColor.RED + "Informe o player!");
                return false;
            }

            Player newGuard = Bukkit.getPlayer(args[1]);
            if (newGuard == null) {
                player.sendMessage(ChatColor.RED + "Player inválido!");
                return false;
            }
            if (!gameManager.guards.contains(player)) {
                player.sendMessage(ChatColor.RED + "Esse player não é um guarda.");
                return false;
            }

            player.sendMessage(ChatColor.GREEN + "Guarda removido!");
            gameManager.removeGuard(newGuard);
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "Subcomando inválido! Utilize /guardas add (nick) ou /guardas remover (nick)");
            return false;
        }
    }
}
