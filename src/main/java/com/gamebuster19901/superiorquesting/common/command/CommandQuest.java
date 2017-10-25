package com.gamebuster19901.superiorquesting.common.command;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;
import com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler;
import com.gamebuster19901.superiorquesting.common.questing.Quest;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandQuest extends CommandBase implements ICommand, Debuggable{

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
		return "/quest edit";
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
			if(args[0].equals("edit")) {
				if(sender instanceof EntityPlayerMP) {
					EntityPlayerMP p = (EntityPlayerMP)sender;
					p.addItemStackToInventory(new ItemStack(ItemQuestBook.ITEM, 1, 1));
					return;
				}
			}
			if(args[0].equals("add")) {
				new Quest(server, "Test", "test", 0, 0, 0, (byte)0);
				for(String name : server.getPlayerList().getOnlinePlayerNames()) {
					EntityPlayer p = server.getPlayerList().getPlayerByUsername(name);
				}
				return;
			}
		}
		
		if(args.length == 2) {
			if(args[0].equals("edit")) {
				if(questhandler.getPlayerFromUsername(args[1]) != null) {
					
				}
				else if(questhandler.getPlayerEntityFromUUID(UUID.fromString(args[1])) != null) {
					
				}
				else if(questhandler.getNbtOfPlayerUsingUUID(UUID.fromString(args[1])) != null) {
					
				}
				throw new PlayerNotFoundException("commands.generic.player.notFound", new Object[] {args[1]});
			}
		}
		
		throw new WrongUsageException(MODID + ".commands.lives.usage");
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
