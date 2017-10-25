package com.gamebuster19901.superiorquesting.common.questing.reward;

import java.util.ArrayList;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.questing.exception.DuplicateKeyException;

public final class RewardType {
	public Class<? extends Reward> reward;
	public String name;
	public String description;
	
	private RewardType(Class<? extends Reward> rew, String nam, String desc) {
		this.reward = rew;
		this.name = nam;
		this.description = desc;
	}
	
	private static final ArrayList<RewardType> rewardTypes = new ArrayList<RewardType>();
	
	/**
	 * 
	 * @param reward the reward class to register
	 * @param image the image to use when selecting a reward type in the quest editor
	 * @param name the name of the reward
	 * @param description the description of the reward
	 */
	public static final void registerRewardType(Class<? extends Reward> reward, String name, String description) {
		for(RewardType r : rewardTypes) {
			if(r.reward.equals(reward)) {
				throw new DuplicateKeyException("Cannot register a reward type more than once: " + reward.getName());
			}
			if(r.reward.equals(Reward.class)) {
				throw new IllegalArgumentException("YOU'RE AN IDIOT, DON'T DO THAT");
			}
		}
		rewardTypes.add(new RewardType(reward, name, description));
		Main.LOGGER.info("Registered new reward type: " + name);
	}
	
	/**
	 * returns the reward type of the reward if it is registered, null if it isn't
	 * @param reward1 the reward to get the reward type of
	 */
	public static final RewardType getRewardType(Reward reward1) {
		for(RewardType reward2 : rewardTypes) {
			if(reward1.getClass() == reward2.reward) {
				return reward2;
			}
		}
		return null;
	}
}
