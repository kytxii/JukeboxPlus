package net.kyle.jukeboxplus.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Music-disc / playback compatibility shim.
 *
 * 1.20.1: discs are MusicDiscItem; playback rides on syncWorldEvent 1010/1011.
 * 1.21:   discs are any item carrying a JukeboxPlayable component; the song comes
 *         from the JukeboxSong registry, and we play/stop the sound directly.
 *
 * Every version-specific bit of disc handling lives here so the rest of the mod
 * is version-agnostic.
 */
public class DiscCompat {

    /** Is this stack a playable music disc? */
    public static boolean isDisc(ItemStack stack) {
        if (AudioPlayerCompat.LOADED && AudioPlayerCompat.isAudioDisc(stack)) return true;
        //? if >=1.21 {
        /*return stack.contains(net.minecraft.component.DataComponentTypes.JUKEBOX_PLAYABLE);
        *///?} else {
        return stack.getItem() instanceof net.minecraft.item.MusicDiscItem;
        //?}
    }

    /**
     * Disc length in ticks. On 1.21 this is read straight from the JukeboxSong
     * (no OGG parsing); works on both client and server via the world's registries.
     * On 1.20.1 duration is computed at the call site (it needs the right
     * ResourceManager), so this returns 0 there and is simply not called.
     */
    public static int getDurationTicks(World world, ItemStack stack) {
        // AudioPlayer discs have unknown duration — return 0 so the tick loop never
        // triggers end-of-disc by tick count; isChannelStopped() handles it instead.
        if (AudioPlayerCompat.LOADED && AudioPlayerCompat.isAudioDisc(stack)) return 0;
        //? if >=1.21 {
        /*return net.minecraft.block.jukebox.JukeboxSong.getSongEntryFromStack(world.getRegistryManager(), stack)
            .map(entry -> entry.value().getLengthInTicks())
            .orElse(0);
        *///?} else {
        return 0;
        //?}
    }

    /** Start the disc's sound at pos. */
    public static void play(World world, BlockPos pos, ItemStack disc) {
        if (AudioPlayerCompat.LOADED && AudioPlayerCompat.isAudioDisc(disc)) {
            AudioPlayerCompat.play(world, pos, disc);
            return;
        }
        //? if >=1.21 {
        /*net.minecraft.block.jukebox.JukeboxSong.getSongEntryFromStack(world.getRegistryManager(), disc)
            .ifPresent(entry -> world.playSound(null, pos, entry.value().soundEvent().value(),
                net.minecraft.sound.SoundCategory.RECORDS, 4.0f, 1.0f));
        *///?} else {
        world.syncWorldEvent(1010, pos, net.minecraft.item.Item.getRawId(disc.getItem()));
        //?}
    }

    /** Stop any record currently playing at pos. */
    public static void stop(World world, BlockPos pos) {
        // Always attempt to stop an AP channel — no-op if none is playing here.
        if (AudioPlayerCompat.LOADED) AudioPlayerCompat.stop(pos);
        //? if >=1.21 {
        /*if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
            net.minecraft.network.packet.s2c.play.StopSoundS2CPacket packet =
                new net.minecraft.network.packet.s2c.play.StopSoundS2CPacket(
                    (net.minecraft.util.Identifier) null, net.minecraft.sound.SoundCategory.RECORDS);
            net.fabricmc.fabric.api.networking.v1.PlayerLookup.around(serverWorld, pos, 64)
                .forEach(p -> p.networkHandler.sendPacket(packet));
        }
        *///?} else {
        world.syncWorldEvent(1011, pos, 0);
        //?}
    }
}
