package org.kauazs.batatinhafrita.managers;


import com.sk89q.worldedit.MaxChangedBlocksException;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kauazs.batatinhafrita.BatatinhaFrita;
import org.kauazs.batatinhafrita.gui.GuiManager;
import org.kauazs.batatinhafrita.teams.TeamManager;
import org.kauazs.batatinhafrita.teams.TeamsType;
import org.kauazs.batatinhafrita.utils.Colorize;
import org.kauazs.batatinhafrita.utils.WeWorld;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private BatatinhaFrita batatinhaFrita;

    private GameTask gameTask;

    @Getter
    public ConfigManager configManager;
    @Getter
    public GuiManager guiManager;
    @Getter
    public TeamManager teamManager;
    @Getter
    public GameState gameState = GameState.WAITING;
    @Setter @Getter
    public RunState runState;

    @Getter @Setter
    public boolean guardsEnabled = false;
    @Getter
    public List<Player> guards = new ArrayList<>();

    @Getter
    public List<Player> playersPlaying = new ArrayList<>();
    @Getter
    public List<Player> playersFinished = new ArrayList<>();
    @Getter
    public List<Player> playersMoved = new ArrayList<>();

    public GameManager(BatatinhaFrita plugin) {
        this.batatinhaFrita = plugin;
        this.guiManager = new GuiManager();

        this.configManager = new ConfigManager(plugin);
        this.teamManager = new TeamManager();
        this.gameTask = new GameTask(this);
        this.gameTask.runTaskTimer(plugin, 0, 20);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void addPlayer(Player player) {
        if (playersPlaying.contains(player)) return;
        playersPlaying.add(player);
        Integer playerNumber = playersPlaying.size();

        sendMessageForAll("&6&lBatatinha Frita &f| &eJogador [&6" + playerNumber + "&e] &b" + player.getName() + " &aentrou na rodada!");

        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setGlowing(false);
        player.teleport(player.getWorld().getSpawnLocation());

        teamManager.setPlayerTeam(player, TeamsType.PLAYER);
    }


    public void addPlayerFinished(Player player) {
        playersFinished.add(player);
        removePlayer(player);

        sendMessageForAll("&6&lBatatinha Frita &f| &eJogador " + player.getName() + " &epassou!");
    }

    public void addGuard(Player player) {
        if (guards.contains(player)) return;
        teamManager.setPlayerTeam(player, TeamsType.GUARD);
        removePlayer(player);

        player.sendMessage(Colorize.format("&6&lBatatinha Frita 123 &f| &eVocê agora é um guarda! Sua função é eliminar os jogadores que se mexerem"));
        player.setAllowFlight(true);
        player.setFlying(true);
        guards.add(player);
    }
    public void sendMessageGuards(String msg) {
        for(Player player : guards) {
            player.sendMessage(Colorize.format(msg));
        }
    }

    public void addPlayerMoved(Player player) {
        if (playersMoved.contains(player)) return;
        player.setGlowing(true);
        playersMoved.add(player);
        sendMessageGuards("&c&lGUARDA! &eelimine o jogador &c" + player.getName());
    }
    public void removeGuard(Player player) {
        if (!guards.contains(player)) return;

        teamManager.setPlayerTeam(player, TeamsType.GUARD);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.teleport(player.getWorld().getSpawnLocation());
        player.sendMessage(ChatColor.RED + "Você não é mais um guarda.");
        guards.remove(player);
    }

    public boolean playerIsFinished(Player player) {
        return playersFinished.contains(player);
    }

    public void removePlayerFinished(Player player) {
        playersFinished.remove(player);
    }

    public void removePlayer(Player player) {
        playersPlaying.remove(player);
    }

    public boolean playerHasPlaying(Player player) {
        return playersPlaying.contains(player);
    }

    public void sendMessageForAll(String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Colorize.format(msg));
        }
    }

    public void sendMessageActionForAll(String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Colorize.format(msg)));
        }
    }

    public void playSoundForAll(Sound sound, float vol, float pitch) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, vol, pitch);
        }
    }

    public void sendTitleForAll(String title1, String title2) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (playerIsFinished(player)) continue;
            player.sendTitle(Colorize.format(title1), Colorize.format(title2), 0, 200, 0);
        }
    }

    public List<Player> getPlayersPlaying() {
        return  playersPlaying;
    }

    public void resetGame() throws MaxChangedBlocksException {
        List<String> posWall = configManager.getPosWall();
        playersPlaying = new ArrayList<>();
        playersFinished = new ArrayList<>();
        playersMoved = new ArrayList<>();
        guards = new ArrayList<>();
        guardsEnabled = false;

        setGameState(GameState.WAITING);


        WeWorld.restoreWall(Bukkit.getWorld("world"), posWall.get(0), posWall.get(1), "barrier");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setGlowing(false);
            player.setGameMode(GameMode.SURVIVAL);

            teamManager.removeScoreboard(player);
            teamManager.setPlayerTeam(player, TeamsType.PLAYER);
            addPlayer(player);
            player.teleport(player.getWorld().getSpawnLocation());

            if (player.isOp()) {
                ItemStack itemStack = new ItemStack(Material.CLOCK, 1);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setItemName(ChatColor.RED + "" + ChatColor.BOLD + "MENU");
                itemStack.setItemMeta(itemMeta);

                player.getInventory().addItem(itemStack);
            }
        }
    }

}
