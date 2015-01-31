package fr.eyzox.forgefaction.command;

import java.util.HashSet;
import java.util.Set;

import fr.eyzox.forgefaction.ForgeFactionMod;
import fr.eyzox.forgefaction.event.interract.ClaimInterract;
import fr.eyzox.forgefaction.network.TerritoryChunkPacket;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;
import fr.eyzox.forgefaction.territory.quarter.HeadQuarter;
import fr.eyzox.forgefaction.territory.quarter.QuarterBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
		
		if(playerProperties.isViewTerritory()) {
			playerProperties.setViewTerritory(false);
		}
		
		if(playerProperties.getFaction() == null) {
			sender.addChatMessage(new ChatComponentText("You must be in a faction to view our territory"));
			return;
		}
		
		Set<ForgeFactionChunk> chunks = new HashSet<ForgeFactionChunk>();
		for(HeadQuarter hq : playerProperties.getFaction().getHeadquarters()) {
			for(ForgeFactionChunk chunk : hq.getAllChunks()) {
				chunks.add(chunk);
			}
			
			for(QuarterBase quarter : hq.getChilds()) {
				do {
					for(ForgeFactionChunk chunk : quarter.getAllChunks()) {
						chunks.add(chunk);
					}
					quarter = quarter.getChild();
				}while(quarter != null);
			}
		}
		
		TerritoryChunkPacket paquet = new TerritoryChunkPacket(chunks, null);
		ForgeFactionMod.getInstance().getChannel().sendTo(paquet, (EntityPlayerMP) sender);
	}

}
