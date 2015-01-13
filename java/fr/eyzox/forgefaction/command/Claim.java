package fr.eyzox.forgefaction.command;

import fr.eyzox.forgefaction.event.interract.ClaimInterract;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class Claim extends ForgeFactionCommand {

	@Override
	public String getName() {
		return "claim";
	}

	@Override
	public String[] getArgsNames() {
		return null;
	}

	@Override
	public String[] getFacultativeArgsNames() {
		return null;
	}

	@Override
	public String getDescription() {
		return "claims a new territory for your faction";
	}

	@Override
	public void process(ICommandSender sender, String[] args) {
		if(!(sender instanceof EntityPlayer)) {
			sender.addChatMessage(new ChatComponentText("Only players can claim territory"));
			return;
		}
		
		ForgeFactionPlayerProperties playerProperties = ForgeFactionPlayerProperties.get((EntityPlayer) sender);
		if(playerProperties.getFaction() == null) {
			sender.addChatMessage(new ChatComponentText("You must be in a faction to claim a territory"));
			return;
		}
		
		//TODO verifier permission
		
		playerProperties.setInterractStrategy(new ClaimInterract());
	}

}
