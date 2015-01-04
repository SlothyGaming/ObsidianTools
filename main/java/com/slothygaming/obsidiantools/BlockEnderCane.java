package com.slothygaming.obsidiantools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockReed;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockEnderCane extends BlockReed
{
    protected BlockEnderCane()
    {
    	super();
        float f = 0.375F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        this.setTickRandomly(true);
        this.blockHardness = 0.0F;
        this.setLightLevel(0.5F);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	int l;
        int metadata = par1World.getBlockMetadata(par2, par3, par4);
    	
        for (l = 1; par1World.getBlock(par2, par3 - l, par4) == ObsidianTools.EnderCaneBlock || par1World.getBlock(par2, par3 - l, par4) == ObsidianTools.EnderCaneBlock2; ++l)
        {
        	;
        }
        if(this == ObsidianTools.EnderCaneBlock2) {
        if (l < 4 && par1World.isAirBlock(par2, par3 + 1, par4))
        {
        	if (metadata >= 3)
            {
        		if(par5Random.nextBoolean())
        		{
               		int newBX = par2 + par5Random.nextInt(3) - 1;
               		int newBY = par3 + par5Random.nextInt(3) - 1;
               		int newBZ = par4 + par5Random.nextInt(3) - 1;
               		if(par1World.getBlock(newBX, newBY - 1, newBZ) == Blocks.obsidian && par1World.isAirBlock(newBX, newBY, newBZ))
               		{
               			par1World.setBlock(newBX, newBY, newBZ, ObsidianTools.EnderCaneBlock);
               		} else {
               			par1World.setBlock(par2, par3 + 1, par4, ObsidianTools.EnderCaneBlock);
               		}
               	}
               	else {
               		par1World.setBlock(par2, par3 + 1, par4, ObsidianTools.EnderCaneBlock);
               	}
        		par1World.setBlock(par2, par3, par4, ObsidianTools.EnderCaneBlock);
            }
            else
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, metadata+1, 4);
            }
        }
        else
        {
        	if(metadata >= 3)
           	{
        		boolean growing = true;
        		for(int i = 0; i <= 4 && growing; i++)
        		{
        			int newBX = par2 + par5Random.nextInt(3) - 1;
        			int newBY = par3 + par5Random.nextInt(3) - 1;
        			int newBZ = par4 + par5Random.nextInt(3) - 1;
        			if(par1World.getBlock(newBX, newBY - 1, newBZ) == Blocks.obsidian && par1World.isAirBlock(newBX, newBY, newBZ))
        			{
        				par1World.setBlock(newBX, newBY, newBZ, ObsidianTools.EnderCaneBlock);
        				growing = false;
        			}
        			par1World.setBlock(par2, par3, par4, ObsidianTools.EnderCaneBlock);
        		}
           	}
        	else
        	{
        		par1World.setBlockMetadataWithNotify(par2, par3, par4, metadata+1, 4);
        	}
        }
        }
        else if(this == ObsidianTools.EnderCaneBlock)
        {
        	if(metadata >= 15)
        	{
        		par1World.setBlock(par2, par3, par4, ObsidianTools.EnderCaneBlock2);
        	}
        	else
        	{
        		par1World.setBlockMetadataWithNotify(par2, par3, par4, metadata+1, 4);
        	}
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
            par1World.spawnParticle("portal", par2 + 0.5, par3 + par5Random.nextDouble() * 2.0D, par4 + 0.5, par5Random.nextGaussian(), 0.0D, par5Random.nextGaussian());
    }
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        Block block = par1World.getBlock(par2, par3 - 1, par4);
        return (block == Blocks.obsidian || block == this || block == ObsidianTools.EnderCaneBlock || block == ObsidianTools.EnderCaneBlock2);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        this.checkBlockCoordValid(par1World, par2, par3, par4);
    }

    /**
     * Checks if current block pos is valid, if not, breaks the block as dropable item. Used for reed and cactus.
     */
    protected final void checkBlockCoordValid(World par1World, int par2, int par3, int par4)
    {
        if (!this.canBlockStay(par1World, par2, par3, par4))
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    @Override
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        return this.canPlaceBlockAt(par1World, par2, par3, par4);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3)
    {
        return ObsidianTools.EnderCaneItem;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 1;
    }

    @SideOnly(Side.CLIENT)

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    @Override
    public Item getItem(World par1World, int par2, int par3, int par4)
    {
        return ObsidianTools.EnderCaneItem;
    }
}
