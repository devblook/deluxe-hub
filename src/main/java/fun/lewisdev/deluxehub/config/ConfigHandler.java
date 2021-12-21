package fun.lewisdev.deluxehub.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigHandler {
    private final JavaPlugin plugin;
    private final String name;
    private final File file;
    private FileConfiguration configFile;

    public ConfigHandler(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name + ".yml";
        this.file = new File(plugin.getDataFolder(), this.name);
        this.configFile = new YamlConfiguration();
    }

    public void saveDefaultConfig() {
        if (!file.exists()) {
            plugin.saveResource(name, false);
            plugin.getLogger().log(Level.INFO, "Resource file {0} written sucessfully!", name);
        }

        try {
            configFile.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
            plugin.getLogger().severe("There was an error loading " + name);
            plugin.getLogger().severe("Please check for any obvious configuration mistakes");
            plugin.getLogger().severe("such as using tabs for spaces or forgetting to end quotes");
            plugin.getLogger().severe("before reporting to the developer. The plugin will now disable.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

    }

    public void save() {
        if (configFile == null || file == null) return;

        try {
            configFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        configFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return configFile;
    }
}
