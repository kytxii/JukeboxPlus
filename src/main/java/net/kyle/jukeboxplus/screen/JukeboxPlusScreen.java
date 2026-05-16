package net.kyle.jukeboxplus.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.widget.ButtonWidget;

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
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(textRenderer, title, titleX, titleY, 0x404040, false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
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

        prevButton = addDrawableChild(ButtonWidget.builder(
            Text.literal("<"), btn -> { /* TODO: prev packet */ })
            .dimensions(x + 55, y + 30, 16, 16).build());

        playButton = addDrawableChild(ButtonWidget.builder(
            Text.literal("▶"), btn -> { /* TODO: play packet */ })
            .dimensions(x + 75, y + 30, 16, 16).build());

        stopButton = addDrawableChild(ButtonWidget.builder(
            Text.literal("■"), btn -> { /* TODO: stop packet */ })
            .dimensions(x + 95, y + 30, 16, 16).build());

        nextButton = addDrawableChild(ButtonWidget.builder(
            Text.literal(">"), btn -> { /* TODO: next packet */ })
            .dimensions(x + 115, y + 30, 16, 16).build());

        loopButton = addDrawableChild(ButtonWidget.builder(
            Text.literal("L"), btn -> { /* TODO: cycle loop */ })
            .dimensions(x + 146, y + 30, 16, 16).build());
    }
}