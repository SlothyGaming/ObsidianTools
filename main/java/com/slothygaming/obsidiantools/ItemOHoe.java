package com.slothygaming.obsidiantools;

import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class ItemOHoe extends ItemHoe
{
    public ItemOHoe(ToolMaterial par2EnumToolMaterial)
    {
        super(par2EnumToolMaterial);
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving)
    {
        par1ItemStack.damageItem(0, par3EntityLiving);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLiving)
    {
        par1ItemStack.damageItem(0, par7EntityLiving);
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
        {
            return false;
        }
        else
        {
            UseHoeEvent event = new UseHoeEvent(par2EntityPlayer, par1ItemStack, par3World, par4, par5, par6);

            if (MinecraftForge.EVENT_BUS.post(event))
            {
                return false;
            }

            if (event.getResult() == Result.ALLOW)
            {
                par1ItemStack.damageItem(0, par2EntityPlayer);
                return true;
            }

            Block i1 = par3World.getBlock(par4, par5, par6);
            boolean air = par3World.isAirBlock(par4, par5 + 1, par6);

            //Forge: Change 0 to air, also BugFix: parens mismatch causing you to be able to hoe dirt under dirt/grass
            if (par7 == 0 || !air || (i1 != Blocks.grass && i1 != Blocks.dirt))
            {
                return false;
            }
            else
            {
                Block block = Blocks.farmland;
                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), block.stepSound.soundName, (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

                if (par3World.isRemote)
                {
                    return true;
                }
                else
                {
                    par3World.setBlock(par4, par5, par6, block);
                    par1ItemStack.damageItem(0, par2EntityPlayer);
                    return true;
                }
            }
        }
    }
}
