package net.kyle.jukeboxplus.util;

import net.kyle.jukeboxplus.JukeboxPlus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DiscDurationUtil {
    private static final Map<Identifier, Integer> cache = new HashMap<>();

    public static int getDurationTicks(SoundEvent soundEvent, ResourceManager resources) {
        Identifier eventId = net.minecraft.registry.Registries.SOUND_EVENT.getId(soundEvent);
        if (cache.containsKey(eventId)) return cache.get(eventId);

        // resolve event ID → actual file path via sounds.json
        WeightedSoundSet soundSet = MinecraftClient.getInstance().getSoundManager().get(eventId);
        if (soundSet == null) return 0;

        Sound sound = soundSet.getSound(net.minecraft.util.math.random.Random.create());
        if (sound == null) return 0;

        Identifier fileId = JukeboxPlus.id(
            sound.getIdentifier().getNamespace(),
            "sounds/" + sound.getIdentifier().getPath() + ".ogg"
        );

        try (InputStream stream = resources.getResource(fileId).get().getInputStream()) {
            byte[] data = stream.readAllBytes();
            int sampleRate = readSampleRate(data);
            long granule = readLastGranule(data);
            if (sampleRate <= 0 || granule <= 0) return 0;
            int ticks = (int)(granule * 20L / sampleRate);
            cache.put(eventId, ticks);
            return ticks;
        } catch (Exception e) {
            return 0;
        }
    }

    private static int readSampleRate(byte[] data) {
        for (int i = 0; i < data.length - 30; i++)
            if (data[i] == 1 && matchVorbis(data, i + 1))
                return readInt32LE(data, i + 12);
        return 0;
    }

    private static long readLastGranule(byte[] data) {
        long last = 0;
        for (int i = 0; i < data.length - 27; i++)
            if (data[i]=='O'&&data[i+1]=='g'&&data[i+2]=='g'&&data[i+3]=='S') {
                long g = readInt64LE(data, i + 6);
                if (g > 0) last = g;
            }
        return last;
    }

    private static boolean matchVorbis(byte[] d, int o) {
        return d[o]=='v'&&d[o+1]=='o'&&d[o+2]=='r'&&d[o+3]=='b'&&d[o+4]=='i'&&d[o+5]=='s';
    }

    private static int readInt32LE(byte[] d, int o) {
        return (d[o]&0xFF)|((d[o+1]&0xFF)<<8)|((d[o+2]&0xFF)<<16)|((d[o+3]&0xFF)<<24);
    }

    private static long readInt64LE(byte[] d, int o) {
        long r = 0;
        for (int i = 0; i < 8; i++) r |= ((long)(d[o+i]&0xFF))<<(8*i);
        return r;
    }

    public static int getDurationTicks(SoundEvent soundEvent, MinecraftServer server) {
        Identifier eventId = net.minecraft.registry.Registries.SOUND_EVENT.getId(soundEvent);
        if (cache.containsKey(eventId)) return cache.get(eventId);

        Identifier fileId = JukeboxPlus.id(
            eventId.getNamespace(),
            "sounds/" + eventId.getPath().replace('.', '/') + ".ogg"
        );

        try (InputStream stream = server.getResourceManager().getResource(fileId).get().getInputStream()) {
            byte[] data = stream.readAllBytes();
            int sampleRate = readSampleRate(data);
            long granule = readLastGranule(data);
            if (sampleRate <= 0 || granule <= 0) return 0;
            int ticks = (int)(granule * 20L / sampleRate);
            cache.put(eventId, ticks);
            return ticks;
        } catch (Exception e) {
            return 0;
        }
    }
}