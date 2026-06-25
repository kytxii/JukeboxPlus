package net.kyle.jukeboxplus.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.state.property.Properties;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
//? if >=1.20.3
//import com.mojang.serialization.MapCodec;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.kyle.jukeboxplus.block.entity.JukeboxPlusBlockEntity;
import net.kyle.jukeboxplus.registry.ModBlockEntities;
import net.kyle.jukeboxplus.registry.ModBlocks;
import net.kyle.jukeboxplus.util.DiscCompat;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class JukeboxPlusBlock extends BlockWithEntity {

    //? if >=1.20.3 {
    /*public static final MapCodec<JukeboxPlusBlock> CODEC = createCodec(JukeboxPlusBlock::new);

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }
    *///?}

    public JukeboxPlusBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
        .with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
        .with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.DOUBLE_BLOCK_HALF, Properties.HORIZONTAL_FACING);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.up()).isAir();
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) {
            if (canPlaceAt(state, world, pos)) {
                world.setBlockState(
                    pos.up(),
                    ModBlocks.JUKEBOX_PLUS_TOP.getDefaultState()
                        .with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)
                        .with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING)),
                    Block.NOTIFY_ALL);
            }
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    //? if >=1.20.3 {
    /*public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    *///?} else {
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    //?}
        if (!world.isClient()) {
            BlockPos topPos = pos.up();
            DiscCompat.stop(world, pos);

            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof JukeboxPlusBlockEntity jukeboxEntity) {
                ItemScatterer.spawn(world, pos, jukeboxEntity);
                jukeboxEntity.clear();
            }

            if (world.getBlockState(topPos).isOf(ModBlocks.JUKEBOX_PLUS_TOP)) {
                world.setBlockState(topPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
            }
        }
        //? if >=1.20.3 {
        /*return super.onBreak(world, pos, state, player);
        *///?} else {
        super.onBreak(world, pos, state, player);
        //?}
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new JukeboxPlusBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    //? if >=1.20.5 {
    /*public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
    *///?} else {
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    //?}
        if (!world.isClient()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof JukeboxPlusBlockEntity jukeboxEntity) {
                player.openHandledScreen(jukeboxEntity);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient()) return null;
        //? if >=1.20.3 {
        /*return validateTicker(type, ModBlockEntities.JUKEBOX_PLUS_BLOCK_ENTITY,
            (w, pos, s, be) -> be.tick(w, pos, s));
        *///?} else {
        return checkType(type, ModBlockEntities.JUKEBOX_PLUS_BLOCK_ENTITY,
            (w, pos, s, be) -> be.tick(w, pos, s));
        //?}
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState()
            .with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite())
            .with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
    }
}