package fr.eyzox.forgefaction.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;

public class InvitePlayer extends ForgeFactionCommand {

	@Override
	public void process(ICommandSender sender, String[] args) {
		
		if(!(sender instanceof EntityPlayer)) {
			sender.addChatMessage(new ChatComponentText("Only players can invite others"));
			return;
		}
		
		if(!hasTeam((EntityPlayer) sender)) {
			sender.addChatMessage(new ChatComponentText("You must be in a faction to invite someone"));
			return;
		}
		
		//TODO : Verifier les permissions
		
		EntityPlayer playerToInvite = MinecraftServer.getServer().getConfigurationManager().func_152612_a(args[0]);	
		if(playerToInvite == null) {
			sender.addChatMessage(new ChatComponentText("Player "+args[0]+" unexist or is offline"));
			return;
		}
		
		if(hasTeam(playerToInvite)) {
			sender.addChatMessage(new ChatComponentText("Player "+args[0]+" is already in a faction"));
			return;
		}
		
		sender.addChatMessage(new ChatComponentText("Player "+args[0]+" was invited to join your faction"));
		//TODO : Envoyer l'invitation

	}
	/*
	private EntityPlayer getPlayer(String name) {
		EntityPlayer player = null;
		for(WorldServer w : MinecraftServer.getServer().worldServers) {
			if((player = w.getPlayerEntityByName(name)) != null) break;
		}
		return player;
	}
	*/
	
	private boolean hasTeam(EntityPlayer player) {
		return ForgeFactionPlayerProperties.get(player).getFaction() != null;
	}

	@Override
	public String getName() {
		return "invite";
	}

	@Override
	public String[] getArgsNames() {
		return new String[]{"player name"};
	}

	@Override
	public String[] getFacultativeArgsNames() {
		return null;
	}

	@Override
	public String getDescription() {
		return "invite a player to join your faction";
	}

}
