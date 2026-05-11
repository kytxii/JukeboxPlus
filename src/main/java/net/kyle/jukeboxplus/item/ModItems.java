package net.kyle.jukeboxplus.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.kyle.jukeboxplus.JukeboxPlus;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item JUKEBOX_PLUS = registerItem("jukebox_plus", new Item(new FabricItemSettings()));

    private static void addItemsToFunctionalItemGroup(FabricItemGroupEntries entries) {
        entries.add(JUKEBOX_PLUS);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(JukeboxPlus.MOD_ID, name), item);
    }

    public static void registerModItems() {
        JukeboxPlus.LOGGER.info("Registering ModItems for " + JukeboxPlus.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ModItems::addItemsToFunctionalItemGroup);
    }
}
