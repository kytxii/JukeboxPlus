package net.kyle.jukeboxplus.screen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;


  public class ModScreenHandlers {
      public static ExtendedScreenHandlerType<JukeboxPlusScreenHandler> JUKEBOX_PLUS;

      public static void register() {
          JUKEBOX_PLUS = Registry.register(
              Registries.SCREEN_HANDLER,
              new Identifier("jukeboxplus", "jukebox_plus"),
              new ExtendedScreenHandlerType<>(JukeboxPlusScreenHandler::new)
          );
      }
  }