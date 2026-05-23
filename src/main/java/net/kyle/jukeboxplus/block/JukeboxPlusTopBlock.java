package net.kyle.jukeboxplus.block;

import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.kyle.jukeboxplus.block.entity.JukeboxPlusBlockEntity;
import net.kyle.jukeboxplus.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;

public class JukeboxPlusTopBlock extends Block {
    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 8, 16);

    public JukeboxPlusTopBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
            .with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)
            .with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.DOUBLE_BLOCK_HALF, Properties.HORIZONTAL_FACING);
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
    BlockHitResult hit) {
        BlockPos bottomPos = pos.down();
        BlockState bottomState = world.getBlockState(bottomPos);
        if (!bottomState.isOf(ModBlocks.JUKEBOX_PLUS)) {
            return ActionResult.PASS;
        }
        if (!world.isClient()) {
            BlockEntity blockEntity = world.getBlockEntity(bottomPos);
            if (blockEntity instanceof JukeboxPlusBlockEntity jukeboxEntity) {
                player.openHandledScreen(jukeboxEntity);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
