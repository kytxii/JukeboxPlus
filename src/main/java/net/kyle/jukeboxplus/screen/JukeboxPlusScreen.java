package net.kyle.jukeboxplus.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.kyle.jukeboxplus.registry.ModPackets;
//? if >=1.20
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.kyle.jukeboxplus.util.DiscDurationUtil;
import net.kyle.jukeboxplus.util.DiscCompat;
import net.minecraft.client.MinecraftClient;

public class JukeboxPlusScreen extends HandledScreen<JukeboxPlusScreenHandler> {
    private ButtonWidget playButton;
    private ButtonWidget stopButton;
    private ButtonWidget prevButton;
    private ButtonWidget nextButton;
    private ButtonWidget loopButton;

    private static final Identifier TEXTURE =
        net.kyle.jukeboxplus.JukeboxPlus.id("textures/gui/jukebox_plus_gui.png");

    public JukeboxPlusScreen(JukeboxPlusScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    //? if >=1.20 {
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        //? if >=1.21.2 {
        /*context.drawTexture(net.minecraft.client.render.RenderLayer::getGuiTextured, TEXTURE, x, y, 0f, 0f, backgroundWidth, backgroundHeight, 256, 256);
        *///?} else {
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        //?}

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
    //?} else {
    /*@Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        com.mojang.blaze3d.systems.RenderSystem.setShader(net.minecraft.client.render.GameRenderer::getPositionTexProgram);
        com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        com.mojang.blaze3d.systems.RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // Progress bar
        int maxWidth = 107;
        int barX = x + 55;
        int barY = y + 21;
        int ticks = handler.getTicksPlayed();
        int duration = handler.getDiscDurationTicks();
        int filled = (duration > 0) ? (ticks * maxWidth / duration) : 0;
        fill(matrices, barX, barY, barX + filled, barY + 4, 0xFFC6C6C6);
        fill(matrices, barX + filled, barY, barX + maxWidth, barY + 4, 0xFF222222);

        // Highlight current slot
        int currentSlot = handler.getCurrentSlot();
        int slotX = x + 8 + currentSlot * 18;
        int slotY = y + 55;
        fill(matrices, slotX, slotY, slotX + 16, slotY + 16, 0x44AAFFAA);
    }
    *///?}

    //? if >=1.20 {
    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(textRenderer, title, titleX, titleY, 0x404040, false);
    }
    //?} else {
    /*@Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title, (float) this.titleX, (float) this.titleY, 0x404040);
    }
    *///?}

    //? if >=1.20 {
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        //? if >=1.20.2 {
        /*renderBackground(context, mouseX, mouseY, delta);
        *///?} else {
        renderBackground(context);
        //?}

        int discX = x + 10;
        int discY = y + 16;
        ItemStack disc = handler.getSlot(handler.getCurrentSlot()).getStack();

        if (!disc.isEmpty()) {
            MatrixStack matrices = context.getMatrices();
            matrices.push();
            matrices.translate(discX + 16, discY + 16, 0);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(0));
            matrices.scale(2.0f, 2.0f, 1.0f);
            context.drawItem(disc, -8, -8);
            matrices.pop();
        }

        String loopIcon = switch (handler.getLoopMode()) {
            case OFF      -> "→";
            case LOOP_ONE -> "1";
            case LOOP_ALL -> "∞";
        };
        loopButton.setMessage(Text.literal(loopIcon));

        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
    //?} else {
    /*@Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        renderBackground(matrices);

        int discX = x + 10;
        int discY = y + 16;
        ItemStack disc = handler.getSlot(handler.getCurrentSlot()).getStack();

        if (!disc.isEmpty()) {
            matrices.push();
            matrices.translate(discX + 16, discY + 16, 0);
            matrices.scale(2.0f, 2.0f, 1.0f);
            this.itemRenderer.renderInGui(matrices, disc, -8, -8);
            matrices.pop();
        }

        String loopIcon = switch (handler.getLoopMode()) {
            case OFF      -> "→";
            case LOOP_ONE -> "1";
            case LOOP_ALL -> "∞";
        };
        loopButton.setMessage(Text.literal(loopIcon));

        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
    *///?}

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
            int duration;
            //? if >=1.21 {
            /*duration = DiscCompat.getDurationTicks(MinecraftClient.getInstance().world, disc);
            *///?} else {
            duration = 0;
            if (disc.getItem() instanceof net.minecraft.item.MusicDiscItem musicDisc)
                duration = DiscDurationUtil.getDurationTicks(musicDisc.getSound(), MinecraftClient.getInstance().getResourceManager());
            //?}
            sendAction(handler.getBlockPos(), ModPackets.PREV, duration);
        }).dimensions(x + 55, y + 30, 16, 16).build());

        playButton = addDrawableChild(ButtonWidget.builder(Text.literal("▶"), btn -> {
            ItemStack disc = handler.getSlot(handler.getCurrentSlot()).getStack();
            int duration;
            //? if >=1.21 {
            /*duration = DiscCompat.getDurationTicks(MinecraftClient.getInstance().world, disc);
            *///?} else {
            duration = 0;
            if (disc.getItem() instanceof net.minecraft.item.MusicDiscItem musicDisc) {
                duration = DiscDurationUtil.getDurationTicks(
                    musicDisc.getSound(),
                    MinecraftClient.getInstance().getResourceManager()
                );
            }
            //?}
            sendAction(handler.getBlockPos(), ModPackets.PLAY, duration);
        }).dimensions(x + 75, y + 30, 16, 16).build());

        stopButton = addDrawableChild(ButtonWidget.builder(
            Text.literal("■"), btn -> {
                sendAction(handler.getBlockPos(), ModPackets.STOP, 0);
             })
            .dimensions(x + 95, y + 30, 16, 16).build());

        nextButton = addDrawableChild(ButtonWidget.builder(Text.literal(">"), btn -> {
            int nextSlot = (handler.getCurrentSlot() + 1) % 9;
            ItemStack disc = handler.getSlot(nextSlot).getStack();
            int duration;
            //? if >=1.21 {
            /*duration = DiscCompat.getDurationTicks(MinecraftClient.getInstance().world, disc);
            *///?} else {
            duration = 0;
            if (disc.getItem() instanceof net.minecraft.item.MusicDiscItem musicDisc)
                duration = DiscDurationUtil.getDurationTicks(musicDisc.getSound(), MinecraftClient.getInstance().getResourceManager());
            //?}
            sendAction(handler.getBlockPos(), ModPackets.NEXT, duration);
        }).dimensions(x + 115, y + 30, 16, 16).build());

        loopButton = addDrawableChild(ButtonWidget.builder(
            Text.literal("L"), btn -> {
                sendAction(handler.getBlockPos(), ModPackets.LOOP, 0);
             })
            .dimensions(x + 146, y + 30, 16, 16).build());
    }

    // Client → server: send a control action. The transport differs per version.
    private static void sendAction(BlockPos pos, int action, int duration) {
        //? if >=1.20.5 {
        /*ClientPlayNetworking.send(new ModPackets.JukeboxActionPayload(action, pos, duration));
        *///?} else {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeInt(duration);
        ClientPlayNetworking.send(ModPackets.idFor(action), buf);
        //?}
    }
}
