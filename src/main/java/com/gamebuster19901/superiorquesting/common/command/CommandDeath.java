package com.gamebuster19901.superiorquesting.common.command;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.packet.life.PacketFinalDeath;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandDeath extends CommandBase implements ICommand, Debuggable{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "packet";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "packet";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayerMP) {
			Main.proxy.NETWORK.sendTo(new PacketFinalDeath(), (EntityPlayerMP) sender);
			debug("sent packet");
		}
		else {
			throw new CommandException("You must be a player to execute this command!");
		}
	}
}
