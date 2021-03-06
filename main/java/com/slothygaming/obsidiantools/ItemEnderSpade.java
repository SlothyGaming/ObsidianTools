package com.slothygaming.obsidiantools;

import java.util.ArrayList;
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

public class ItemEnderSpade extends ItemOSpade
{
	private ArrayList<Item> dig = new ArrayList();
	
    public ItemEnderSpade()
    {
        super(ObsidianTools.obsidian);
        dig.add(Item.getItemFromBlock(Blocks.clay));
        dig.add(Item.getItemFromBlock(Blocks.gravel));
    }
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World par2World, int x, int y, int z, int meta, float px, float py, float pz)
    {
			if(dig.contains(par2World.getBlock(x, y, z).getItem(par2World, x, y, z)))
			{
				//par2World.destroyBlock(x, y, z, true);
				this.repeatMine(par2World, x, y, z, (EntityPlayer) player, dig);
			}
        return true;
    }
    
	public void repeatMine(World world, int x, int y, int z, EntityPlayer player, ArrayList dig)
	{
		for(int i = -1; i <= 1; i++)
		{
			for(int j = -1; j <= 1; j++)
			{
				for(int k = -1; k <= 1; k++)
				{
					if(dig.contains(world.getBlock(x+i, y+j, z+k).getItem(world, x+i, y+j, z+k)))
					{
						//world.destroyBlock(x + i, y + j, z + k, true);
						world.getBlock(x + i, y + j, z + k).harvestBlock(world, player, x + i, y + j, z + k, world.getBlockMetadata(x + i, y + j, z + k));
						//world.destroyBlock(x + i, y + j, z + k, false);
						world.func_147480_a(x+i, y+j, z+k, false);
						this.repeatMine(world, x + i, y + j, z + k, player, dig);
					}
				}
			}
		}
	}
}
