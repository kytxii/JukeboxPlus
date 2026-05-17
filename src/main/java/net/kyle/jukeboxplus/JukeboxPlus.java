package net.kyle.jukeboxplus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyle.jukeboxplus.block.ModBlocks;
import net.kyle.jukeboxplus.block.entity.JukeboxPlusBlockEntity;
import net.kyle.jukeboxplus.block.entity.ModBlockEntities;
import net.kyle.jukeboxplus.item.ModItemGroups;
import net.kyle.jukeboxplus.item.ModItems;
import net.kyle.jukeboxplus.screen.ModPackets;
import net.kyle.jukeboxplus.screen.ModScreenHandlers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;

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

		ServerPlayNetworking.registerGlobalReceiver(ModPackets.PLAY, (server, player, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			int duration = buf.readInt();
			server.execute(() -> {
				if (player.getWorld().getBlockEntity(pos) instanceof JukeboxPlusBlockEntity j) {
					j.setPlaying(true);
					j.setTicksPlayed(0);
					j.setDiscDurationTicks(duration);
					player.getWorld().syncWorldEvent(1011, pos, 0);
					ItemStack disc = j.getStack(j.getCurrentSlot());
					if (disc.getItem() instanceof MusicDiscItem musicDisc) {
						player.getWorld().syncWorldEvent(1010, pos, Item.getRawId(disc.getItem()));
					}
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(ModPackets.STOP, (server, player, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			server.execute(() -> {
				if (player.getWorld().getBlockEntity(pos) instanceof JukeboxPlusBlockEntity j) {
					j.setPlaying(false);
					j.setTicksPlayed(0);
					player.getWorld().syncWorldEvent(1011, pos, 0);
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(ModPackets.NEXT, (server, player, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			int duration = buf.readInt();
			server.execute(() -> {
				if (player.getWorld().getBlockEntity(pos) instanceof JukeboxPlusBlockEntity j) {
					player.getWorld().syncWorldEvent(1011, pos, 0);
					j.setCurrentSlot((j.getCurrentSlot() + 1) % 9);
					j.setTicksPlayed(0);
					j.setDiscDurationTicks(duration);
					j.setPlaying(true);
					ItemStack disc = j.getStack(j.getCurrentSlot());
					if (disc.getItem() instanceof MusicDiscItem)
						player.getWorld().syncWorldEvent(1010, pos, Item.getRawId(disc.getItem()));
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(ModPackets.PREV, (server, player, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			int duration = buf.readInt();
			server.execute(() -> {
				if (player.getWorld().getBlockEntity(pos) instanceof JukeboxPlusBlockEntity j) {
					player.getWorld().syncWorldEvent(1011, pos, 0);
					j.setCurrentSlot((j.getCurrentSlot() + 8) % 9);
					j.setTicksPlayed(0);
					j.setDiscDurationTicks(duration);
					j.setPlaying(true);
					ItemStack disc = j.getStack(j.getCurrentSlot());
					if (disc.getItem() instanceof MusicDiscItem)
						player.getWorld().syncWorldEvent(1010, pos, Item.getRawId(disc.getItem()));
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(ModPackets.LOOP_TOGGLE, (server, player, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			server.execute(() -> {
				if (player.getWorld().getBlockEntity(pos) instanceof JukeboxPlusBlockEntity j) {
					JukeboxPlusBlockEntity.LoopMode next = switch (j.getLoopMode()) {
						case OFF -> JukeboxPlusBlockEntity.LoopMode.LOOP_ONE;
						case LOOP_ONE -> JukeboxPlusBlockEntity.LoopMode.LOOP_ALL;
						case LOOP_ALL -> JukeboxPlusBlockEntity.LoopMode.OFF;
				};
				j.setLoopMode(next);
			}});
		});
	}
}