package fr.eyzox.forgefaction.command;

import fr.eyzox.forgefaction.faction.Faction;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class KickPlayerCommand extends ForgeFactionCommand {

	@Override
	public String getName() {
		return "kick";
	}

	@Override
	public String[] getArgsNames() {
		return new String[]{"player name"};
	}

	@Override
	public String[] getFacultativeArgsNames() {
		return new String[]{"reason"};
	}

	@Override
	public String getDescription() {
		return "kick a player from your faction";
	}

	@Override
	public void process(ICommandSender sender, String[] args) {
		if(!(sender instanceof EntityPlayer)) {
			sender.addChatMessage(new ChatComponentText("Only players can kick others"));
			return;
		}
		
		EntityPlayer player = (EntityPlayer) sender;
		Faction faction = ForgeFactionPlayerProperties.get(player).getFaction();
		if(faction == null) {
			sender.addChatMessage(new ChatComponentText("You are not in a faction"));
			return;
		}
		
		//TODO : Verifier les permissions
		
		EntityPlayer playerToKick = MinecraftServer.getServer().getConfigurationManager().func_152612_a(args[0]);
		boolean removed;
		if(playerToKick == null) {
			removed = faction.removePlayer(args[0]);
		}else {
			removed = faction.removePlayer(playerToKick);
			if(removed) playerToKick.addChatComponentMessage(new ChatComponentText("You've been kicked from "+faction.getName()+" by "+player.getDisplayName()+(args.length<2?"":" reason: "+args[1])));
		}
		
		if(removed) sender.addChatMessage(new ChatComponentText("You have kicked "+playerToKick.getDisplayName()));
		else sender.addChatMessage(new ChatComponentText(playerToKick.getDisplayName()+" is not in your faction"));
	}

}
