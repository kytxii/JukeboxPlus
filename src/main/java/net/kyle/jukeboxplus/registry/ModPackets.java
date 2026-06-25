package net.kyle.jukeboxplus.registry;

import net.kyle.jukeboxplus.JukeboxPlus;
import net.kyle.jukeboxplus.block.entity.JukeboxPlusBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
//? if >=1.20.5 {
/*import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
*///?} else {
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
//?}

/**
 * Networking hub. All the version-specific wiring lives here so the rest of the
 * mod just calls registerReceivers() (server) and ModPackets.send*(...) (client).
 *
 * Wire format is uniform across actions: a BlockPos + an int duration. STOP and
 * LOOP ignore the duration, but sending it keeps one packet shape on both versions.
 */
public class ModPackets {
    public static final int PLAY = 0, STOP = 1, NEXT = 2, PREV = 3, LOOP = 4;

    //? if >=1.20.5 {
    /*// 1.20.5+: a typed payload record carrying (action, pos, duration).
    public record JukeboxActionPayload(int action, BlockPos pos, int duration) implements CustomPayload {
        public static final CustomPayload.Id<JukeboxActionPayload> ID =
            new CustomPayload.Id<>(JukeboxPlus.id("jukebox_action"));
        public static final PacketCodec<RegistryByteBuf, JukeboxActionPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, JukeboxActionPayload::action,
            BlockPos.PACKET_CODEC, JukeboxActionPayload::pos,
            PacketCodecs.VAR_INT, JukeboxActionPayload::duration,
            JukeboxActionPayload::new);
        @Override public CustomPayload.Id<JukeboxActionPayload> getId() { return ID; }
    }
    *///?} else {
    // 1.20.1: one Identifier per action.
    public static final Identifier PLAY_ID = JukeboxPlus.id("play");
    public static final Identifier STOP_ID = JukeboxPlus.id("stop");
    public static final Identifier NEXT_ID = JukeboxPlus.id("next");
    public static final Identifier PREV_ID = JukeboxPlus.id("prev");
    public static final Identifier LOOP_ID = JukeboxPlus.id("loop_toggle");

    public static Identifier idFor(int action) {
        return switch (action) {
            case PLAY -> PLAY_ID;
            case STOP -> STOP_ID;
            case NEXT -> NEXT_ID;
            case PREV -> PREV_ID;
            default   -> LOOP_ID;
        };
    }
    //?}

    /** Server-side: register the C2S receiver(s). Called from JukeboxPlus.onInitialize. */
    public static void registerReceivers() {
        //? if >=1.20.5 {
        /*PayloadTypeRegistry.playC2S().register(JukeboxActionPayload.ID, JukeboxActionPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(JukeboxActionPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            int action = payload.action();
            BlockPos pos = payload.pos();
            int duration = payload.duration();
            context.server().execute(() -> dispatch(player, action, pos, duration));
        });
        *///?} else {
        registerReceiver(PLAY_ID, PLAY);
        registerReceiver(STOP_ID, STOP);
        registerReceiver(NEXT_ID, NEXT);
        registerReceiver(PREV_ID, PREV);
        registerReceiver(LOOP_ID, LOOP);
        //?}
    }

    //? if <1.20.5 {
    private static void registerReceiver(Identifier id, int action) {
        ServerPlayNetworking.registerGlobalReceiver(id, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            int duration = buf.readInt();
            server.execute(() -> dispatch(player, action, pos, duration));
        });
    }
    //?}

    /** Shared: apply an action to the jukebox at pos. Runs on the server thread. */
    private static void dispatch(ServerPlayerEntity player, int action, BlockPos pos, int duration) {
        if (player.getWorld().getBlockEntity(pos) instanceof JukeboxPlusBlockEntity j) {
            switch (action) {
                case PLAY -> j.play(duration);
                case STOP -> j.stopPlayback();
                case NEXT -> j.next(duration);
                case PREV -> j.prev(duration);
                case LOOP -> j.toggleLoop();
            }
        }
    }
}
