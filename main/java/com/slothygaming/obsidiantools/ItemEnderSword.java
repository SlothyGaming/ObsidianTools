package com.slothygaming.obsidiantools;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class ItemEnderSword extends ItemOSword
{
    private final boolean special;

    public ItemEnderSword(boolean special)
    {
        super(ObsidianTools.obsidian);
        this.special = special;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par3EntityPlayer.ridingEntity != null)
        {
            return par1ItemStack;
        }
        else
        {
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!par2World.isRemote)
            {
                if (this.special)
                {
                    par2World.spawnEntityInWorld(new EntityEnderPearlNoDamage(par2World, par3EntityPlayer));
                }
                else
                {
                    par2World.spawnEntityInWorld(new EntityEnderPearl(par2World, par3EntityPlayer));
                }
            }

            return par1ItemStack;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return this.special ? EnumRarity.epic : EnumRarity.common;
    }

    @Override
    public int getItemEnchantability()
    {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack)
    {
        return this.special ? true : false;
    }
}
