package com.gamebuster19901.superiorquesting.common.questing.integrate;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.Unique;
import com.gamebuster19901.superiorquesting.common.UpdatableSerializable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface Hideable extends UpdatableSerializable, Unique{
	public String HIDDEN = "HIDDEN";
	
	public NBTTagCompound getDefaultPlayerNBT();
	
	/**
	 * Checks if this is marked as hidden for the player
	 * @param p the player to check
	 * @return true if this is hidden for the player, false otherwise
	 */
	public boolean isHidden(EntityPlayer p);
	
	/**
	 * Checks if this is marked as hidden for the player
	 * @param p the uuid of the player to check
	 * @return true if this is hidden for the player, false otherwise
	 */
	public boolean isHidden(UUID p);
	
	/**
	 * Hides this from the player.
	 * @param p the player to hide this from
	 */
	public void hide(EntityPlayer p);
	
	/**
	 * Hides this from the player.
	 * @param p the uuid of the player to hide this from
	 */
	public void hide(UUID p);
	
	/**
	 * Unides this from the player.
	 * @param p the player to hide this from
	 */
	public void unhide(EntityPlayer p);
	
	/**
	 * Unides this from the player.
	 * @param p the uuid of the player to hide this from
	 */
	public void unhide(UUID p);
	
	/**
	 * @return true if this is hidden by default, false otherwise
	 */
	public boolean isHiddenByDefault();
}
