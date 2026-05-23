package net.kyle.jukeboxplus.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.kyle.jukeboxplus.JukeboxPlus;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup JUKEBOX_PLUS_GROUP = Registry.register(Registries.ITEM_GROUP,
        new Identifier(JukeboxPlus.MOD_ID, "jukebox_plus"),
        FabricItemGroup.builder()
            .displayName(Text.translatable("itemgroup.jukebox_plus"))
            .icon(() -> new ItemStack(ModBlocks.JUKEBOX_PLUS))
            .entries((displayContext, entries) -> {
                // Jukebox+ block
                entries.add(ModBlocks.JUKEBOX_PLUS);

                // Vanilla (minecraft:music_discs)
                Registries.ITEM.getEntryList(ItemTags.MUSIC_DISCS)
                    .ifPresent(tag -> tag.forEach(e -> entries.add(e.value())));

                // More Music Discs support
                Registries.ITEM.getIds().stream()
                    .filter(id -> id.getNamespace().equals("morediscs"))
                    .map(Registries.ITEM::get)
                    .filter(item -> item != Items.AIR)
                    .forEach(entries::add);
            })
            .build());

    public static void registerItemGroups() {
        JukeboxPlus.LOGGER.info("Registering Item Groups for " + JukeboxPlus.MOD_ID);
    }
}
