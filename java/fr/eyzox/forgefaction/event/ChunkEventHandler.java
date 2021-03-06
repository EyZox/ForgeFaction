package fr.eyzox.forgefaction.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.ChunkEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgefaction.ForgeFactionMod;
import fr.eyzox.forgefaction.data.ForgeFactionData;
import fr.eyzox.forgefaction.faction.Faction;
import fr.eyzox.forgefaction.territory.AbstractQuarter;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;
import fr.eyzox.forgefaction.territory.IQuarter;
import fr.eyzox.forgefaction.territory.TerritoryIndex;
import fr.eyzox.forgefaction.territory.quarter.Headquarter;
import fr.eyzox.forgefaction.territory.quarter.QuarterBase;

public class ChunkEventHandler implements IEvent {

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event) {
		if(!event.world.isRemote && MinecraftServer.getServer().worldServers.length > 0) {
			for(Faction faction : ForgeFactionData.getData().getFactions().getFactions()) {
				for(Headquarter hq : faction.getHeadquarters()) {
					if(hq.contains(event.getChunk().worldObj.provider.dimensionId, event.getChunk().xPosition, event.getChunk().zPosition)) {
						TerritoryIndex.getIndex().add(hq, new ForgeFactionChunk(event.getChunk()));
						return;
					}
					for(QuarterBase quarter : hq.getChilds()) {
						if(quarter.contains(event.getChunk().worldObj.provider.dimensionId, event.getChunk().xPosition, event.getChunk().zPosition)) {
							TerritoryIndex.getIndex().add(quarter, new ForgeFactionChunk(event.getChunk()));
							return;
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload event) {
		if(!event.world.isRemote) {
			TerritoryIndex.getIndex().remove(new ForgeFactionChunk(event.getChunk()));
		}
	}
	
	@SubscribeEvent
	public void onEntityEnteringChunk(EntityEvent.EnteringChunk event) {
		if(!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
			TerritoryIndex index = TerritoryIndex.getIndex();
			IQuarter oldAbstractQuarter = index.getIQuarter(new ForgeFactionChunk(event.entity.worldObj.provider.dimensionId, event.oldChunkX, event.oldChunkZ));
			IQuarter newAbstractQuarter = index.getIQuarter(new ForgeFactionChunk(event.entity.worldObj.provider.dimensionId, event.newChunkX, event.newChunkZ));
			
			if(oldAbstractQuarter != newAbstractQuarter) {
				EntityPlayer player = (EntityPlayer) event.entity;
				
				if(oldAbstractQuarter != null) {
					player.addChatComponentMessage(new ChatComponentText("You left "+oldAbstractQuarter.getFaction().getName()+" "+oldAbstractQuarter.getName()));
				}
				
				if(newAbstractQuarter != null) {
					player.addChatComponentMessage(new ChatComponentText("You enter to "+newAbstractQuarter.getFaction().getName()+" "+newAbstractQuarter.getName()));
				}
			}
		}
	}

}
