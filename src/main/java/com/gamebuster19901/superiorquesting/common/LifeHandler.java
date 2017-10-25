package com.gamebuster19901.superiorquesting.common;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import java.util.LinkedHashSet;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.packet.PacketFinalDeath;
import com.gamebuster19901.superiorquesting.common.packet.PacketLifeTotal;
import com.gamebuster19901.superiorquesting.common.packet.PacketMaxLife;
import com.gamebuster19901.superiorquesting.proxy.ClientProxy;
import com.gamebuster19901.superiorquesting.proxy.ServerProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class LifeHandler extends MultiplayerHandler implements Assertable, Debuggable{
	private static final String LIFE_KEY = MODID + ":lives"; 
	private static double maxLives = ModConfig.RULES.maxLives;
	private static double startingLives = ModConfig.RULES.startingLives;
	/**
	 * Adds one life to the player, if the life total would be less than 1, it is set to 1 instead, it if is greater than the max life count, it is unchanged.
	 * 
	 * @param p the player to add a life to
	 * @return true if the life was added, false otherwise
	 */
	public boolean addLife(EntityPlayer p){
		double newLives = getLives(p) + 1;
		double maxLives = getMaxLives();
		if (newLives < 1){
			Main.LOGGER.error("Attempted to set lives to be less than 1", new AssertionError("Attempted to set lives to be less than 1"));
			setLives(p, 1d, true);
			return true;
		}
		if (newLives <= maxLives){
			setLives(p, newLives, false);
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
	public boolean removeLife(EntityPlayer p){
		double newLives = getLives(p) - 1;
		double maxLives = getMaxLives();
		if (newLives < 0){
			setLives(p, 0d, true);
			return false;
		}
		if (newLives <= maxLives){
			setLives(p,newLives, true);
			return true;
		}
		setLives(p, getMaxLives(), true);
		return true;
	}
	
	public boolean addLives(EntityPlayer p, Double amount){
		return setLives(p,getLives(p) + Math.floor(amount), true);
	}
	
	/**
	 * Sets the player's life counter, unless the count would be less than 0 or greater than the maximum life count
	 * 
	 * @param p the player's life count to set
	 * @param amount the amount of lives the player will have
	 * @return true if the life count was set, false otherwise
	 */
	public boolean setLives(EntityPlayer p, Double amount, boolean sendMessage){
		if (amount < 0d || amount > getMaxLives()){
			return false;
		}
		getPersistantTag(p).setDouble(LIFE_KEY, amount);
		if (amount < 1d){
			p.setGameType(GameType.SPECTATOR);
		}
		if(sendMessage){
			messageLives(p);
		}
		Main.proxy.NETWORK.sendTo(new PacketLifeTotal(getLives(p)), (EntityPlayerMP) p);
		return true;
	}
	
	/**
	 * Resets the player's life counter to its default value
	 * @param p the player's life to reset
	 * @throws AssertionError if the default life count is less than 1 and the maximum life count is less than 1
	 */
	public void resetLives(EntityPlayer p){
		if (getStartingLives() > getMaxLives()){
			Assert(getMaxLives() >= 1d, "Starting life total and max life total out of bounds, nowhere to fall back to", new IllegalStateException(new IndexOutOfBoundsException("Starting life total and max life total < 1, nowhere to fall back to")));
			setLives(p, getMaxLives(), true);
		}
		else{
			Assert(getStartingLives() >= 1d, "Starting life total and max life total out of bounds, nowhere to fall back to", new IllegalStateException(new IndexOutOfBoundsException("Starting life total and max life total < 1, nowhere to fall back to")));
			setLives(p, getStartingLives(), true);
		}
	}
	
	/**
	 * Gets the remaining lives a player has, rounded to floor
	 * @param p the player whose lives to get
	 * @return remaining lives the player has, rounded to floor
	 */
	public double getLives(EntityPlayer p){
		NBTTagCompound nbt = getPersistantTag(p);
		return Math.floor(nbt.getDouble(LIFE_KEY));
	}
	
	/**
	 * @return the maximum life total, rounded to floor
	 */
	public double getMaxLives(){
		if(maxLives != ModConfig.RULES.maxLives && !((ClientProxy)Main.proxy).isServerRemote()) {
			maxLives = ModConfig.RULES.maxLives;
		}
		return Math.floor(maxLives);
	}
	
	/**
	 * @return the starting life total, rounded to floor
	 */
	public double getStartingLives(){
		if(startingLives != ModConfig.RULES.startingLives && !((ClientProxy)Main.proxy).isServerRemote()) {
			startingLives = ModConfig.RULES.startingLives;
		}
		return Math.floor(ModConfig.RULES.startingLives);
	}
	
	/**
	 * Used to check if the player has life NBT
	 * @param p the player to check
	 * @return true if the player has life NBT, false otherwise
	 */
	public boolean hasLifeNBT(EntityPlayer p){
		return getPersistantTag(p).hasKey(LIFE_KEY);
	}
	
	/**
	 * If any player's life count is out of bounds, set it to the closest in-bounds number
	 * @return a LinkedHashSet containing all player's whose life count was changed.
	 */
	public LinkedHashSet<EntityPlayer> assertValidLives(){
		LinkedHashSet<EntityPlayer> ret = new LinkedHashSet<EntityPlayer>();
		if (FMLCommonHandler.instance().getMinecraftServerInstance() != null){
			for(EntityPlayer p :FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
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
	public boolean assertValidLives(EntityPlayer p){
		if(hasLifeNBT(p)){
			double lives = getLives(p);
			if(lives < 0d){
				setLives(p, 1d, true);
				return true;
			}
			else if (lives > getMaxLives()){
				setLives(p, getMaxLives(), true);
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
	 * Messages a player their life total
	 * 
	 * @param p the player to message
	 */
	public void messageLives(EntityPlayer p){
		Double lives = getLives(p);
		if(lives < 1d){
			p.sendMessage(new TextComponentString("You have lost all of your lives!"));
		}
		else if (!lives.isInfinite()){
			p.sendMessage(new TextComponentString("You have " + (long)getLives(p) + " lives remaining."));
		}
		else{
			p.sendMessage(new TextComponentString("You have " + (char)0x221E + " lives remaining."));
		}
	}
	
	/**
	 * Messages a player another player's life total
	 * 
	 * @param req the player to message
	 * @param p the player's life total to message
	 */
	public void messageLives(EntityPlayer req, EntityPlayer p){
		Double lives = getLives(p);
		if(lives < 1d){
			req.sendMessage(new TextComponentString(p + " has lost all of their lives!"));
		}
		else if (!lives.isInfinite()){
			req.sendMessage(new TextComponentString(p + " has " + (long)getLives(p) + " lives remaining."));
		}
		else{
			req.sendMessage(new TextComponentString(p + " has " + (char)0x221E + " lives remaining."));
		}
	}
	
	@Override
	public void playerLoggedIn(PlayerLoggedInEvent e){
		EntityPlayer p = (EntityPlayer)e.player;
		if(!assertValidLives(p)){
			messageLives(p);
		}
		if(e.player.world.isRemote && Main.proxy instanceof ServerProxy) {
			Main.proxy.NETWORK.sendTo(new PacketMaxLife(getMaxLives()), (EntityPlayerMP) e.player);
		}
		Main.proxy.NETWORK.sendTo(new PacketLifeTotal(getLives(e.player)), (EntityPlayerMP) e.player);
	}
	
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent e){
		if (e.getEntity() instanceof EntityPlayer){
			EntityPlayer p = (EntityPlayer) e.getEntity();
			if(removeLife(p) && p.isSpectator() && p.posY >= 0){
				p.setHealth(0.01f);
				if(p instanceof EntityPlayerMP) {
					Main.proxy.NETWORK.sendTo(new PacketFinalDeath(), (EntityPlayerMP) p);
				}
				debug("test1");
			}
		}
		debug("test2");
	}
	
	/**
	 * Used to make sure that lives are still within bounds after the config has changed
	 */
	@Override
	protected void onConfigFinishChanged() {
		boolean remote = (Main.proxy instanceof ClientProxy && Minecraft.getMinecraft().player.world.isRemote) || Main.proxy instanceof ServerProxy;
		if(remote) {
			double prevMaxLives = getMaxLives();
			double prevStartingLives = getStartingLives();
			assertValidLives();
			double newMaxLives = getMaxLives();
			double newStartingLives = getStartingLives();
			if(prevMaxLives != newMaxLives) {
				Main.proxy.NETWORK.sendToAll(new PacketMaxLife(newMaxLives));
			}
		}
		else {
			assertValidLives();
		}
	}

	public void setMaxLives(double amount) {
		if(((ClientProxy)Main.proxy).isServerRemote()) {
			maxLives = amount;
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Max lives set to " + amount));
		}
		else {
			throw new IllegalStateException("A non integerated server cannot set the max life total except via a config change!");
		}
	}
	
	public void setStartingLives(double amount) {
		if(((ClientProxy)Main.proxy).isServerRemote()) {
			startingLives = amount;
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Starting lives set to " + amount));
		}
		else {
			throw new IllegalStateException("A non integerated server cannot set the starting life total except via a config change!");
		}
	}
}
