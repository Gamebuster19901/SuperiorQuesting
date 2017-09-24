package com.gamebuster19901.superiorquesting.common.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gamebuster19901.superiorquesting.common.Debuggable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandQuest extends CommandBase implements ICommand, Debuggable{

	@Override
	public int getRequiredPermissionLevel(){
		return 0;
	}
	
	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "quest";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "";
	}
	
	@Override
	public List<String> getAliases() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("quests");
		return list;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if (sender instanceof EntityPlayer){
			debug(((EntityPlayer)sender).getName());
			for(String s : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayerNames()){
				if (((EntityPlayer)sender).getName().equals(s)){
					return true;
				}
			}
			debug(FMLCommonHandler.instance().getMinecraftServerInstance().getServerOwner());
			if (FMLCommonHandler.instance().getMinecraftServerInstance().getServerOwner().equals(((EntityPlayer)sender).getName())){
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
