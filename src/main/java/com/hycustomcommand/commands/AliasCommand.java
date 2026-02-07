package com.hycustomcommand.commands;

import com.hycustomcommand.config.CommandEntry;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class AliasCommand extends AbstractPlayerCommand {

    private final String targetCommand;

    public AliasCommand(CommandEntry entry) {
        super(entry.getName(), entry.getDescription() != null ? entry.getDescription() : "");
        this.targetCommand = entry.getCommand();
        if (entry.getPermission() != null && !entry.getPermission().isEmpty()) {
            requirePermission(entry.getPermission());
        }
        setAllowsExtraArguments(true);
    }

    @Override
    protected void execute(CommandContext context, Store<EntityStore> store, Ref<EntityStore> playerRef, PlayerRef player, World world) {
        if (targetCommand == null || targetCommand.isEmpty()) {
            return;
        }

        String fullCommand = targetCommand;
        String extraArgs = context.getInputString().trim();
        int spaceIndex = extraArgs.indexOf(' ');
        if (spaceIndex >= 0) {
            String args = extraArgs.substring(spaceIndex + 1).trim();
            if (!args.isEmpty()) {
                fullCommand = targetCommand + " " + args;
            }
        }

        Player playerEntity = store.getComponent(playerRef, Player.getComponentType());
        if (playerEntity != null) {
            CommandManager.get().handleCommand(playerEntity, fullCommand);
        }
    }
}
