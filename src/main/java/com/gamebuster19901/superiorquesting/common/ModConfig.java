package com.gamebuster19901.superiorquesting.common;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = MODID)
public class ModConfig{
	public static interface NonConfigFields{
		public int VERSION = 1;
	}
	
	/*
	 * Config begin
	 */

	@Config.Comment("The the rules for questing, such as starting/max lives")
	public static final QuestingRules RULES = new QuestingRules();
	
	public static class QuestingRules{
		@Config.Name("Starting Lives")
		@Config.RangeDouble(min = 1d, max = Double.POSITIVE_INFINITY)
		@Config.Comment("Decimal numbers will be rounded down, use integers only. If larger than the maximum life counter, then the starting life count will be the maximum count. If less than 1, it will be 1.")
		public double startingLives = Double.POSITIVE_INFINITY;
		@Config.Name("Maximum Lives")
		@Config.RangeDouble(min = 1d, max = Double.POSITIVE_INFINITY)
		@Config.Comment("Decimal numbers will be rounded down, use integers only")
		public double maxLives = Double.POSITIVE_INFINITY;
		@Config.Name("Start With Book")
		@Config.Comment("If the player starts the world with a quest book")
		public boolean hasBook = true;
		@Config.Name("Delete World On Death")
		@Config.Comment("If true, the world is deleted on death or the player is banned, if false, the player is put into spectator mode")
		public boolean delOnDeath = false;
	}
	
	/*
	 * Config end
	 */
	
	@Mod.EventBusSubscriber(modid = MODID)
	private static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(MODID)) {
				ConfigManager.sync(MODID, Config.Type.INSTANCE);
				LifeHandler.onConfigFinishChanged();
			}
		}
	}
}
