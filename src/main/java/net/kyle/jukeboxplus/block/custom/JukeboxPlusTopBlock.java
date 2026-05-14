package net.kyle.jukeboxplus.block.custom;

import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.kyle.jukeboxplus.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;

public class JukeboxPlusTopBlock extends Block {
    public JukeboxPlusTopBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
              .with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.DOUBLE_BLOCK_HALF);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            BlockPos bottomPos = pos.down();
            BlockState bottomState = world.getBlockState(bottomPos);
            if (bottomState.isOf(ModBlocks.JUKEBOX_PLUS)) {
                if (!player.isCreative()) {
                    Block.dropStacks(bottomState, world, bottomPos, null, player, player.getMainHandStack());
                }
                world.setBlockState(bottomPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
            }
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockPos bottomPos = pos.down();
        BlockState bottomState = world.getBlockState(bottomPos);
        if (!bottomState.isOf(ModBlocks.JUKEBOX_PLUS)) {
            return ActionResult.PASS;
        }
        return bottomState.onUse(world, player, hand, hit);
    }
}
