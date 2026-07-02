package net.kyle.jukeboxplus.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Soft-dependency bridge to AudioPlayer (1.10.x – 1.13.x for MC 1.20.4–1.21.4).
 *
 * AudioPlayer does not have a public API package in these versions; we call the
 * internal classes (AudioManager, CustomSound, PlayerType, PlayerManager) directly.
 *
 * Not supported on 1.19.4 (AP 1.8.9 has a different internal structure) or 1.20.1
 * (AP 1.13.2 on MC 1.20.1 silently fails when called outside the vanilla jukebox
 * context). On those versions isAudioDisc() returns false and AP discs fall through
 * to vanilla syncWorldEvent.
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

    // Returns false on 1.19.4 and 1.20.1 — AP discs fall through to vanilla on those versions.
    public static boolean isAudioDisc(ItemStack stack) {
        //? if >=1.20.4 {
        /*return de.maxhenkel.audioplayer.CustomSound.of(stack) != null;
        *///?} else {
        return false;
        //?}
    }

    public static void play(World world, BlockPos pos, ItemStack disc) {
        //? if >=1.20.4 {
        /*if (!(world instanceof net.minecraft.server.world.ServerWorld sw)) return;
        de.maxhenkel.audioplayer.CustomSound sound = de.maxhenkel.audioplayer.CustomSound.of(disc);
        if (sound == null) return;
        net.minecraft.entity.player.PlayerEntity nearbyPlayer =
            sw.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 64.0, false);
        UUID channelId = de.maxhenkel.audioplayer.AudioManager.play(
            sw, pos, de.maxhenkel.audioplayer.PlayerType.MUSIC_DISC, sound, nearbyPlayer
        );
        if (channelId != null) channels.put(pos, channelId);
        else channels.remove(pos);
        *///?}
    }

    public static void stop(BlockPos pos) {
        //? if >=1.20.4 {
        /*UUID channelId = channels.remove(pos);
        if (channelId != null) {
            de.maxhenkel.audioplayer.PlayerManager.instance().stop(channelId);
        }
        *///?} else {
        channels.remove(pos);
        //?}
    }

    public static boolean isChannelStopped(BlockPos pos) {
        //? if >=1.20.4 {
        /*if (!SVC_LOADED) return false;
        UUID channelId = channels.get(pos);
        if (channelId == null) return true;
        return !de.maxhenkel.audioplayer.PlayerManager.instance().isPlaying(channelId);
        *///?} else {
        return true;
        //?}
    }
}
