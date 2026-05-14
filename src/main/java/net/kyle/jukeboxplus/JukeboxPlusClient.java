package net.kyle.jukeboxplus;

  import net.fabricmc.api.ClientModInitializer;
  import net.kyle.jukeboxplus.screen.JukeboxPlusScreen;
  import net.kyle.jukeboxplus.screen.ModScreenHandlers;
  import net.minecraft.client.gui.screen.ingame.HandledScreens;

  public class JukeboxPlusClient implements ClientModInitializer {
      @Override
      public void onInitializeClient() {
          HandledScreens.register(ModScreenHandlers.JUKEBOX_PLUS, JukeboxPlusScreen::new);
      }
  }