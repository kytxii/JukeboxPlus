package net.kyle.jukeboxplus.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.kyle.jukeboxplus.screen.JukeboxPlusScreenHandler;
import net.kyle.jukeboxplus.util.DiscDurationUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JukeboxPlusBlockEntity extends BlockEntity implements Inventory, ExtendedScreenHandlerFactory {
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

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

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
    @Override public ItemStack removeStack(int slot, int amount) { return Inventories.splitStack(items, slot, amount); }
    @Override public ItemStack removeStack(int slot) { return Inventories.removeStack(items, slot); }
    @Override public void setStack(int slot, ItemStack stack) { items.set(slot, stack); markDirty(); }
    @Override public boolean canPlayerUse(PlayerEntity player) { return true; }
    @Override public void clear() { items.replaceAll(ignored -> ItemStack.EMPTY); markDirty(); }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        nbt.putInt("currentSlot", currentSlot);
        nbt.putInt("ticksPlayed", ticksPlayed);
        nbt.putInt("discDurationTicks", discDurationTicks);
        nbt.putBoolean("isPlaying", isPlaying);
        nbt.putString("loopMode", loopMode.name());
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
        currentSlot = nbt.getInt("currentSlot");
        ticksPlayed = nbt.getInt("ticksPlayed");
        discDurationTicks = nbt.getInt("discDurationTicks");
        isPlaying = nbt.getBoolean("isPlaying");
        try { loopMode = LoopMode.valueOf(nbt.getString("loopMode")); }
        catch (IllegalArgumentException e) { loopMode = LoopMode.OFF; }
        isPlaying = false;
        ticksPlayed = 0;
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!isPlaying) return;

        ticksPlayed++;
        if (discDurationTicks <= 0 || ticksPlayed < discDurationTicks) {
            markDirty();
            return;
        }

        world.syncWorldEvent(1011, pos, 0); // stop current sound

        switch (loopMode) {
            case LOOP_ONE -> {
                ticksPlayed = 0;
                ItemStack disc = items.get(currentSlot);
                if (disc.getItem() instanceof MusicDiscItem)
                    world.syncWorldEvent(1010, pos, Item.getRawId(disc.getItem()));
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
                    if (disc.getItem() instanceof MusicDiscItem musicDisc) {
                        discDurationTicks = DiscDurationUtil.getDurationTicks(musicDisc.getSound(), world.getServer());
                        world.syncWorldEvent(1010, pos, Item.getRawId(disc.getItem()));
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
}