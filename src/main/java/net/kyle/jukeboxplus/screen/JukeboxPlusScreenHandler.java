package net.kyle.jukeboxplus.screen;

import net.kyle.jukeboxplus.block.entity.JukeboxPlusBlockEntity;
import net.kyle.jukeboxplus.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class JukeboxPlusScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private BlockPos blockPos;

    // Client-side
    public JukeboxPlusScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(9), new ArrayPropertyDelegate(5));
        this.blockPos = buf.readBlockPos();
    }

    // Server-side
    public JukeboxPlusScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.JUKEBOX_PLUS, syncId);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.addProperties(propertyDelegate);

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 55) {
                @Override public boolean canInsert(ItemStack stack) {
                    return stack.getItem() instanceof MusicDiscItem;
                }
            });
        }
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int col = 0; col < 9; col++)
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < 9) {
                if (!this.insertItem(originalStack, 9, 45, true)) return ItemStack.EMPTY;
            } else {
                if (!this.insertItem(originalStack, 0, 9, false)) return ItemStack.EMPTY;
            }
            if (originalStack.isEmpty()) slot.setStack(ItemStack.EMPTY);
            else slot.markDirty();
        }
        return newStack;
    }

    public JukeboxPlusBlockEntity.LoopMode getLoopMode() {
        return JukeboxPlusBlockEntity.LoopMode.values()[propertyDelegate.get(4)];
    }
    
    public BlockPos getBlockPos()        { return blockPos; }
    public int getCurrentSlot()          { return propertyDelegate.get(0); }
    public int getTicksPlayed()          { return propertyDelegate.get(1); }
    public int getDiscDurationTicks()    { return propertyDelegate.get(2); }
    public boolean isPlaying()           { return propertyDelegate.get(3) != 0; }
}