package com.slothygaming.obsidiantools;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.eventhandler.Event;

public class OTEventHandler {
	
	@SubscribeEvent
	public void onPlayerOpenContainerEvent(PlayerOpenContainerEvent event)
	{
		if(event.entityPlayer != null && event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == ObsidianTools.enderBag)
		{
			if(event.entityPlayer.getHeldItem().getTagCompound() != null && event.entityPlayer.getHeldItem().getTagCompound().getBoolean("hasSet"))
			{
				MinecraftServer ms = MinecraftServer.getServer();
				WorldServer ws1= ms.worldServerForDimension(event.entityPlayer.getHeldItem().getTagCompound().getInteger("WorldNumber"));
				event.setResult(Event.Result.ALLOW);
			}
		}
	}
}