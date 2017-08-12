package com.gamebuster19901.superiorquesting.common.item;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.CreativeTabQuesting;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemQuestBook extends Item{
	public static ItemQuestBook ITEM = new ItemQuestBook();
	public ItemQuestBook(){
		super();
		this.setCreativeTab(CreativeTabQuesting.TAB);
		this.setUnlocalizedName(Main.MODID + ".questbook");
		this.setRegistryName(new ResourceLocation(Main.MODID + ":questbook"));
	}
}
