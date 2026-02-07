# Hy-CustomCommand

A Hytale server plugin that allows the creation of custom commands via a JSON config file.

## Features

- **Info Commands** — Define a command that sends pre-written lines of text to the player
- **Alias Commands** — Define a command that triggers another existing command (with argument passthrough)
- Auto-generates a default `commands.json` config on first run
- Per-command permission support
- Color code formatting for info command messages

## Installation

1. Build with `./gradlew build`
2. Copy `build/libs/Hy-CustomCommand-1.0.0.jar` into your server's `plugins/` folder
3. Start the server — a default `commands.json` will be generated in the plugin's data directory

## Config

The config file `commands.json` is located in the plugin's data directory and contains an array of command definitions.

### Info Command Example

Sends formatted text lines to the player when the command is run.

```json
{
  "name": "serverinfo",
  "type": "info",
  "description": "Display server information",
  "permission": "customcommand.serverinfo",
  "lines": [
    "&6=== Server Info ===",
    "&fWelcome to our Hytale server!",
    "&fWebsite: &bhttps://example.com",
    "&fDiscord: &bhttps://discord.gg/example",
    "&6==================="
  ]
}
```

### Alias Command Example

Runs another command when executed. Extra arguments are passed through.

```json
{
  "name": "day",
  "type": "alias",
  "description": "Set the time to day",
  "permission": "customcommand.day",
  "command": "time day"
}
```

## Color Codes

Info command lines support `&` color codes. The following codes are available:

| Code | Color | Hex | Unicode |
|------|-------|-----|---------|
| `&0` | Black | `#000000` | `\u00260` |
| `&1` | Dark Blue | `#0000AA` | `\u00261` |
| `&2` | Dark Green | `#00AA00` | `\u00262` |
| `&3` | Dark Aqua | `#00AAAA` | `\u00263` |
| `&4` | Dark Red | `#AA0000` | `\u00264` |
| `&5` | Dark Purple | `#AA00AA` | `\u00265` |
| `&6` | Gold | `#FFAA00` | `\u00266` |
| `&7` | Gray | `#AAAAAA` | `\u00267` |
| `&8` | Dark Gray | `#555555` | `\u00268` |
| `&9` | Blue | `#5555FF` | `\u00269` |
| `&a` | Green | `#55FF55` | `\u0026a` |
| `&b` | Aqua | `#55FFFF` | `\u0026b` |
| `&c` | Red | `#FF5555` | `\u0026c` |
| `&d` | Light Purple | `#FF55FF` | `\u0026d` |
| `&e` | Yellow | `#FFFF55` | `\u0026e` |
| `&f` | White | `#FFFFFF` | `\u0026f` |

### Formatting Codes

| Code | Effect | Unicode |
|------|--------|---------|
| `&l` | **Bold** | `\u0026l` |
| `&o` | *Italic* | `\u0026o` |
| `&r` | Reset | `\u0026r` |

## Permissions

Each command can have a custom permission node set via the `permission` field in the config. If omitted, the command has no permission requirement.
