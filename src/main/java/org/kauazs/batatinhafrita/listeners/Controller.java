package org.kauazs.batatinhafrita.listeners;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kauazs.batatinhafrita.managers.GameManager;
import org.kauazs.batatinhafrita.managers.GameState;
import org.kauazs.batatinhafrita.managers.RunState;
import org.kauazs.batatinhafrita.utils.Colorize;
import org.kauazs.batatinhafrita.utils.WeWorld;

import java.util.List;

public class Controller implements Listener {

    private GameManager gameManager;
    public Controller(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (gameManager.playersPlaying.contains(player)) {
            gameManager.removePlayer(player);
        }
        if (gameManager.guards.contains(player)) {

        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("");

        if (gameManager.gameState.equals(GameState.PLAYING) && !player.isOp()) {
            player.kickPlayer(ChatColor.RED + "Evento em andamento");
            return;
        }
        player.getInventory().clear();
        if (player.isOp()) {
            ItemStack itemStack = new ItemStack(Material.CLOCK, 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setItemName(ChatColor.RED + "" + ChatColor.BOLD + "MENU");
            itemStack.setItemMeta(itemMeta);

            player.getInventory().addItem(itemStack);
        }

        if (gameManager.gameState.equals(GameState.WAITING) || gameManager.gameState.equals(GameState.STARTING)) {
            gameManager.addPlayer(player);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();

        List<String> posFinish = gameManager.configManager.getPosFinish();
        CuboidRegion region = WeWorld.createRegion(player.getWorld(), posFinish.get(0), posFinish.get(1));

        BlockVector3 playerPos = BlockVector3.at(
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        );

        if (gameManager.gameState.equals(GameState.PLAYING)) {
            if (gameManager.runState.equals(RunState.STOP)) {
                if (gameManager.playerHasPlaying(player) || (gameManager.playerIsFinished(player) && !region.contains(playerPos))) {
                    if (!gameManager.guardsEnabled) {
                        gameManager.removePlayer(player);
                        player.sendTitle(ChatColor.RED + "ELIMINADO!", "");
                        player.setGameMode(GameMode.SPECTATOR);
                        player.getWorld().strikeLightningEffect(player.getLocation());
                        gameManager.sendMessageForAll("&cJogador " + player.getDisplayName() + " &celiminado!");
                    } else {
                        gameManager.addPlayerMoved(player);
                        player.sendMessage(ChatColor.RED + "Você se mexeu!");
                    }

                    if (gameManager.playerIsFinished(player)) {
                        gameManager.removePlayerFinished(player);
                    }
                }
            }
            if (region.contains(playerPos) && gameManager.playersPlaying.contains(player) &&!gameManager.playerIsFinished(player)) {
                player.sendTitle("", "");
                player.sendMessage(ChatColor.GREEN + "Você passou!");
                gameManager.addPlayerFinished(player);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent playerDeathEvent) {
        Player player = playerDeathEvent.getEntity();
        player.spigot().respawn();

    }

    @EventHandler
    public void onDamageEvent(EntityDamageEvent event) {
        if (!event.getEntityType().equals(EntityType.PLAYER)) return;
        Player player = (Player) event.getEntity();
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
            Entity attacker = entityEvent.getDamager();
            if (attacker instanceof Player) {
                Player playerAttacker = (Player) attacker;
                if (gameManager.guards.contains(playerAttacker)) {
                    event.setCancelled(true);
                } else {
                    player.setHealth(20.0);
                }
            }
        }
    }


    @EventHandler
    public void onBreakBlocks(BlockBreakEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player hit)) return;
        if (e.getDamager() instanceof Arrow arrow) {

            if (!(arrow.getShooter() instanceof Player shooter)) return;
            ItemStack bow = shooter.getInventory().getItemInMainHand();
            if (!gameManager.guards.contains(shooter)) return;
            if (!gameManager.playersMoved.contains(hit))  {
                shooter.sendMessage(ChatColor.RED + "Esse jogador não se mexeu.");
                e.setCancelled(true);
                return;
            }

            if (bow == null || !bow.hasItemMeta()) return;
            if (!bow.getItemMeta().hasDisplayName()) return;

            if (ChatColor.stripColor(bow.getItemMeta().getDisplayName())
                    .equalsIgnoreCase("arco op")) {

                hit.sendTitle(ChatColor.RED + "ELIMINADO!", "");
                hit.setGlowing(false);
                hit.setGameMode(GameMode.SPECTATOR);
                hit.getWorld().strikeLightningEffect(hit.getLocation());

                gameManager.removePlayer(hit);
                gameManager.sendMessageForAll("&cJogador " + hit.getDisplayName() + " &celiminado!");
            }
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if (gameManager.gameState.equals(GameState.PLAYING)) {
            event.setCancelled(true);
        }
    }
}
