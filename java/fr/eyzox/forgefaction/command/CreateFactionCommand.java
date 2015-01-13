package fr.eyzox.forgefaction.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import fr.eyzox.forgefaction.ForgeFactionData;
import fr.eyzox.forgefaction.faction.Faction;
import fr.eyzox.forgefaction.faction.Factions;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;

public class CreateFactionCommand extends ForgeFactionCommand {

	@Override
	public void process(ICommandSender sender, String[] args) {
		if(args[0].length() >= 4 && args[0].length() < 20) {
			if(sender instanceof EntityPlayer) {
				ForgeFactionPlayerProperties p = ForgeFactionPlayerProperties.get((EntityPlayer) sender);
				if(p.getFaction() == null) {
					Factions factions = ForgeFactionData.getData().getFactions();
					if(!factions.contains(args[0])) {
						Faction team = new Faction(args[0]);
						factions.add(team);
						team.addPlayer((EntityPlayer) sender);
						MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(((EntityPlayer)sender).getDisplayName()+" has created a new faction : "+args[0]));
					}else {
						sender.addChatMessage(new ChatComponentText("name already taken"));
					}
				}else {
					sender.addChatMessage(new ChatComponentText("You are already in a faction"));
				}
			}else {
				sender.addChatMessage(new ChatComponentText("Only players can create a faction"));
			}
		}else {
			sender.addChatMessage(new ChatComponentText("Faction name's length must be beetween 4-20 characters"));
		}
	}

	@Override
	public String getName() {
		return "create";
	}

	@Override
	public String[] getArgsNames() {
		return new String[] {"faction name"};
	}

	@Override
	public String[] getFacultativeArgsNames() {
		return null;
	}

	@Override
	public String getDescription() {
		return "Creates a faction";
	}

}
