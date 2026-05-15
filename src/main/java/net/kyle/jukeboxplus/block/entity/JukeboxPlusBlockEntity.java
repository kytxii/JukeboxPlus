package net.kyle.jukeboxplus.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.kyle.jukeboxplus.screen.JukeboxPlusScreenHandler;
import net.minecraft.text.Text;

public class JukeboxPlusBlockEntity extends BlockEntity implements Inventory, NamedScreenHandlerFactory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);

    private int currentSlot = 0;
    private int ticksPlayed = 0;
    private int discDurationTicks = 0;
    private boolean isPlaying = false;
    private LoopMode loopMode = LoopMode.OFF;

    public enum LoopMode { 
        OFF, 
        LOOP_ONE, 
        LOOP_ALL 
    }

    public JukeboxPlusBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.JUKEBOX_PLUS_BLOCK_ENTITY, pos, state);
    }

    @Override
    public int size() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(items, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(items, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        items.replaceAll(ignored -> ItemStack.EMPTY);
        markDirty();
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.jukeboxplus.jukebox_plus_block");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new JukeboxPlusScreenHandler(syncId, playerInventory, this);
    }

    // NBT
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

        try {
            loopMode = LoopMode.valueOf(nbt.getString("loopMode"));
        } catch (IllegalArgumentException e) {
            loopMode = LoopMode.OFF;
        }
    }

    // Getters/setters
    public int getCurrentSlot() { return currentSlot; }
    public void setCurrentSlot(int slot) {currentSlot = slot; markDirty(); }

    public int getTicksPlayed() { return ticksPlayed; }
    public void setTicksPlayed(int ticks) { ticksPlayed = ticks; markDirty(); }

    public int getDiscDurationTicks() { return discDurationTicks; }
    public void setDiscDurationTicks(int ticks) { discDurationTicks = ticks; markDirty(); }

    public boolean isPlaying() { return isPlaying; }
    public void setPlaying(boolean playing) { isPlaying = playing; markDirty(); }

    public LoopMode getLoopMode() { return loopMode; }
    public void setLoopMode(LoopMode mode) { loopMode = mode; markDirty(); }
}