package com.gamebuster19901.superiorquesting.common.questing;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class Quest implements Rewardable, Assignment{
	private ArrayList<Reward> rewards;
	private ArrayList<Task> tasks;

	@Override
	public boolean canAward(EntityPlayer p) {
		if(this.isFinished(p)) {
			return false;
		}
		for(Reward r : rewards) {
			if (!r.canAward(p)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void award(EntityPlayer p) {
		for(Reward r : rewards) {
			r.award(p);
		}
		//set reward as collected
	}

	@Override
	public boolean isFinished(EntityPlayer p) {
		
	}
	
	@Override
	public void finish(EntityPlayer p) {
		
	}
	
	public boolean rewardCollected(EntityPlayer p) {

	}
	
	@Override
	public String getTitle() {

	}

	@Override
	public String getDescription() {

	}
	
	@SubscribeEvent
	public void worldTickEvent(WorldTickEvent e) {
		if(e.phase.equals(Phase.END)) {
			if(e.world.isRemote) {
				for(EntityPlayerMP p : e.world.getPlayers(EntityPlayerMP.class, null)) {
					if(canAward(p)) {
						award(p);
					}
				}
			}
		}
	}
}
