package org.kauazs.batatinhafrita;

import com.sk89q.worldedit.MaxChangedBlocksException;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.kauazs.batatinhafrita.commands.GuardsCommand;
import org.kauazs.batatinhafrita.listeners.Controller;
import org.kauazs.batatinhafrita.listeners.PlayerInteract;
import org.kauazs.batatinhafrita.managers.GameManager;
import org.kauazs.batatinhafrita.utils.WeWorld;

import java.util.List;

public final class BatatinhaFrita extends JavaPlugin {

    private GameManager gameManager;
    @Override
    public void onEnable() {
        this.gameManager = new GameManager(this);

        getServer().getPluginManager().registerEvents(new Controller(gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(gameManager), this);
        getCommand("guardas").setExecutor(new GuardsCommand(gameManager));

        List<String> posWall = gameManager.configManager.getPosWall();
        World world = getServer().getWorld("world");

        try {
            WeWorld.restoreWall(world, posWall.get(0), posWall.get(1), "barrier");
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {}
}
