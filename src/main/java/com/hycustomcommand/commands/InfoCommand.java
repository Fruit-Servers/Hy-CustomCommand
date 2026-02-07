package com.hycustomcommand.commands;

import com.hycustomcommand.config.CommandEntry;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import java.util.List;

public class InfoCommand extends CommandBase {

    private final List<String> lines;

    public InfoCommand(CommandEntry entry) {
        super(entry.getName(), entry.getDescription() != null ? entry.getDescription() : "");
        this.lines = entry.getLines();
        if (entry.getPermission() != null && !entry.getPermission().isEmpty()) {
            requirePermission(entry.getPermission());
        }
    }

    @Override
    protected void executeSync(CommandContext context) {
        if (lines == null || lines.isEmpty()) {
            return;
        }

        for (String line : lines) {
            context.sendMessage(colorize(line));
        }
    }

    private Message colorize(String text) {
        if (!text.contains("&")) {
            return Message.raw(text);
        }

        Message result = null;
        String[] parts = text.split("(?=&[0-9a-fk-or])");

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            String color = null;
            boolean bold = false;
            boolean italic = false;
            String content = part;

            if (part.startsWith("&") && part.length() >= 2) {
                char code = part.charAt(1);
                content = part.substring(2);
                color = getColor(code);
                bold = code == 'l';
                italic = code == 'o';
            }

            if (content.isEmpty()) {
                continue;
            }

            Message segment = Message.raw(content);
            if (color != null) {
                segment = segment.color(color);
            }
            if (bold) {
                segment = segment.bold(true);
            }
            if (italic) {
                segment = segment.italic(true);
            }

            if (result == null) {
                result = segment;
            } else {
                result = Message.join(result, segment);
            }
        }

        return result != null ? result : Message.raw(text);
    }

    private String getColor(char code) {
        return switch (code) {
            case '0' -> "#000000";
            case '1' -> "#0000AA";
            case '2' -> "#00AA00";
            case '3' -> "#00AAAA";
            case '4' -> "#AA0000";
            case '5' -> "#AA00AA";
            case '6' -> "#FFAA00";
            case '7' -> "#AAAAAA";
            case '8' -> "#555555";
            case '9' -> "#5555FF";
            case 'a' -> "#55FF55";
            case 'b' -> "#55FFFF";
            case 'c' -> "#FF5555";
            case 'd' -> "#FF55FF";
            case 'e' -> "#FFFF55";
            case 'f' -> "#FFFFFF";
            default -> null;
        };
    }
}
