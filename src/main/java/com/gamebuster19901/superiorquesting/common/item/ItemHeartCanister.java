package com.gamebuster19901.superiorquesting.common.item;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.CreativeTabQuesting;
import com.gamebuster19901.superiorquesting.common.LifeHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ItemHeartCanister extends Item{
	public static final ItemHeartCanister ITEM = new ItemHeartCanister();
	private static final LifeHandler LIFE_HANDLER = Main.proxy.getLifeHandler();
	
	public ItemHeartCanister(){
		super();
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabQuesting.QUESTING_TAB);
		this.setUnlocalizedName(Main.MODID + ".heartcanister");
		this.setRegistryName(new ResourceLocation(Main.MODID + ":heartcanister"));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		int i = stack.getMetadata();
		return super.getUnlocalizedName() + getUnlocalizedNameSuffix(i);
	}
	
	private String getUnlocalizedNameSuffix(int i){
		String[] suffixes = new String[]{"full", "threequarters", "half", "quarter", "empty"};
		if (i <= 3){
			return suffixes[i];
		}
		else{
			return suffixes[4];
		}
	}

    /**
     * Called when the equipped item is right clicked.
     */
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if(itemstack.getMetadata() == 0){
        	if (playerIn instanceof EntityPlayerMP)
        	{
        		EntityPlayerMP p = (EntityPlayerMP)playerIn;
        		double lives = LIFE_HANDLER.getLives(p);
        		if(Double.isFinite(lives) && LIFE_HANDLER.addLife(p)){
        			itemstack.shrink(1);
        			p.sendMessage(new TextComponentTranslation("questing.life.change.add", LIFE_HANDLER.getLives(p)));
        			LIFE_HANDLER.messageLives(p);
        			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        		}
            
        		else if (Double.isInfinite(lives)){
        			p.sendMessage(new TextComponentTranslation("questing.life.change.infinity"));
        		}
        		
        		else{
        			p.sendMessage(new TextComponentTranslation("questing.life.change.toomany", LIFE_HANDLER.getMaxLives()));
        		}
            
        		return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        	}
        	return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
	
	@Override
	public boolean isEnchantable(ItemStack stack){
		return false;
	}
	
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
    	stack.getMaxStackSize();
        return stack.getMetadata() == 0;
    }
    
    @Override
    @Deprecated
    public int getItemStackLimit()
    {
        return 4;
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        if(stack.getMetadata() == 3){
        	return 4;
        }
        if(stack.getMetadata() == 2){
        	return 2;
        }
        if(stack.getMetadata() > 0){
        	return 1;
        }
        return 64;
    }
}
