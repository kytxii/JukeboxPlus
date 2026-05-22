package net.kyle.jukeboxplus.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.kyle.jukeboxplus.registry.ModPackets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.kyle.jukeboxplus.util.DiscDurationUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.MusicDiscItem;

public class JukeboxPlusScreen extends HandledScreen<JukeboxPlusScreenHandler> {
    private ButtonWidget playButton;
    private ButtonWidget stopButton;
    private ButtonWidget prevButton;
    private ButtonWidget nextButton;
    private ButtonWidget loopButton;
    
    private static final Identifier TEXTURE =
        new Identifier("jukeboxplus", "textures/gui/jukebox_plus_gui.png");

    public JukeboxPlusScreen(JukeboxPlusScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // Progress bar
        int maxWidth = 107;
        int barX = x + 55;
        int barY = y + 21;
        int ticks = handler.getTicksPlayed();
        int duration = handler.getDiscDurationTicks(); 
        int filled = (duration > 0) ? (ticks * maxWidth / duration) : 0;
        context.fill(barX, barY , barX + filled, barY + 4, 0xFFC6C6C6);
        context.fill(barX + filled, barY, barX + maxWidth, barY + 4, 0xFF222222);

        // Highlight current slot
        int currentSlot = handler.getCurrentSlot();
        int slotX = x + 8 + currentSlot * 18;
        int slotY = y + 55;
        context.fill(slotX, slotY, slotX + 16, slotY + 16, 0x44AAFFAA);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(textRenderer, title, titleX, titleY, 0x404040, false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        renderBackground(context);

        int discX = x + 10;
        int discY = y + 16;
        ItemStack disc = handler.getSlot(handler.getCurrentSlot()).getStack();

        if (!disc.isEmpty()) {
            MatrixStack matrices = context.getMatrices();
            matrices.push();
            matrices.translate(discX, discY, 0);
            matrices.scale(2.0f, 2.0f, 1.0f);
            context.drawItem(disc, 0, 0);
            matrices.pop();
        }

        String loopIcon = switch (handler.getLoopMode()) {
            case OFF      -> "\u2192";
            case LOOP_ONE -> "\u21BA";
            case LOOP_ALL -> "\u221E";
        };
        loopButton.setMessage(Text.literal(loopIcon));

        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        titleX = 7;
        titleY = 5;

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        prevButton = addDrawableChild(ButtonWidget.builder(Text.literal("<"), btn -> {
            int nextSlot = (handler.getCurrentSlot() + 8) % 9;
            ItemStack disc = handler.getSlot(nextSlot).getStack();
            int duration = 0;
            if (disc.getItem() instanceof MusicDiscItem musicDisc)
                duration = DiscDurationUtil.getDurationTicks(musicDisc.getSound(), MinecraftClient.getInstance().getResourceManager());
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(handler.getBlockPos());
            buf.writeInt(duration);
            ClientPlayNetworking.send(ModPackets.PREV, buf);
        }).dimensions(x + 55, y + 30, 16, 16).build());

        playButton = addDrawableChild(ButtonWidget.builder(Text.literal("▶"), btn -> {
            ItemStack disc = handler.getSlot(handler.getCurrentSlot()).getStack();
            int duration = 0;
            if (disc.getItem() instanceof MusicDiscItem musicDisc) {
                duration = DiscDurationUtil.getDurationTicks(
                    musicDisc.getSound(),
                    MinecraftClient.getInstance().getResourceManager()
                );
            }
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(handler.getBlockPos());
            buf.writeInt(duration);
            ClientPlayNetworking.send(ModPackets.PLAY, buf);
        }).dimensions(x + 75, y + 30, 16, 16).build());

        stopButton = addDrawableChild(ButtonWidget.builder(
            Text.literal("■"), btn -> { 
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(handler.getBlockPos());
                ClientPlayNetworking.send(ModPackets.STOP, buf);
             })
            .dimensions(x + 95, y + 30, 16, 16).build());

        nextButton = addDrawableChild(ButtonWidget.builder(Text.literal(">"), btn -> {
            int nextSlot = (handler.getCurrentSlot() + 1) % 9;
            ItemStack disc = handler.getSlot(nextSlot).getStack();
            int duration = 0;
            if (disc.getItem() instanceof MusicDiscItem musicDisc)
                duration = DiscDurationUtil.getDurationTicks(musicDisc.getSound(), MinecraftClient.getInstance().getResourceManager());
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(handler.getBlockPos());
            buf.writeInt(duration);
            ClientPlayNetworking.send(ModPackets.NEXT, buf);
        }).dimensions(x + 115, y + 30, 16, 16).build());

        loopButton = addDrawableChild(ButtonWidget.builder(
            Text.literal("L"), btn -> { 
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(handler.getBlockPos());
                ClientPlayNetworking.send(ModPackets.LOOP_TOGGLE, buf);
             })
            .dimensions(x + 146, y + 30, 16, 16).build());
    }
}