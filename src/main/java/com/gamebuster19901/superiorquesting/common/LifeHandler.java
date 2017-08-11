package com.gamebuster19901.superiorquesting.common;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import java.util.LinkedHashSet;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.proxy.ClientProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.server.FMLServerHandler;

public class LifeHandler {
	private static final String LIFE_KEY = MODID + ":lives"; 
	/**
	 * Adds one life to the player, if the life total would be less than 1, it is set to 1 instead, it if is greater than the max life count, it is unchanged.
	 * 
	 * @param p the player to add a life to
	 * @return true if the life was added, false otherwise
	 */
	public boolean addLife(EntityPlayerMP p){
		double newLives = getLives(p) + 1;
		double maxLives = getMaxLives();
		if (newLives < 1){
			Main.LOGGER.error("Attempted to set lives to be less than 1", new AssertionError("Attempted to set lives to be less than 1"));
			setLives(p, 1d);
			return true;
		}
		if (newLives <= maxLives){
			setLives(p,newLives);
			return true;
		}
		return false;
	}
	
	/**
	 * Removes 1 life from the player if possible, if the life total would be greater than the max life count, it is set to the max life count instead
	 * 
	 * @param p the player to add a life to
	 * @return true if a life can be taken, false otherwise
	 */
	public boolean removeLife(EntityPlayerMP p){
		double newLives = getLives(p) - 1;
		double maxLives = getMaxLives();
		if (newLives < 1){
			return false;
		}
		if (newLives <= maxLives){
			setLives(p,newLives);
			return true;
		}
		setLives(p, getMaxLives());
		return true;
	}
	
	/**
	 * Sets the player's life counter, unless the count would be less than 1 or greater than the maximum life count
	 * 
	 * @param p the player's life count to set
	 * @param amount the amount of lives the player will have
	 * @return true if the life count was set, false otherwise
	 */
	public boolean setLives(EntityPlayerMP p, Double amount){
		if (amount < 1d || amount > getMaxLives()){
			return false;
		}
		getPersistantTag(p).setDouble(LIFE_KEY, amount);
		return true;
	}
	
	/**
	 * Resets the player's life counter to its default value
	 * @param p the player's life to reset
	 * @throws AssertionError if the default life count is less than 1 and the maximum life count is less than 1
	 */
	public void resetLives(EntityPlayerMP p){
		if (getStartingLives() > getMaxLives()){
			if (getMaxLives() < 1){
				throw new AssertionError(new IllegalStateException(new IndexOutOfBoundsException("Starting life total and max life total < 1, nowhere to fall back to")));
			}
			setLives(p, getMaxLives());
		}
		else{
			if (getStartingLives() < 1){
				throw new AssertionError(new IllegalStateException(new IndexOutOfBoundsException("Starting life total and max life total < 1, nowhere to fall back to")));
			}
			setLives(p, getStartingLives());
		}
	}
	
	/**
	 * Gets the remaining lives a player has, rounded to floor
	 * @param p the player whose lives to get
	 * @return remaining lives the player has, rounded to floor
	 */
	public double getLives(EntityPlayerMP p){
		NBTTagCompound nbt = getPersistantTag(p);
		return Math.floor(nbt.getDouble(LIFE_KEY));
	}
	
	/**
	 * @return the maximum life total, rounded to floor
	 */
	public double getMaxLives(){
		return Math.floor(ModConfig.RULES.maxLives);
	}
	
	/**
	 * @return the starting life total, rounded to floor
	 */
	public double getStartingLives(){
		return Math.floor(ModConfig.RULES.startingLives);
	}
	
	/**
	 * Used to check if the player has life NBT
	 * @param p the player to check
	 * @return true if the player has life NBT, false otherwise
	 */
	public boolean hasLifeNBT(EntityPlayerMP p){
		return getPersistantTag(p).hasKey(LIFE_KEY);
	}
	
	/**
	 * If any player's life count is out of bounds, set it to the closest in-bounds number
	 * @return a LinkedHashSet containing all player's whose life count was changed.
	 */
	public LinkedHashSet<EntityPlayerMP> assertValidLives(){
		LinkedHashSet<EntityPlayerMP> ret = new LinkedHashSet<EntityPlayerMP>();
		if (FMLCommonHandler.instance().getMinecraftServerInstance() != null){
			for(EntityPlayerMP p :FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
				if(assertValidLives(p)){
					ret.add(p);
				}
			}
		}
		return ret;
	}
	
	/**
	 * If the player's life count is out of bounds, set it to the closest in-bounds number
	 * @param p The player to check
	 * @return true if the player's life count changed
	 */
	public boolean assertValidLives(EntityPlayerMP p){
		if(hasLifeNBT(p)){
			double lives = getLives(p);
			if(lives < 1d){
				setLives(p, 1d);
				return true;
			}
			else if (lives > getMaxLives()){
				setLives(p, getMaxLives());
				return true;
			}
			double x = getMaxLives();
			return false;
		}
		else{
			resetLives(p);
			return true;
		}
	}
	
	/**
	 * Gets the NBTTagCompound that persists with a player after death
	 * @param p the player whose tag to retrieve
	 * @return the NBTTagCompound that persists with a player after death
	 * @throws AssertionError if the tag doesn't exist
	 */
	private NBTTagCompound getPersistantTag(EntityPlayerMP p){
		NBTTagCompound entityData = p.getEntityData();
		NBTTagCompound persist;
		if (!hasPersistantTag(p)) {
			throw new AssertionError("No persistent tag found for player " + p.getName());
		} else {
		   return persist = entityData.getCompoundTag(p.PERSISTED_NBT_TAG);
		}
	}
	
	/**
	 * Checks if the NBTTagCompound that persists with a player after death exists
	 * @param p the player to check
	 * @return true if it exists, false otherwise
	 */
	private boolean hasPersistantTag(EntityPlayerMP p){
		return p.getEntityData().hasKey(p.PERSISTED_NBT_TAG);
	}
	
	@SubscribeEvent
	public void playerLoggedInEvent(PlayerLoggedInEvent e){
		EntityPlayerMP p = (EntityPlayerMP)e.player;
		if (!hasPersistantTag(p)){
			p.getEntityData().setTag(p.PERSISTED_NBT_TAG, new NBTTagCompound());
		}
		NBTTagCompound nbt = getPersistantTag(p);
		assertValidLives(p);
		Double lives = getLives(p);
		if (!lives.isInfinite()){
			p.sendMessage(new TextComponentString("You have " + (int)getLives(p) + " lives remaining."));
		}
		else{
			p.sendMessage(new TextComponentString("You have " + (char)0x221E + " lives remaining."));
		}
	}
	
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent e){
		if (e.getEntity() instanceof EntityPlayer){
			EntityPlayerMP p = (EntityPlayerMP)e.getEntity();
			if(!removeLife((EntityPlayerMP)e.getEntity())){
				p.setGameType(GameType.SPECTATOR);
				p.sendMessage(new TextComponentString("You have lost all of your lives!"));
			}
		
			else if (!((Double)getLives(p)).isInfinite()){
				p.sendMessage(new TextComponentString("You have " + (int)getLives(p) + " lives remaining"));
			}
			
			else{
				p.sendMessage(new TextComponentString("You have " + (char)0x221E + " lives remaining."));
			}
		}
	}
	
	/**
	 * Used to make sure that lives are still within bounds after the config has changed
	 */
	static void onConfigFinishChanged() {
		LifeHandler l = Main.proxy.getLifeHandler();
		for(EntityPlayerMP p : l.assertValidLives()){
			if (!((Double)l.getLives(p)).isInfinite()){
				p.sendMessage(new TextComponentString("You have " + (int)l.getLives(p) + " lives remaining"));
			}
			else{
				p.sendMessage(new TextComponentString("You have " + (char)0x221E + " lives remaining."));
			}
		}
	}
}
