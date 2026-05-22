package net.kyle.jukeboxplus.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.kyle.jukeboxplus.JukeboxPlus;
import net.kyle.jukeboxplus.block.entity.JukeboxPlusBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static BlockEntityType<JukeboxPlusBlockEntity> JUKEBOX_PLUS_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        JukeboxPlus.LOGGER.info("Registering Block Entities for " + JukeboxPlus.MOD_ID);
        
        JUKEBOX_PLUS_BLOCK_ENTITY = Registry.register(
          Registries.BLOCK_ENTITY_TYPE,
          new Identifier(JukeboxPlus.MOD_ID, "jukebox_plus_block_entity"),
          FabricBlockEntityTypeBuilder.create(
              JukeboxPlusBlockEntity::new,
              ModBlocks.JUKEBOX_PLUS
          ).build()
      );
    }
}