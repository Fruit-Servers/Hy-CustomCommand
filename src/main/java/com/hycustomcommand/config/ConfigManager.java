package com.hycustomcommand.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import com.hypixel.hytale.logger.HytaleLogger;
import java.util.logging.Level;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path configFile;
    private final HytaleLogger logger;
    private CustomCommandConfig config;

    public ConfigManager(Path dataDirectory, HytaleLogger logger) {
        this.configFile = dataDirectory.resolve("commands.json");
        this.logger = logger;
    }

    public void load() {
        if (!Files.exists(configFile)) {
            createDefault();
        }

        try (Reader reader = Files.newBufferedReader(configFile)) {
            config = GSON.fromJson(reader, CustomCommandConfig.class);
            if (config == null || config.getCommands() == null) {
                logger.at(Level.WARNING).log("Config was empty or invalid, creating default");
                createDefault();
                try (Reader retry = Files.newBufferedReader(configFile)) {
                    config = GSON.fromJson(retry, CustomCommandConfig.class);
                }
            }
        } catch (IOException e) {
            logger.at(Level.SEVERE).log("Failed to load commands.json: " + e.getMessage());
            config = new CustomCommandConfig();
            config.setCommands(List.of());
        }
    }

    private void createDefault() {
        try {
            Files.createDirectories(configFile.getParent());

            CommandEntry infoCommand = new CommandEntry();
            infoCommand.setName("serverinfo");
            infoCommand.setType("info");
            infoCommand.setDescription("Display server information");
            infoCommand.setPermission("customcommand.serverinfo");
            infoCommand.setLines(List.of(
                "&6=== Server Info ===",
                "&fWelcome to our Hytale server!",
                "&fWebsite: &bhttps://example.com",
                "&fDiscord: &bhttps://discord.gg/example",
                "&6==================="
            ));

            CommandEntry aliasCommand = new CommandEntry();
            aliasCommand.setName("day");
            aliasCommand.setType("alias");
            aliasCommand.setDescription("Set the time to day");
            aliasCommand.setPermission("customcommand.day");
            aliasCommand.setCommand("time day");

            CustomCommandConfig defaultConfig = new CustomCommandConfig();
            defaultConfig.setCommands(List.of(infoCommand, aliasCommand));

            try (Writer writer = Files.newBufferedWriter(configFile)) {
                GSON.toJson(defaultConfig, writer);
            }

            logger.at(Level.INFO).log("Created default commands.json");
        } catch (IOException e) {
            logger.at(Level.SEVERE).log("Failed to create default commands.json: " + e.getMessage());
        }
    }

    public CustomCommandConfig getConfig() {
        return config;
    }
}
