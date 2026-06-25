package net.kyle.jukeboxplus.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.kyle.jukeboxplus.JukeboxPlus;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModItemGroups {
    //? if >=1.20 {
    public static final ItemGroup JUKEBOX_PLUS_GROUP = Registry.register(Registries.ITEM_GROUP,
        JukeboxPlus.id("jukebox_plus"),
        FabricItemGroup.builder()
            .displayName(Text.translatable("itemgroup.jukebox_plus"))
            .icon(() -> new ItemStack(ModBlocks.JUKEBOX_PLUS))
            .entries((displayContext, entries) -> {
                entries.add(ModBlocks.JUKEBOX_PLUS);
            })
            .build());
    //?} else {
    /*public static final ItemGroup JUKEBOX_PLUS_GROUP =
        FabricItemGroup.builder(JukeboxPlus.id("jukebox_plus"))
            .displayName(Text.translatable("itemgroup.jukebox_plus"))
            .icon(() -> new ItemStack(ModBlocks.JUKEBOX_PLUS))
            .entries((displayContext, entries) -> {
                entries.add(ModBlocks.JUKEBOX_PLUS);
            })
            .build();
    *///?}

    public static void registerItemGroups() {
        JukeboxPlus.LOGGER.info("Registering Item Groups for " + JukeboxPlus.MOD_ID);
    }
}
