package net.kyle.jukeboxplus.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.kyle.jukeboxplus.JukeboxPlus;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup JUKEBOX_PLUS_GROUP = Registry.register(Registries.ITEM_GROUP,
        new Identifier(JukeboxPlus.MOD_ID, "jukebox_plus"),
        FabricItemGroup.builder().displayName(Text.translatable("itemgroup.jukebox_plus"))
            .icon(() -> new ItemStack(ModItems.JUKEBOX_PLUS)).entries((displayContext, entries) -> {
                entries.add(ModItems.JUKEBOX_PLUS);
                // Add discs
            }).build());

    public static void registerItemGroups() {
        JukeboxPlus.LOGGER.info("Registering Item Groups for " + JukeboxPlus.MOD_ID);
    }
}
