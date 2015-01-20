package fr.eyzox.forgefaction.command;

import fr.eyzox.forgefaction.event.interract.ClaimInterract;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class ViewTerritoryCommand extends ForgeFactionCommand {

	@Override
	public String getName() {
		return "view";
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
		return "Highlight your faction's territory";
	}

	@Override
	public void process(ICommandSender sender, String[] args) {
		if(!(sender instanceof EntityPlayer)) {
			sender.addChatMessage(new ChatComponentText("Only players can view their territory"));
			return;
		}
		
		ForgeFactionPlayerProperties playerProperties = ForgeFactionPlayerProperties.get((EntityPlayer) sender);
		
		if(playerProperties.getFaction() == null) {
			sender.addChatMessage(new ChatComponentText("You must be in a faction to view our territory"));
			return;
		}

	}

}
