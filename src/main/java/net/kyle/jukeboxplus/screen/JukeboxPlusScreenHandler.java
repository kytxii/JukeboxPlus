package net.kyle.jukeboxplus.screen;

  import net.minecraft.entity.player.PlayerEntity;
  import net.minecraft.entity.player.PlayerInventory;
  import net.minecraft.inventory.Inventory;
  import net.minecraft.inventory.SimpleInventory;
  import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.screen.ScreenHandler;
  import net.minecraft.screen.slot.Slot;

  public class JukeboxPlusScreenHandler extends ScreenHandler {
      private final Inventory inventory;

      // Client-side — called when the client receives the open-screen packet
      public JukeboxPlusScreenHandler(int syncId, PlayerInventory playerInventory) {
          this(syncId, playerInventory, new SimpleInventory(9));
      }

      // Server-side — called with the real block entity inventory
      public JukeboxPlusScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
          super(ModScreenHandlers.JUKEBOX_PLUS, syncId);
          this.inventory = inventory;

          // 9 disc slots
          for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 55) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.getItem() instanceof MusicDiscItem;
                }
            });
  }

          // Player inventory (3 rows × 9 columns)
          for (int row = 0; row < 3; row++) {
              for (int col = 0; col < 9; col++) {
                  this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
              }
          }

          // Hotbar
          for (int col = 0; col < 9; col++) {
              this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
          }
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
                  // Disc slot → player inventory
                  if (!this.insertItem(originalStack, 9, 45, true)) return ItemStack.EMPTY;
              } else {
                  // Player inventory → disc slots
                  if (!this.insertItem(originalStack, 0, 9, false)) return ItemStack.EMPTY;
              }
              if (originalStack.isEmpty()) {
                  slot.setStack(ItemStack.EMPTY);
              } else {
                  slot.markDirty();
              }
          }
          return newStack;
      }
  }