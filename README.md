# JukeboxPlus

> An interactive jukebox block that extends the vanilla jukebox with a 9-disc inventory, playback controls, and loop modes.

![Modrinth](https://img.shields.io/modrinth/dt/jukebox+?label=downloads&color=00AF5C&logo=modrinth)
![CurseForge](https://img.shields.io/curseforge/dt/1558003?label=downloads&color=F16436&logo=curseforge&style=flat-square)
![Minecraft](https://img.shields.io/badge/minecraft-1.19.4%20%E2%80%93%201.21.4-blue)
![Fabric](https://img.shields.io/badge/loader-fabric-orange)
![License](https://img.shields.io/badge/license-MIT-blue)

---

<!-- screenshot of the block in-world -->

## Features

- **9-disc storage** — load an entire playlist into one block
- **Full playback controls** — play, stop, skip forward, skip back
- **Loop modes** — Loop One, Loop All, or no loop
- **Auto-advance** — moves to the next disc automatically when one ends, skips empty slots
- **Progress bar** — shows how far through the current disc you are
- **Disc display** — loaded discs appear on the face of the block
- **Mod-compatible** — works with any disc mod, no config needed

---

<!-- screenshot of the GUI open -->

## Crafting

<!-- recipe screenshot -->

Requires a vanilla jukebox plus additional materials — check the in-game recipe book or a mod like [EMI](https://modrinth.com/mod/emi).

---

## Supported versions

JukeboxPlus ships a separate build per Minecraft version. **Download the file matching your Minecraft version** — each one covers a range:

| Download | Works on | Java |
|---|---|---|
| `jukebox+ 1.1+1.19.4.jar` | 1.19.4 | 17 |
| `jukebox+ 1.1+1.20.1.jar` | 1.20 – 1.20.2 | 17 |
| `jukebox+ 1.1+1.20.4.jar` | 1.20.3 – 1.20.4 | 17 |
| `jukebox+ 1.1+1.20.6.jar` | 1.20.5 – 1.20.6 | 21 |
| `jukebox+ 1.1+1.21.1.jar` | 1.21 – 1.21.1 | 21 |
| `jukebox+ 1.1+1.21.4.jar` | 1.21.2 – 1.21.4 | 21 |

All builds are Fabric and require [Fabric API](https://modrinth.com/mod/fabric-api). 1.21.5+ isn't supported yet.

---

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/)
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) for your Minecraft version
3. Drop the **JukeboxPlus jar for your Minecraft version** (see the table above) and Fabric API into your `mods` folder
4. Launch

---

## AudioPlayer compatibility

JukeboxPlus works with [AudioPlayer](https://modrinth.com/mod/audioplayer) custom MP3/WAV discs out of the box — no config needed.

**Required mods for custom audio to play:**

| Mod | Why |
|---|---|
| [AudioPlayer](https://modrinth.com/mod/audioplayer) | Attaches your custom audio file to a disc item |
| [Simple Voice Chat](https://modrinth.com/mod/simple-voice-chat) | AudioPlayer uses it for the actual audio streaming |

Without Simple Voice Chat installed, AudioPlayer discs are still recognized and won't break anything — but no audio will play and the jukebox will wait on that disc until you skip manually.

AudioPlayer streaming requires **Minecraft 1.20.4+**.

---

## Issues & Suggestions

Found a bug or have an idea? [Open an issue on GitHub](https://github.com/kytxii/JukeboxPlus/issues).
