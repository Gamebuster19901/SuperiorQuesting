package com.gamebuster19901.superiorquesting.common.item;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.CreativeTabQuesting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public final class ItemQuestBook extends Item{
	public static final ItemQuestBook ITEM = new ItemQuestBook();
	public ItemQuestBook(){
		super();
		this.setCreativeTab(CreativeTabQuesting.QUESTING_TAB);
		this.setUnlocalizedName(Main.MODID + ".questbook");
		this.setRegistryName(new ResourceLocation(Main.MODID + ":questbook"));
	}
	
    /**
     * Called when the equipped item is right clicked.
     */
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if(!worldIn.isRemote) {
			
		}
		else {
			
		}
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
}
