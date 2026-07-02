package net.kyle.jukeboxplus.block.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.kyle.jukeboxplus.registry.ModBlockEntities;
import net.kyle.jukeboxplus.screen.JukeboxPlusScreenHandler;
import net.kyle.jukeboxplus.util.AudioPlayerCompat;
import net.kyle.jukeboxplus.util.DiscCompat;
import net.kyle.jukeboxplus.util.DiscDurationUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

//? if >=1.20.5 {
/*public class JukeboxPlusBlockEntity extends BlockEntity implements Inventory, ExtendedScreenHandlerFactory<BlockPos> {
*///?} else {
public class JukeboxPlusBlockEntity extends BlockEntity implements Inventory, ExtendedScreenHandlerFactory {
//?}
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);

    private int currentSlot = 0;
    private int ticksPlayed = 0;
    private int discDurationTicks = 0;
    private boolean isPlaying = false;
    private LoopMode loopMode = LoopMode.OFF;

    public enum LoopMode { OFF, LOOP_ONE, LOOP_ALL }

    public final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override public int get(int index) {
            return switch (index) {
                case 0 -> currentSlot;
                case 1 -> ticksPlayed;
                case 2 -> discDurationTicks;
                case 3 -> isPlaying ? 1 : 0;
                case 4 -> loopMode.ordinal();
                default -> 0;
            };
        }
        @Override public void set(int index, int value) {
            switch (index) {
                case 0 -> currentSlot = value;
                case 1 -> ticksPlayed = value;
                case 2 -> discDurationTicks = value;
                case 3 -> isPlaying = value != 0;
                case 4 -> loopMode = LoopMode.values()[value];
            }
        }
        @Override public int size() { return 5; }
    };

    public JukeboxPlusBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.JUKEBOX_PLUS_BLOCK_ENTITY, pos, state);
    }

    //? if >=1.20.5 {
    /*@Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }
    *///?} else {
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }
    //?}

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.jukeboxplus.jukebox_plus_block");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new JukeboxPlusScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override public int size() { return 9; }
    @Override public boolean isEmpty() { return items.stream().allMatch(ItemStack::isEmpty); }
    @Override public ItemStack getStack(int slot) { return items.get(slot); }
    @Override public ItemStack removeStack(int slot, int amount) { ItemStack result = Inventories.splitStack(items, slot, amount); markDirty(); return result; }
    @Override public ItemStack removeStack(int slot) {  ItemStack result = Inventories.removeStack(items, slot); markDirty(); return result; }
    @Override public void setStack(int slot, ItemStack stack) { items.set(slot, stack); markDirty(); }
    @Override public boolean canPlayerUse(PlayerEntity player) { return true; }
    @Override public void clear() { items.replaceAll(ignored -> ItemStack.EMPTY); markDirty(); }

    @Override
    //? if >=1.20.5 {
    /*public void writeNbt(NbtCompound nbt, net.minecraft.registry.RegistryWrapper.WrapperLookup registries) {
        Inventories.writeNbt(nbt, items, registries);
    *///?} else {
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
    //?}
        nbt.putInt("currentSlot", currentSlot);
        nbt.putInt("ticksPlayed", ticksPlayed);
        nbt.putInt("discDurationTicks", discDurationTicks);
        nbt.putBoolean("isPlaying", isPlaying);
        nbt.putString("loopMode", loopMode.name());
        //? if >=1.20.5 {
        /*super.writeNbt(nbt, registries);
        *///?} else {
        super.writeNbt(nbt);
        //?}
    }

    @Override
    //? if >=1.20.5 {
    /*public void readNbt(NbtCompound nbt, net.minecraft.registry.RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
    *///?} else {
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    //?}
        items.replaceAll(ignored -> ItemStack.EMPTY); // clear stale items first
        //? if >=1.20.5 {
        /*Inventories.readNbt(nbt, items, registries);
        *///?} else {
        Inventories.readNbt(nbt, items);
        //?}
        currentSlot = nbt.getInt("currentSlot");
        ticksPlayed = nbt.getInt("ticksPlayed");
        discDurationTicks = nbt.getInt("discDurationTicks");
        isPlaying = nbt.getBoolean("isPlaying");
        try { loopMode = LoopMode.valueOf(nbt.getString("loopMode")); }
        catch (IllegalArgumentException e) { loopMode = LoopMode.OFF; }
        isPlaying = false;
        ticksPlayed = 0;
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    //? if >=1.20.5 {
    /*public NbtCompound toInitialChunkDataNbt(net.minecraft.registry.RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }
    *///?} else {
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
    //?}

    @Override
    public void markDirty() {
        super.markDirty();
        if (world instanceof ServerWorld serverWorld) {
            Packet<?> packet = toUpdatePacket();
            if (packet != null) {
                PlayerLookup.tracking(this).forEach(p -> p.networkHandler.sendPacket(packet));
            }
        }
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!isPlaying)     return;

        if (isPlaying && items.get(currentSlot).isEmpty()) {
            DiscCompat.stop(world, pos);
            isPlaying = false;
            ticksPlayed = 0;
            markDirty();
            return;
        }

        // AudioPlayer discs: use channel completion instead of tick counting.
        if (AudioPlayerCompat.LOADED && AudioPlayerCompat.isAudioDisc(items.get(currentSlot))) {
            if (!AudioPlayerCompat.isChannelStopped(pos)) return;
            // Channel finished — fall through to the end-of-disc switch below.
        } else {
            ticksPlayed++;
            if (discDurationTicks <= 0 || ticksPlayed < discDurationTicks) return;
        }

        DiscCompat.stop(world, pos); // stop current sound

        switch (loopMode) {
            case LOOP_ONE -> {
                ticksPlayed = 0;
                ItemStack disc = items.get(currentSlot);
                if (DiscCompat.isDisc(disc))
                    DiscCompat.play(world, pos, disc);
                else
                    isPlaying = false;
            }
            case LOOP_ALL -> {
                int next = findNextFilledSlot(currentSlot);
                if (next == -1) {
                    isPlaying = false;
                    ticksPlayed = 0;
                } else {
                    currentSlot = next;
                    ticksPlayed = 0;
                    ItemStack disc = items.get(currentSlot);
                    if (DiscCompat.isDisc(disc)) {
                        //? if >=1.21 {
                        /*discDurationTicks = DiscCompat.getDurationTicks(world, disc);
                        *///?} else {
                        discDurationTicks = DiscDurationUtil.getDurationTicks(((net.minecraft.item.MusicDiscItem) disc.getItem()).getSound(), world.getServer());
                        //?}
                        DiscCompat.play(world, pos, disc);
                    } else {
                        isPlaying = false;
                    }
                }
            }
            case OFF -> {
                isPlaying = false;
                ticksPlayed = 0;
            }
        }
        markDirty();
    }

    private int findNextFilledSlot(int from) {
        for (int i = 1; i <= 9; i++) {
            int candidate = (from + i) % 9;
            if (!items.get(candidate).isEmpty()) return candidate;
        }
        return -1;
    }

    public int getCurrentSlot() { return currentSlot; }
    public void setCurrentSlot(int slot) { currentSlot = slot; markDirty(); }
    public int getTicksPlayed() { return ticksPlayed; }
    public void setTicksPlayed(int ticks) { ticksPlayed = ticks; markDirty(); }
    public int getDiscDurationTicks() { return discDurationTicks; }
    public void setDiscDurationTicks(int ticks) { discDurationTicks = ticks; markDirty(); }
    public boolean isPlaying() { return isPlaying; }
    public void setPlaying(boolean playing) { isPlaying = playing; markDirty(); }
    public LoopMode getLoopMode() { return loopMode; }
    public void setLoopMode(LoopMode mode) { loopMode = mode; markDirty(); }

    // --- Server-side action handlers (called by ModPackets.dispatch) ---

    public void play(int duration) {
        setDiscDurationTicks(duration);
        setTicksPlayed(0);
        setPlaying(true);
        restartCurrentDisc();
    }

    public void stopPlayback() {
        setPlaying(false);
        setTicksPlayed(0);
        stopSound();
    }

    public void next(int duration) { advance(1, duration); }
    public void prev(int duration) { advance(8, duration); }

    private void advance(int delta, int duration) {
        setCurrentSlot((currentSlot + delta) % 9);
        setDiscDurationTicks(duration);
        setTicksPlayed(0);
        setPlaying(true);
        restartCurrentDisc();
    }

    public void toggleLoop() {
        setLoopMode(switch (loopMode) {
            case OFF      -> LoopMode.LOOP_ONE;
            case LOOP_ONE -> LoopMode.LOOP_ALL;
            case LOOP_ALL -> LoopMode.OFF;
        });
    }

    private void stopSound() {
        if (world != null) DiscCompat.stop(world, pos);
    }

    private void restartCurrentDisc() {
        if (world == null) return;
        DiscCompat.stop(world, pos);
        ItemStack disc = getStack(currentSlot);
        if (DiscCompat.isDisc(disc))
            DiscCompat.play(world, pos, disc);
    }
}