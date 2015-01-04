package com.slothygaming.obsidiantools;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemEnderHoe extends ItemOHoe
{
    public ItemEnderHoe()
    {
        super(ObsidianTools.obsidian);
    }
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World par2World, int x, int y, int z, int meta, float px, float py, float pz)
    {
		for(int i=-1;i<=1;i++)
		{
			for(int j=-1;j<=1;j++)
			{
				if(par2World.getBlock(x+i, y, z+j) != null && (par2World.getBlock(x+i, y, z+j) == Blocks.dirt || par2World.getBlock(x+i, y, z+j) == Blocks.grass) && par2World.getBlock(x+i, y+1, z+j) == Blocks.air)
				{
					par2World.setBlock(x+i, y, z+j, Blocks.farmland);
				}
			}
		}
        return true;
    }
}
