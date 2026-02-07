package com.hycustomcommand;

import com.hycustomcommand.commands.AliasCommand;
import com.hycustomcommand.commands.InfoCommand;
import com.hycustomcommand.config.CommandEntry;
import com.hycustomcommand.config.ConfigManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public class HyCustomCommand extends JavaPlugin {

    private static HyCustomCommand instance;
    private ConfigManager configManager;

    public HyCustomCommand(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
        getLogger().at(Level.INFO).log("Hy-CustomCommand loaded");
    }

    @Override
    protected void setup() {
        configManager = new ConfigManager(getDataDirectory(), getLogger());
        configManager.load();

        int registered = 0;
        var registry = getCommandRegistry();

        for (CommandEntry entry : configManager.getConfig().getCommands()) {
            if (entry.getName() == null || entry.getName().isEmpty()) {
                getLogger().at(Level.WARNING).log("Skipping command with no name");
                continue;
            }

            if (entry.getType() == null || entry.getType().isEmpty()) {
                getLogger().at(Level.WARNING).log("Skipping command '" + entry.getName() + "' with no type");
                continue;
            }

            switch (entry.getType().toLowerCase()) {
                case "info" -> {
                    registry.registerCommand(new InfoCommand(entry));
                    registered++;
                    getLogger().at(Level.INFO).log("Registered info command: /" + entry.getName());
                }
                case "alias" -> {
                    if (entry.getCommand() == null || entry.getCommand().isEmpty()) {
                        getLogger().at(Level.WARNING).log("Alias command '" + entry.getName() + "' has no target command, skipping");
                        continue;
                    }
                    registry.registerCommand(new AliasCommand(entry));
                    registered++;
                    getLogger().at(Level.INFO).log("Registered alias command: /" + entry.getName() + " -> /" + entry.getCommand());
                }
                default -> getLogger().at(Level.WARNING).log("Unknown command type '" + entry.getType() + "' for command '" + entry.getName() + "'");
            }
        }

        getLogger().at(Level.INFO).log("Hy-CustomCommand enabled - " + registered + " custom command(s) registered");
    }

    @Override
    protected void shutdown() {
        getLogger().at(Level.INFO).log("Hy-CustomCommand disabled");
    }

    public static HyCustomCommand getInstance() {
        return instance;
    }
}
