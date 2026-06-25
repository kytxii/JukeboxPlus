package net.kyle.jukeboxplus.registry;

import net.kyle.jukeboxplus.screen.JukeboxPlusScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.kyle.jukeboxplus.JukeboxPlus;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;


  public class ModScreenHandlers {
      //? if >=1.20.5 {
      /*public static ExtendedScreenHandlerType<JukeboxPlusScreenHandler, BlockPos> JUKEBOX_PLUS;
      *///?} else {
      public static ExtendedScreenHandlerType<JukeboxPlusScreenHandler> JUKEBOX_PLUS;
      //?}

      public static void register() {
          JUKEBOX_PLUS = Registry.register(
              Registries.SCREEN_HANDLER,
              JukeboxPlus.id("jukebox_plus"),
              //? if >=1.20.5 {
              /*new ExtendedScreenHandlerType<>(JukeboxPlusScreenHandler::new, BlockPos.PACKET_CODEC)
              *///?} else {
              new ExtendedScreenHandlerType<>(JukeboxPlusScreenHandler::new)
              //?}
          );
      }
  }