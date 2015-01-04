package com.slothygaming.obsidiantools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.MinecraftForge;

public class ItemEnderBag extends Item
{
	
	public ItemEnderBag() {
		super();
		this.maxStackSize = 1;
	}
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
	    itemStack.setTagCompound(new NBTTagCompound());
	    itemStack.getTagCompound().setBoolean("hasSet", false);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
		Block blockID;
		int blockX;
		int blockY;
		int blockZ;
		if(itemstack.getTagCompound() == null)
		{
			itemstack.setTagCompound(new NBTTagCompound());
			return true;
		}
		else
		if(itemstack.getTagCompound().getBoolean("hasSet") == false)
		{
			itemstack.getTagCompound().setInteger("BlockX", x);
			itemstack.getTagCompound().setInteger("BlockY", y);
			itemstack.getTagCompound().setInteger("BlockZ", z);
			itemstack.getTagCompound().setInteger("WorldNumber", world.provider.dimensionId);
			itemstack.getTagCompound().setBoolean("hasSet", true);
			itemstack.getTagCompound().setString("BlockName", world.getBlock(x, y, z).getLocalizedName());
			return true;
		}
		else
		{
			blockX = itemstack.getTagCompound().getInteger("BlockX");
			blockY = itemstack.getTagCompound().getInteger("BlockY");
			blockZ = itemstack.getTagCompound().getInteger("BlockZ");
			blockID = world.getBlock(blockX, blockY, blockZ);
			if(blockID != null)
			{
				WorldServer ws1 = MinecraftServer.getServer().worldServerForDimension(itemstack.getTagCompound().getInteger("WorldNumber"));
				if(!ws1.isRemote)
				if(blockID.onBlockActivated(ws1, blockX, blockY, blockZ, player, par7, par8, par9, par10) == false)
				{
					itemstack.getTagCompound().setBoolean("hasSet", false);
					itemstack.getTagCompound().setInteger("BlockX", 0);
					itemstack.getTagCompound().setInteger("BlockY", 0);
					itemstack.getTagCompound().setInteger("BlockZ", 0);
					itemstack.getTagCompound().setInteger("WorldNumber", 0);
					itemstack.getTagCompound().setString("BlockName", "");
					return false;
				}
			}
			else
			{
				itemstack.getTagCompound().setBoolean("hasSet", false);
				itemstack.getTagCompound().setInteger("BlockX", 0);
				itemstack.getTagCompound().setInteger("BlockY", 0);
				itemstack.getTagCompound().setInteger("BlockZ", 0);
				itemstack.getTagCompound().setInteger("WorldNumber", 0);
				itemstack.getTagCompound().setString("BlockName", "");
			}
			return true;
		}
    }
	
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
		Block blockID;
		int blockX;
		int blockY;
		int blockZ;
		if(itemstack.getTagCompound() != null)
		{
			blockX = itemstack.getTagCompound().getInteger("BlockX");
			blockY = itemstack.getTagCompound().getInteger("BlockY");
			blockZ = itemstack.getTagCompound().getInteger("BlockZ");
			blockID = world.getBlock(blockX, blockY, blockZ);
			if(blockID != null)
			{
				WorldServer ws1 = MinecraftServer.getServer().worldServerForDimension(itemstack.getTagCompound().getInteger("WorldNumber"));
				if(!ws1.isRemote){
				if(blockID.onBlockActivated(ws1, blockX, blockY, blockZ, player, 0, 0.0F, 0.0F, 0.0F) == false)
				{
					itemstack.getTagCompound().setBoolean("hasSet", false);
					itemstack.getTagCompound().setInteger("BlockX", 0);
					itemstack.getTagCompound().setInteger("BlockY", 0);
					itemstack.getTagCompound().setInteger("BlockZ", 0);
					itemstack.getTagCompound().setInteger("WorldNumber", 0);
					itemstack.getTagCompound().setString("BlockName", "");
				}}
			}
			else
			{
				itemstack.getTagCompound().setBoolean("hasSet", false);
				itemstack.getTagCompound().setInteger("BlockX", 0);
				itemstack.getTagCompound().setInteger("BlockY", 0);
				itemstack.getTagCompound().setInteger("BlockZ", 0);
				itemstack.getTagCompound().setInteger("WorldNumber", 0);
				itemstack.getTagCompound().setString("BlockName", "");
			}
		}
		else
		{
			itemstack.setTagCompound(new NBTTagCompound());
		}
        return itemstack;
    }
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4)
	{
		super.addInformation(itemstack, player, list, par4);
		if(itemstack.getTagCompound() != null)
		{
			list.add(itemstack.getTagCompound().getString("BlockName"));
			list.add("X: " + itemstack.getTagCompound().getInteger("BlockX")+ " Y: " + itemstack.getTagCompound().getInteger("BlockY") + " Z: " + itemstack.getTagCompound().getInteger("BlockZ"));
			list.add("Dimension: " + itemstack.getTagCompound().getInteger("WorldNumber"));
		}
	}
}
