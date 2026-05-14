package net.kyle.jukeboxplus.screen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.resource.featuretoggle.FeatureFlags;

public class ModScreenHandlers {
      public static ScreenHandlerType<JukeboxPlusScreenHandler> JUKEBOX_PLUS;

      public static void register() {
        JUKEBOX_PLUS = Registry.register(
            Registries.SCREEN_HANDLER,
            new Identifier("jukeboxplus", "jukebox_plus"),
            new ScreenHandlerType<>(JukeboxPlusScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
        );
      }
  }