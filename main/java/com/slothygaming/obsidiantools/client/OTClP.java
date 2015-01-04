package com.slothygaming.obsidiantools.client;

import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;

import com.slothygaming.obsidiantools.OTCoP;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class OTClP extends OTCoP {
	@Override
	public void rRs()
	{
		RenderingRegistry.registerEntityRenderingHandler(com.slothygaming.obsidiantools.EntityEnderPearlNoDamage.class, new RenderSnowball(Items.ender_pearl));
	}
}
