package net.kyle.jukeboxplus;

import net.fabricmc.api.ModInitializer;
import net.kyle.jukeboxplus.registry.ModBlockEntities;
import net.kyle.jukeboxplus.registry.ModBlocks;
import net.kyle.jukeboxplus.registry.ModItemGroups;
import net.kyle.jukeboxplus.registry.ModItems;
import net.kyle.jukeboxplus.registry.ModPackets;
import net.kyle.jukeboxplus.registry.ModScreenHandlers;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JukeboxPlus implements ModInitializer {
	public static final String MOD_ID = "jukeboxplus";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Identifier construction differs across versions; isolate the version split here
	// so every call site can just use JukeboxPlus.id(...) and stay version-agnostic.
	public static Identifier id(String path) {
		return id(MOD_ID, path);
	}

	public static Identifier id(String namespace, String path) {
		//? if >=1.21 {
		/*return Identifier.of(namespace, path);
		*///?} else {
		return new Identifier(namespace, path);
		//?}
	}

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.register();

		ModPackets.registerReceivers();
	}
}