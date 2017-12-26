package com.gamebuster19901.superiorquesting.common.item;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.CreativeTabQuesting;
import com.gamebuster19901.superiorquesting.client.gui.GuiHandler;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ItemQuestBook extends Item{
	public static final ItemQuestBook ITEM = new ItemQuestBook();
	public ItemQuestBook(){
		super();
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabQuesting.QUESTING_TAB);
		this.setUnlocalizedName(MODID + ".questbook");
		this.setRegistryName(new ResourceLocation(MODID + ":questbook"));
		this.maxStackSize = 1;
	}
	
    /**
     * Called when the equipped item is right clicked.
     */
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer p, EnumHand handIn)
    {
		if(p.getHeldItem(handIn).getItemDamage() == 0) {
			p.openGui(Main.getInstance(), GuiHandler.QUEST_BOOK, worldIn, (int)p.posX, (int)p.posY, (int)p.posZ);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, p.getHeldItem(handIn));
		}
		else if(p.canUseCommand(2, "give")){
			p.openGui(Main.getInstance(), GuiHandler.QUEST_BOOK, worldIn, (int)p.posX, (int)p.posY, (int)p.posZ);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, p.getHeldItem(handIn));
		}
		else if(!worldIn.isRemote){
			((EntityPlayerMP) p).connection.sendPacket(new SPacketCustomSound("questing:bookvanish", SoundCategory.PLAYERS, p.posX, p.posY, p.posZ, 1, 1));
			p.getHeldItem(handIn).setCount(0);
			worldIn.addWeatherEffect(new EntityLightningBolt(worldIn, p.posX, p.posY, p.posZ, false));
			worldIn.addWeatherEffect(new EntityLightningBolt(worldIn, p.posX, p.posY, p.posZ, false));
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
    }
	
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return stack.getItemDamage() == 1;
    }
}
