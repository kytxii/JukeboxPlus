package net.kyle.jukeboxplus;

  import net.fabricmc.api.ClientModInitializer;
  import net.kyle.jukeboxplus.registry.ModBlockEntities;
  import net.kyle.jukeboxplus.screen.JukeboxPlusScreen;
  import net.kyle.jukeboxplus.registry.ModScreenHandlers;
  import net.minecraft.client.gui.screen.ingame.HandledScreens;
  import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
  import net.kyle.jukeboxplus.renderer.JukeboxPlusBlockEntityRenderer;

public class JukeboxPlusClient implements ClientModInitializer {
      @Override
      public void onInitializeClient() {
          HandledScreens.register(ModScreenHandlers.JUKEBOX_PLUS, JukeboxPlusScreen::new);
          BlockEntityRendererFactories.register(ModBlockEntities.JUKEBOX_PLUS_BLOCK_ENTITY, JukeboxPlusBlockEntityRenderer::new);
      }
  }