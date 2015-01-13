package fr.eyzox.forgefaction.command;

import java.util.Arrays;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public abstract class ForgeFactionCommand extends CommandBase {

	@Override
	public final String getCommandName() {
		return "ff-"+getName();
	}

	

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		String[] minExpectedArgs = getArgsNames();
		if(minExpectedArgs == null || args.length >= minExpectedArgs.length) {
			process(sender, args);
		}else {
			sender.addChatMessage(new ChatComponentText(this.getCommandUsage(sender)));
		}
		
	}



	@Override
	public final String getCommandUsage(ICommandSender p_71518_1_) {
		StringBuilder sb = new StringBuilder();
		sb.append("/"+getCommandName()+" ");
		
		String[] args = getArgsNames();
		if(args != null)
		for(String s : args) {
			sb.append("<"+s+"> ");
		}
		
		args = getFacultativeArgsNames();
		if(args != null) {
			for(String s : args) {
				sb.append("["+s+"] ");
			}
		}
		String s = getDescription();
		if(s != null) sb.append(": "+s);
		
		return sb.toString();
	}

	public abstract String getName();
	public abstract String[] getArgsNames();
	public abstract String[] getFacultativeArgsNames();
	public abstract String getDescription();
	public abstract void process(ICommandSender sender, String[] args);

}
