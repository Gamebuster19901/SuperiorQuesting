package com.gamebuster19901.superiorquesting.common.command;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.GuiHandler;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.NBTDebugger;
import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;
import com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler;
import com.gamebuster19901.superiorquesting.common.questing.Page;
import com.gamebuster19901.superiorquesting.common.questing.Quest;
import com.gamebuster19901.superiorquesting.common.questing.reward.ItemReward;
import com.gamebuster19901.superiorquesting.common.shape.Square;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandQuest extends CommandBase implements ICommand, Debuggable, NBTDebugger{

	@Override
	public int getRequiredPermissionLevel(){
		return 0;
	}
	
	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getName() {
		return "quest";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/quest <edit|book>";
	}
	
	@Override
	public List<String> getAliases() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("quests");
		return list;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		GlobalQuestHandler questhandler = Main.proxy.getGlobalQuestHandler();
		if (args.length == 0){
			if(sender instanceof EntityPlayerMP){
				EntityPlayerMP p = (EntityPlayerMP)sender;
				int questsCompleted = questhandler.getCompletedQuests(p).size();
				int totalQuests = questhandler.getAllQuests().size();
				p.sendMessage(new TextComponentString("Quests completed: " + questsCompleted + "/" + totalQuests ));
				return;
			}
		}
		if(!sender.canUseCommand(2, "give")){
			throw new CommandException("commands.generic.permission");
		}
		
		if(args.length == 1) {
			if(args[0].equals("book")) {
				if(sender instanceof EntityPlayerMP) {
					EntityPlayerMP p = (EntityPlayerMP)sender;
					p.addItemStackToInventory(new ItemStack(ItemQuestBook.ITEM, 1, 1));
					return;
				}
			}
			if(args[0].equals("edit")) {
				if(sender instanceof EntityPlayer) {
					EntityPlayerMP p = (EntityPlayerMP)sender;
					((EntityPlayerMP)sender).openGui(Main.getInstance(), GuiHandler.QUEST_BOOK, p.getEntityWorld(), (int)p.posX, (int)p.posY, (int)p.posZ);
					return;
				}
			}
		}
		if(args[0].equals("add") && Debuggable.debug) {
			if(args[1].equals("page")) {
				new Page("Test");
				return;
			}
			if(args[1].equals("lorem")) {
				new Page("Lorem ipsum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
				return;
			}
			if(args[1].equals("quest")) {
				Quest q = new Quest("Test", "A test quest.", new Page("Test page").getUUID(), 0, 0, Square.class, (byte)1);
				if(args.length > 2) {
					for(int i = 2; i < args.length; i++) {
						if(args[i].equals("task")) {
							throw new AssertionError();
						}
						else if (args[i].equals("reward")) {
							if(sender instanceof EntityPlayer) {
								new ItemReward(q, new ItemStack(Items.DIAMOND, 2));
							}
						}
					}
				}
				return;
			}
		}
		if(args[0].equals("clear") && Debuggable.debug) {
			Main.proxy.getGlobalQuestHandler().clean(true);
			return;
		}
		if(args[0].equals("list") && Debuggable.debug) {
			for(Quest q : Main.proxy.getGlobalQuestHandler().getAllQuests()) {
				String fullMessage = getFullNBTString(q.serializeNBT(), 1);
				fullMessage = fullMessage.replaceAll('\t' + "", "  ");
				String[] lines = fullMessage.split("\\n");
				for(String line : lines) {
					sender.sendMessage(new TextComponentString(line));
				}
			}
			for(Page p : Main.proxy.getGlobalQuestHandler().getAllPages()) {
				sender.sendMessage(new TextComponentString(p.order + " " + p.getUUID()));
			}
			return;
		}
		throw new WrongUsageException(MODID + ".commands.quests.usage");
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if (sender instanceof EntityPlayer){
			debug(((EntityPlayer)sender).getName());
			for(String s : Main.proxy.getServer().getPlayerList().getOppedPlayerNames()){
				if (((EntityPlayer)sender).getName().equals(s)){
					return true;
				}
			}
			debug(Main.proxy.getServer().getServerOwner());
			if (Main.proxy.getServer().getServerOwner() != null && Main.proxy.getServer().getServerOwner().equals(((EntityPlayer)sender).getName())){
				return true;
			}
		}
		else if (sender instanceof CommandBlockBaseLogic || sender instanceof MinecraftServer || sender instanceof RConConsoleSource){
			return true;
		}
		return false;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		if(args.length == 1) {
			return getListOfStringsMatchingLastWord(args, new String[] {"book", "edit"});
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}
}
