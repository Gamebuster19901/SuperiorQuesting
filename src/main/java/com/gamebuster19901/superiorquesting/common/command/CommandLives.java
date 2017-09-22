package com.gamebuster19901.superiorquesting.common.command;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import java.util.Collections;
import java.util.List;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Debuggable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;

public final class CommandLives extends CommandBase implements ICommand, Debuggable{

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
		// TODO Auto-generated method stub
		return "lives";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "lives [<set|add|remove> <amount> [<player>]]";
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Collections.EMPTY_LIST;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0){
			if(sender instanceof EntityPlayerMP){
				Main.proxy.getLifeHandler().messageLives((EntityPlayerMP)sender);
				return;
			}
		}
		if(!sender.canUseCommand(2, "give")){
			throw new CommandException("commands.generic.permission");
		}
		if (args.length == 1){
			if(sender instanceof EntityPlayerMP){
				for(String s : server.getOnlinePlayerNames()){
					if (s.equals(args[2]) || getPlayer(server, sender, s).getUniqueID().toString().equals(args[2])){
						Main.proxy.getLifeHandler().messageLives((EntityPlayerMP)sender, getPlayer(server, sender, args[2]));
						return;
					}
				}
			}
		}
		if (args.length == 2){
			args = new String[]{args[0], args[1], sender.getName()};
		}
		if (args.length == 3){
			for(String s : server.getOnlinePlayerNames()){
				if (s.equals(args[2]) || getPlayer(server, sender, s).getUniqueID().toString().equals(args[2])){
					try{
						Double lives = Double.parseDouble(args[1]);
						if(lives.isNaN()){
							throw new NumberInvalidException("commands.generic.num.invalid", new Object[]{args[1]});
						}
						EntityPlayerMP p = getPlayer(server, sender, args[2]);
						if(args[0].equals("add")){
							if (Main.proxy.getLifeHandler().addLives(p, lives)){
								p.sendMessage(new TextComponentTranslation(MODID + ".life.change.unnatural"));
								sender.sendMessage(new TextComponentTranslation(MODID + ".commands.lives.success"));
								return;
							}
							throw new NumberInvalidException(MODID + ".commands.lives.outofbounds", new Object[]{args[1]});
						}
						else if(args[0].equals("remove")){
							if (Main.proxy.getLifeHandler().addLives(p, lives * -1)){
								p.sendMessage(new TextComponentTranslation(MODID + ".life.change.unnatural"));
								sender.sendMessage(new TextComponentTranslation(MODID + ".commands.lives.success"));
								return;
							}
							throw new NumberInvalidException(MODID + ".commands.lives.outofbounds", new Object[]{args[1]});
						}
						else if(args[0].equals("set")){
							if(Main.proxy.getLifeHandler().setLives(p, lives, true)){
								p.sendMessage(new TextComponentTranslation(MODID + ".life.change.unnatural"));
								sender.sendMessage(new TextComponentTranslation(MODID + ".commands.lives.success"));
								return;
							}
							throw new NumberInvalidException(MODID + ".commands.lives.outofbounds", new Object[]{args[1]});
						}
						throw new WrongUsageException(MODID + ".commands.lives.usage");
					}
					catch(NumberFormatException e){
						throw new NumberInvalidException("commands.generic.num.invalid", new Object[]{args[1]});
					}
				}
			}
			throw new PlayerNotFoundException("commands.generic.player.notFound", new Object[] {args[2]});
		}
		throw new WrongUsageException(MODID + ".commands.lives.usage");
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
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		if(args.length == 1){
			return getListOfStringsMatchingLastWord(args, new String[] {"add", "remove", "set"});
		}
		if(args.length == 3){
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return true;
	}

}
