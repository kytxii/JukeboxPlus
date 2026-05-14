package net.kyle.jukeboxplus;

import net.fabricmc.api.ModInitializer;
import net.kyle.jukeboxplus.block.ModBlocks;
import net.kyle.jukeboxplus.block.entity.ModBlockEntities;
import net.kyle.jukeboxplus.item.ModItemGroups;
import net.kyle.jukeboxplus.item.ModItems;
import net.kyle.jukeboxplus.screen.ModScreenHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JukeboxPlus implements ModInitializer {
	public static final String MOD_ID = "jukeboxplus";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.register();
	}
}