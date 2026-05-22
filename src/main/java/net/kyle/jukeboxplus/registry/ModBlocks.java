package net.kyle.jukeboxplus.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.kyle.jukeboxplus.JukeboxPlus;
import net.kyle.jukeboxplus.block.JukeboxPlusBlock;
import net.kyle.jukeboxplus.block.JukeboxPlusTopBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block JUKEBOX_PLUS = registerBlock("jukebox_plus_block", new JukeboxPlusBlock(FabricBlockSettings.copyOf(Blocks.JUKEBOX).nonOpaque()));
    public static final Block JUKEBOX_PLUS_TOP = registerBlockWithoutItem("jukebox_plus_block_top", new JukeboxPlusTopBlock(FabricBlockSettings.copyOf(Blocks.JUKEBOX).nonOpaque()));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(JukeboxPlus.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(JukeboxPlus.MOD_ID, name), 
            new BlockItem(block, new FabricItemSettings()));
    }

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK,
                new Identifier(JukeboxPlus.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        JukeboxPlus.LOGGER.info("Registering Mod Blocks for " + JukeboxPlus.MOD_ID);
    }
    
}
