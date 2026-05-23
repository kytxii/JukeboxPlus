package net.kyle.jukeboxplus.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.kyle.jukeboxplus.JukeboxPlus;
import net.minecraft.item.ItemGroups;

public class ModItems {

    private static void addItemsToFunctionalItemGroup(FabricItemGroupEntries entries) {
        entries.add(ModBlocks.JUKEBOX_PLUS);
    }


    public static void registerModItems() {
        JukeboxPlus.LOGGER.info("Registering ModItems for " + JukeboxPlus.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ModItems::addItemsToFunctionalItemGroup);
    }
}
