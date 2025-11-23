package org.kauazs.batatinhafrita.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.kauazs.batatinhafrita.BatatinhaFrita;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final BatatinhaFrita plugin;

    private final File configFile;
    private final YamlConfiguration configYaml;

    public ConfigManager(BatatinhaFrita plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        this.configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.configYaml = YamlConfiguration.loadConfiguration(configFile);
    }

    public List<String> getPosWall() {
        ConfigurationSection section = configYaml.getConfigurationSection("positions.walls");
        if (section == null) {
            return null;
        }

        List<String> pos = new ArrayList<>();

        String pos1 = section.getString("pos1");
        String pos2 = section.getString("pos2");

        if (pos1 != null) pos.add(pos1);
        if (pos2 != null) pos.add(pos2);

        return pos;
    }

    public List<String> getPosFinish() {
        ConfigurationSection section = configYaml.getConfigurationSection("positions.finish");
        if (section == null) {
            return null;
        }

        List<String> pos = new ArrayList<>();

        String pos1 = section.getString("pos1");
        String pos2 = section.getString("pos2");

        if (pos1 != null) pos.add(pos1);
        if (pos2 != null) pos.add(pos2);

        return pos;
    }



}
