package net.kyle.jukeboxplus.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class JukeboxPlusBlockEntity extends BlockEntity {

    public JukeboxPlusBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.JUKEBOX_PLUS_BLOCK_ENTITY, pos, state);
    }
}