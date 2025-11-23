package org.kauazs.batatinhafrita.managers;

import com.sk89q.worldedit.MaxChangedBlocksException;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.kauazs.batatinhafrita.utils.Fireworks;
import org.kauazs.batatinhafrita.utils.WeWorld;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GameTask extends BukkitRunnable {

    private GameManager gameManager;
    private int counterStarting = 10;
    private int counterEnd = 180;
    private int counterRun = 4;
    private int delayReturn;
    private int counterReset = 15;
    private List<String> posWalls;
    public GameTask(GameManager gameManager) {
        this.gameManager = gameManager;

         this.posWalls = gameManager.configManager.getPosWall();
         this.delayReturn = gameManager.guardsEnabled ? 3 : 5;
    }

    @Override
    public void run() {

        switch (gameManager.gameState) {
            case STARTING:
                counterStarting--;
                gameManager.sendMessageActionForAll(ChatColor.YELLOW + "Rodada começando em " + counterStarting + " segundos...");

                if (counterStarting <= 0) {
                    gameManager.setGameState(GameState.PLAYING);
                    gameManager.sendMessageForAll("&aPartida iniciando...");

                    if (gameManager.guardsEnabled) {
                        for (Player player : gameManager.guards) {
                            ItemStack bow = new ItemStack(Material.BOW);
                            ItemMeta meta = bow.getItemMeta();
                            meta.setDisplayName(ChatColor.DARK_PURPLE + "Arco OP");
                            bow.setItemMeta(meta);

                            ItemStack arrow = new ItemStack(Material.ARROW, 128);
                            player.getInventory().clear();
                            player.getInventory().addItem(bow, arrow);
                        }
                    }
                    emitGreenLight();

                    gameManager.sendMessageForAll("&6&lBatatinha Frita 123...");
                    for (Player player : gameManager.getPlayersPlaying()) {
                        player.getInventory().clear();
                        player.setGameMode(GameMode.SURVIVAL);
                        player.teleport(player.getWorld().getSpawnLocation());


                        try {
                            WeWorld.deleteWall(player.getWorld(), posWalls.get(0), posWalls.get(1));
                        } catch (MaxChangedBlocksException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                break;
            case PLAYING:
                if (gameManager.playersPlaying.size() == 0) {
                    gameManager.setGameState(GameState.RESET);
                }

                counterEnd--;
                gameManager.sendMessageActionForAll("&cTempo restante: " + formatTime(counterEnd));
                if (counterEnd <= 0) {
                    for (Player player : gameManager.getPlayersPlaying()) {
                        player.getWorld().strikeLightningEffect(player.getLocation());
                        player.sendMessage(ChatColor.RED + "Você foi eliminado pelo tempo.");
                        gameManager.sendMessageForAll(ChatColor.RED + player.getName() + " foi eliminado pelo tempo.");
                        gameManager.removePlayer(player);
                        player.setGameMode(GameMode.SPECTATOR);
                    }

                    gameManager.setGameState(GameState.RESET);
                }
                counterRun--;
                if (counterRun - 1 == 0) {
                    emitRedLight();
                }

                if (counterRun <= 0) {
                    gameManager.runState = RunState.STOP;
                    delayReturn--;

                }

                if (delayReturn <= 0) {
                    int newCounterRun = ThreadLocalRandom.current().nextInt(2, 7);
                    counterRun = newCounterRun;
                    delayReturn = gameManager.guardsEnabled ? 3 : 5;
                    gameManager.sendMessageForAll("&6&lBatatinha Frita 123...");
                    emitGreenLight();
                }

            break;
            case RESET:
                if (counterReset == 15) {
                    gameManager.sendMessageForAll("");
                    gameManager.sendMessageForAll("&6&lBatatinha Frita &f| &e&lJogo Finalizado");
                    String finishedPlayers = gameManager.playersFinished.isEmpty()
                            ? "Nenhum jogador passou"
                            : gameManager.playersFinished.stream()
                            .map(pl -> pl.getName())
                            .collect(Collectors.joining(", "));
                    gameManager.sendMessageForAll("&aJogadores que passaram: &e" + finishedPlayers);
                    gameManager.sendMessageForAll("");

                    if (gameManager.playersFinished.size() > 0) {
                        gameManager.playSoundForAll(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);

                        List<String> pos = gameManager.configManager.getPosFinish();
                        World world = Bukkit.getWorld("world");

                        int[] p1 = WeWorld.parsePos(pos.get(0));
                        int[] p2 = WeWorld.parsePos(pos.get(1));

                        Location location1 = new Location(world, p1[0], p1[1], p1[2], 0.0f, 0.0f);
                        Location location2 = new Location(world, p2[0], p2[1], p2[2], 0.0f, 0.0f);
                        Fireworks.spawnFireworksAroundLine(world, location1, location2);
                    }
                }
                counterReset--;
                if (counterReset == 0) {
                    try {
                        gameManager.resetGame();
                    } catch (MaxChangedBlocksException e) {
                        throw new RuntimeException(e);
                    }
                    resetVars();
                }
        }
    }

    private void emitGreenLight() {
        gameManager.runState = RunState.RUN;

        gameManager.sendMessageForAll("&a&lCORRA!");
        gameManager.sendTitleForAll("&a&lLUZ VERDE!", "");
        gameManager.playSoundForAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 2F);
    }

    private void emitRedLight() {

        gameManager.sendMessageForAll("&c&lPARE!");
        gameManager.sendTitleForAll("&c&lLUZ VERMELHA!", "");
        gameManager.playSoundForAll(Sound.BLOCK_ANVIL_LAND, 1F, 1F);
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;

        return String.format("%d:%02d", minutes, secs);
    }

    public void resetVars() {
        counterStarting = 10;
        counterEnd = 180;
        counterRun = 4;
        counterReset = 15;
        delayReturn = gameManager.guardsEnabled ? 3 : 5;
    }
}
