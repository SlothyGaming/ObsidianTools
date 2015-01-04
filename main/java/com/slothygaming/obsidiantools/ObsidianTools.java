package com.slothygaming.obsidiantools;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.common.util.EnumHelper;



@Mod(modid = ObsidianTools.MODID, name = ObsidianTools.NAME, version = ObsidianTools.VERSION)
public class ObsidianTools
{
	public final static String MODID = "obsidiantools";
	public final static String NAME = "Obsidian Tools";
	public final static String VERSION = "2.0.3";
	
    public static CreativeTabs tabObsidianTools = new CreativeTabs("tabObsidianTools")
    {
		@Override
		public Item getTabIconItem() {
			return ObsidianTools.oPickaxe;
		}
    };

    @Instance("ObsidianTools")
    public static ObsidianTools instance;
    
    @SidedProxy(clientSide="com.slothygaming.obsidiantools.client.OTClP", serverSide="com.slothygaming.obsidiantools.OTCoP")
    public static OTCoP prox;
    
    private ArrayList<Item> ore = new ArrayList();
    private ArrayList<Item> wood = new ArrayList();
    
    public static ToolMaterial obsidian;
    public static Item oSword;
    public static Item oAxe;
    public static Item oHoe;
    public static Item oPickaxe;
    public static Item oSpade;
    public static Item enderPickaxe;
    public static Item enderAxe;
    public static Item enderSpade;
    public static Item enderHoe;

    public static Item enderSword;
    public static Item enderSwordSpecial;

    public static Item EnderCaneItem;
    public static Block EnderCaneBlock;
    public static Block EnderCaneBlock2;

    public static Item enderBag;
    
    private static boolean enderCaneEnabled;

    @EventHandler
    public void load(FMLInitializationEvent evt)
    {
    	this.registerEntities();
    	//this.registerLanguage();
    	if(this.enderCaneEnabled) 
    	OreDictionary.registerOre("materialEnderPearl", this.EnderCaneItem);
    	if(!OreDictionary.getOres("materialEnderPearl").contains(Items.ender_pearl))
    	{
    		OreDictionary.registerOre("materialEnderPearl", Items.ender_pearl);
    	}
    	this.registerCrafting();
    	MinecraftForge.EVENT_BUS.register(new OTEventHandler());
    	prox.rRs();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
    	this.registerOre("oreGold");
    	this.registerOre("oreIron");
    	this.registerOre("oreLapis");
    	this.registerOre("oreDiamond");
    	this.registerOre("oreRedstone");
    	this.registerOre("oreEmerald");
    	this.registerOre("oreQuartz");
    	this.registerOre("oreCoal");
    	
    	this.registerWood("logWood");
    	this.registerWood("woodLog");
    	this.registerWood("treeWood");
    	
    	((ItemEnderAxe) enderAxe).setWoodArray(wood);
    	((ItemEnderPickaxe) enderPickaxe).setOreArray(ore);
    }
    
    //Ore name must be name used to register with OreDictionary
    //Don't be a fuckwit about it, register all ores with OreDictionary
    //OT checks for:
    //Gold, iron, lapis, diamond, redstone, emerald, quartz, coal
    //Register your ores before Post-Init to be compatible with ender tools
    //If you don't even register your ores
    //You are the biggest fuckwit in the modding community
    public void registerOre(String oreName)
    {
    	for(int i = 0; i < OreDictionary.getOres(oreName).size();i++)
    	{
    		if(OreDictionary.getOres(oreName).get(i) == null)
    		{
    			break;
    		}
    		ore.add(OreDictionary.getOres(oreName).get(i).getItem());
    	}
    }
    
    //If for some reason you don't use 'logWood', 'woodLog', or 'treeWood'
    //1. You're a fuckwit, 2. Register your wood's with OreDict using one of those
    //If you don't even register your wood
    //You are a total fuckwit and are throwing out compat with other mods
    public void registerWood(String woodName)
    {
    	for(int i = 0; i < OreDictionary.getOres(woodName).size();i++)
    	{
    		if(OreDictionary.getOres(woodName).get(i) == null)
    		{
    			break;
    		}
    		wood.add(OreDictionary.getOres(woodName).get(i).getItem());
    	}
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {
        instance = this;
        Configuration config = new Configuration(evt.getSuggestedConfigurationFile());
        config.load();
        
        Property oreProp = config.get("General", "OreDictNamesForEnderPick", "oreCopper, oreTin, oreLead, oreUranium");
        oreProp.comment = "Add Mod Ores Here - Use Ore Dictionary Names - I.E. oreCopper";
        
        String ores2 = oreProp.getString();
        String[] configRegister = ores2.split(",");
        if(configRegister.length != 0)
        for(int i = 0; i < configRegister.length; i++)
        {
        	if(configRegister[i]==null)
        	{
        		break;
        	}
        	this.registerOre(configRegister[i].trim());
        }
        
        Property woodProp = config.get("General", "OreDictNamesForEnderAxe", "");
        woodProp.comment = "If mod author is an idiot, add custom wood Ore Dictionary names here";
        
        String woods2 = woodProp.getString();
        String[] woodRegister = woods2.split(",");
        for(int i = 0; i < woodRegister.length; i++)
        {
        	if(woodRegister[i]==null)
        	{
        		break;
        	}
        	this.registerWood(woodRegister[i].trim());
        }
        
        Property enableEnderCane = config.get("TrueFalse", "EnderCaneEnabled", true);
        enableEnderCane.comment = "If you think ender cane is OP, you may disable it";
        this.enderCaneEnabled = enableEnderCane.getBoolean();
        config.save();
        
        
        this.obsidian = EnumHelper.addToolMaterial("OBSIDIAN", 3, 1000, 6.0F, 2, 12);
        
        this.oSword = new ItemOSword(obsidian).setCreativeTab(tabObsidianTools).setUnlocalizedName("oSword").setNoRepair().setTextureName("obsidiantools:oSword");
        this.oAxe = new ItemOAxe(obsidian).setCreativeTab(tabObsidianTools).setUnlocalizedName("oAxe").setNoRepair().setTextureName("obsidiantools:oAxe");
        this.oHoe = new ItemOHoe(obsidian).setCreativeTab(tabObsidianTools).setUnlocalizedName("oHoe").setNoRepair().setTextureName("obsidiantools:oHoe");
        this.oPickaxe = new ItemOPickaxe(obsidian).setCreativeTab(tabObsidianTools).setUnlocalizedName("oPickaxe").setNoRepair().setTextureName("obsidiantools:oPickaxe");
        this.oSpade = new ItemOSpade(obsidian).setCreativeTab(tabObsidianTools).setUnlocalizedName("oSpade").setNoRepair().setTextureName("obsidiantools:oSpade");
        this.enderSword = new ItemEnderSword(false).setCreativeTab(tabObsidianTools).setUnlocalizedName("enderSword").setNoRepair().setTextureName("obsidiantools:enderSword");
        this.enderSwordSpecial = new ItemEnderSword(true).setCreativeTab(tabObsidianTools).setUnlocalizedName("enderSword").setNoRepair().setTextureName("obsidiantools:enderSword");
        this.EnderCaneBlock = new BlockEnderCane().setStepSound(Block.soundTypeGrass).setBlockTextureName("obsidiantools:enderCane").setBlockName("EnderCane");
        this.EnderCaneBlock2 = new BlockEnderCane().setStepSound(Block.soundTypeGrass).setBlockTextureName("obsidiantools:enderCane").setBlockName("EnderCane");
        this.EnderCaneItem = (new ItemReed(this.EnderCaneBlock)).setUnlocalizedName("enderCaneItem").setCreativeTab(this.tabObsidianTools).setTextureName("obsidiantools:enderCane");
        this.enderPickaxe = new ItemEnderPickaxe().setCreativeTab(this.tabObsidianTools).setUnlocalizedName("enderPickaxe").setTextureName("obsidiantools:enderPickaxe");
        this.enderAxe = new ItemEnderAxe().setCreativeTab(this.tabObsidianTools).setUnlocalizedName("enderAxe").setTextureName("obsidiantools:enderAxe");
        this.enderBag = new ItemEnderBag().setCreativeTab(this.tabObsidianTools).setUnlocalizedName("enderBag").setTextureName("obsidiantools:enderBag");
        this.enderSpade = new ItemEnderSpade().setCreativeTab(this.tabObsidianTools).setUnlocalizedName("enderSpade").setTextureName("obsidiantools:enderSpade");
        this.enderHoe = new ItemEnderHoe().setCreativeTab(this.tabObsidianTools).setUnlocalizedName("enderHoe").setTextureName("obsidiantools:enderHoe");
        
        this.registerItems();
        
        //System.out.println("[[[ObsidiantTools Test Code]]]" + OreDictionary.getOreNames());
        //Logger.getAnonymousLogger().log(Level.SEVERE, "[[[ObsidiantTools Test Code]]]" + OreDictionary.getOreNames()[0]);
    }
    
    public void registerCrafting()
    {
    	GameRegistry.addShapedRecipe(new ItemStack(this.oSword), "o", "o", "b", 'o', Blocks.obsidian, 'b', Items.stick);
    	GameRegistry.addShapedRecipe(new ItemStack(this.oAxe), " oo", " bo", " b ", 'o', Blocks.obsidian, 'b', Items.stick);
        GameRegistry.addShapedRecipe(new ItemStack(this.oAxe), "oo ", "ob ", " b ", 'o', Blocks.obsidian, 'b', Items.stick);
        GameRegistry.addShapedRecipe(new ItemStack(this.oHoe), "oo", " b", " b", 'o', Blocks.obsidian, 'b', Items.stick);
        GameRegistry.addShapedRecipe(new ItemStack(this.oPickaxe), "ooo", " b ", " b ", 'o', Blocks.obsidian, 'b', Items.stick);
        GameRegistry.addShapedRecipe(new ItemStack(this.oSpade), "o", "b", "b", 'o', Blocks.obsidian, 'b', Items.stick);
        if(this.enderCaneEnabled){
        GameRegistry.addShapedRecipe(new ItemStack(this.EnderCaneItem), " e ", "ese", " e ", 'e', Items.ender_pearl, 's', Items.reeds);
        GameRegistry.addShapedRecipe(new ItemStack(Items.ender_pearl), "s", 's', this.EnderCaneItem);
        }
        GameRegistry.addShapelessRecipe(new ItemStack(this.enderBag), this.enderBag);
        GameRegistry.addShapedRecipe(new ItemStack(this.enderBag), "ooo", "oeo", "ooo", 'o', Items.leather, 'e', Blocks.ender_chest);
        
        
        
        GameRegistry.addRecipe(new ShapedOreRecipe(this.enderSword, true, new Object[]{"ooo", "obo", Character.valueOf('o'), "materialEnderPearl", Character.valueOf('b'), this.oSword}));
        GameRegistry.addRecipe(new ShapedOreRecipe(this.enderSwordSpecial, true, new Object[]{"ddd", "ooo", "obo", Character.valueOf('o'), "materialEnderPearl", Character.valueOf('b'), this.oSword, Character.valueOf('d'), Items.diamond}));
        GameRegistry.addRecipe(new ShapedOreRecipe(this.enderSwordSpecial, true, new Object[]{" o ", "obo", Character.valueOf('o'), Items.diamond, Character.valueOf('b'), this.enderSword}));
        GameRegistry.addRecipe(new ShapedOreRecipe(this.enderPickaxe, true, new Object[]{"ooo", "obo", Character.valueOf('o'), "materialEnderPearl", Character.valueOf('b'), this.oPickaxe}));
        GameRegistry.addRecipe(new ShapedOreRecipe(this.enderAxe, true, new Object[]{"ooo", "obo", Character.valueOf('o'), "materialEnderPearl", Character.valueOf('b'), this.oAxe}));
        GameRegistry.addRecipe(new ShapedOreRecipe(this.enderSpade, true, new Object[]{"ooo", "obo", Character.valueOf('o'), "materialEnderPearl", Character.valueOf('b'), this.oSpade}));
        GameRegistry.addRecipe(new ShapedOreRecipe(this.enderHoe, true, new Object[]{"ooo", "obo", Character.valueOf('o'), "materialEnderPearl", Character.valueOf('b'), this.oHoe}));
    }
    
    public void registerItems()
    {
    	GameRegistry.registerItem(this.oSword, "oSword");
        GameRegistry.registerItem(this.oAxe, "oAxe");
        GameRegistry.registerItem(this.oHoe, "oHoe");
        GameRegistry.registerItem(this.oPickaxe, "oPickaxe");
        GameRegistry.registerItem(this.oSpade, "oSpade");
        GameRegistry.registerItem(this.enderSword, "enderSword");
        GameRegistry.registerItem(this.enderSwordSpecial, "enderSSword");
        if(this.enderCaneEnabled){
        GameRegistry.registerBlock(this.EnderCaneBlock, "enderCaneBlock");
        GameRegistry.registerBlock(this.EnderCaneBlock2, "enderCaneBlock2");
        GameRegistry.registerItem(this.EnderCaneItem, "enderCaneItem");
        }
        GameRegistry.registerItem(this.enderPickaxe, "enderPickaxe");
        GameRegistry.registerItem(this.enderBag, "enderBag");
        GameRegistry.registerItem(this.enderAxe, "enderAxe");
        GameRegistry.registerItem(this.enderSpade, "enderSpade");
        GameRegistry.registerItem(this.enderHoe, "enderHoe");
    }
    
    public void registerEntities()
    {
    	EntityRegistry.registerModEntity(EntityEnderPearlNoDamage.class, "EnderPearlNoDamage", 230, this, 64, 10, true);
    }
}
