package com.slothygaming.obsidiantools;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemOSpade extends ItemSpade
{
    public ItemOSpade(ToolMaterial par2EnumToolMaterial)
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
}
