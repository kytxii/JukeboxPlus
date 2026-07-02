package net.kyle.jukeboxplus.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Soft-dependency bridge to AudioPlayer (1.8.9 – 1.13.x for MC 1.19.4–1.21.4).
 *
 * AudioPlayer does not have a public API package in these versions; we call the
 * internal classes (AudioManager, CustomSound, PlayerType, PlayerManager) directly.
 * Those classes exist from AP 1.10.x onward (MC 1.20.1+). AP 1.8.9 (MC 1.19.4)
 * uses a different internal structure, so on that version we detect AP discs via
 * raw NBT and skip custom streaming (vanilla placeholder is played instead).
 *
 * AudioPlayer requires Simple Voice Chat for actual audio streaming. AudioManager.play()
 * returns null without SVC installed. When that happens we never store a channel, so
 * isChannelStopped() would return true immediately and trigger instant auto-advance.
 * We guard against that by checking SVC_LOADED: without SVC, isChannelStopped() returns
 * false so the disc stays on-screen until the user manually changes it.
 */
public class AudioPlayerCompat {

    public static final boolean LOADED =
        FabricLoader.getInstance().isModLoaded("audioplayer");

    public static final boolean SVC_LOADED =
        FabricLoader.getInstance().isModLoaded("voicechat");

    // BlockPos → UUID channel returned by AudioManager.play()
    private static final Map<BlockPos, UUID> channels = new ConcurrentHashMap<>();

    /**
     * True if this stack carries AudioPlayer custom audio.
     *
     * On 1.20.1+ we delegate to CustomSound.of() which handles NBT vs DataComponents
     * internally. On 1.19.4 (where CustomSound doesn't exist) we check the NBT key
     * directly — AudioPlayer always stores the audio UUID under "CustomSound".
     */
    public static boolean isAudioDisc(ItemStack stack) {
        //? if >=1.20 {
        /*return de.maxhenkel.audioplayer.CustomSound.of(stack) != null;
        *///?} else {
        var tag = stack.getNbt();
        return tag != null && tag.containsUuid("CustomSound");
        //?}
    }

    /**
     * Begin streaming the AP audio at pos. No-op on 1.19.4 (CustomSound/AudioManager
     * not available) and also a no-op if Simple Voice Chat is absent (AudioManager
     * returns null). Caller must still call DiscCompat.stop() before calling this to
     * clear any previous vanilla sound event.
     */
    public static void play(World world, BlockPos pos, ItemStack disc) {
        //? if >=1.20 {
        /*if (!(world instanceof net.minecraft.server.world.ServerWorld sw)) return;
        de.maxhenkel.audioplayer.CustomSound sound = de.maxhenkel.audioplayer.CustomSound.of(disc);
        if (sound == null) return;
        UUID channelId = de.maxhenkel.audioplayer.AudioManager.play(
            sw, pos, de.maxhenkel.audioplayer.PlayerType.MUSIC_DISC, sound, null
        );
        if (channelId != null) channels.put(pos, channelId);
        else channels.remove(pos);
        *///?}
    }

    /** Stop the AP channel at pos and remove it from the map. */
    public static void stop(BlockPos pos) {
        //? if >=1.20 {
        /*UUID channelId = channels.remove(pos);
        if (channelId != null) {
            de.maxhenkel.audioplayer.PlayerManager.instance().stop(channelId);
        }
        *///?} else {
        channels.remove(pos);
        //?}
    }

    /**
     * True once the channel has finished streaming. Used by the tick loop to trigger
     * auto-advance without needing to know the disc's duration in ticks.
     *
     * Returns false (disc "never ends") when SVC is absent — AudioManager.play() returns
     * null without SVC, meaning no channel is stored, so without this guard the method
     * would return true immediately and the jukebox would instant-advance every tick.
     * Returning false keeps the disc on-screen until the user manually changes it.
     *
     * Always returns true on 1.19.4 (no channel tracking on that AP version).
     */
    public static boolean isChannelStopped(BlockPos pos) {
        //? if >=1.20 {
        /*if (!SVC_LOADED) return false;
        UUID channelId = channels.get(pos);
        if (channelId == null) return true;
        return !de.maxhenkel.audioplayer.PlayerManager.instance().isPlaying(channelId);
        *///?} else {
        return true;
        //?}
    }
}
