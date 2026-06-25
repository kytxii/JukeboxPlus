package net.kyle.jukeboxplus.registry;

import net.minecraft.block.AbstractBlock;
import net.kyle.jukeboxplus.JukeboxPlus;
import net.kyle.jukeboxplus.block.JukeboxPlusBlock;
import net.kyle.jukeboxplus.block.JukeboxPlusTopBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
//? if >=1.21.2 {
/*import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
*///?}

public class ModBlocks {
    public static final Block JUKEBOX_PLUS = registerBlock("jukebox_plus_block", new JukeboxPlusBlock(blockSettings("jukebox_plus_block")));
    public static final Block JUKEBOX_PLUS_TOP = registerBlockWithoutItem("jukebox_plus_block_top", new JukeboxPlusTopBlock(blockSettings("jukebox_plus_block_top")));

    // 1.21.2+ requires the block's registry key on its Settings before construction
    // (the loot-table key is derived from it). Older versions have no registryKey().
    private static AbstractBlock.Settings blockSettings(String name) {
        AbstractBlock.Settings settings = AbstractBlock.Settings.copy(Blocks.JUKEBOX).nonOpaque();
        //? if >=1.21.2 {
        /*settings = settings.registryKey(RegistryKey.of(RegistryKeys.BLOCK, JukeboxPlus.id(name)));
        *///?}
        return settings;
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, JukeboxPlus.id(name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        Item.Settings settings = new Item.Settings();
        //? if >=1.21.2 {
        /*settings = settings.registryKey(RegistryKey.of(RegistryKeys.ITEM, JukeboxPlus.id(name)));
        *///?}
        return Registry.register(Registries.ITEM, JukeboxPlus.id(name),
            new BlockItem(block, settings));
    }

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK,
                JukeboxPlus.id(name), block);
    }

    public static void registerModBlocks() {
        JukeboxPlus.LOGGER.info("Registering Mod Blocks for " + JukeboxPlus.MOD_ID);
    }

}
