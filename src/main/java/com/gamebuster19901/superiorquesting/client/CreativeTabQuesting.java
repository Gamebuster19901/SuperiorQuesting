package com.gamebuster19901.superiorquesting.client;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public final class CreativeTabQuesting extends CreativeTabs{
	public static final CreativeTabQuesting QUESTING_TAB = new CreativeTabQuesting(Main.MODID);
	public CreativeTabQuesting(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		// TODO Auto-generated method stub
		return new ItemStack(ItemQuestBook.ITEM,1);
	}
	
}
