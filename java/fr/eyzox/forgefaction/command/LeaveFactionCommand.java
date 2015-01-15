package fr.eyzox.forgefaction.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import fr.eyzox.forgefaction.data.ForgeFactionData;
import fr.eyzox.forgefaction.faction.Faction;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;

public class LeaveFactionCommand extends ForgeFactionCommand {

	@Override
	public void process(ICommandSender sender, String[] args) {
		if(!(sender instanceof EntityPlayer)) {
			sender.addChatMessage(new ChatComponentText("Only players can leave a faction"));
			return;
		}
		
		EntityPlayer player = (EntityPlayer) sender;
		Faction faction = ForgeFactionPlayerProperties.get(player).getFaction();
		if(faction == null) {
			sender.addChatMessage(new ChatComponentText("You must be in a faction before leave it"));
			return;
		}
		
		faction.removePlayer(player);
		sender.addChatMessage(new ChatComponentText("You left your faction"));
		if(faction.getNbPlayer() == 0) {
			ForgeFactionData.getData().getFactions().remove(faction);
			sender.addChatMessage(new ChatComponentText("As there was nobody left in your faction, it has been removed"));
		}
		
	}

	@Override
	public String getName() {
		return "leave";
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
		return "leave your faction";
	}

}
