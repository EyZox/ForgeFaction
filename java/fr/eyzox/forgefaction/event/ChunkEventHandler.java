package fr.eyzox.forgefaction.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.ChunkEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgefaction.ForgeFactionData;
import fr.eyzox.forgefaction.faction.Faction;
import fr.eyzox.forgefaction.territory.TerritoryIndex;
import fr.eyzox.forgefaction.territory.quarter.AbstractQuarter;
import fr.eyzox.forgefaction.territory.quarter.HeadQuarter;
import fr.eyzox.forgefaction.territory.quarter.Quarter;

public class ChunkEventHandler implements IEvent {

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event) {
		if(!event.world.isRemote) {
			ForgeFactionData data = ForgeFactionData.getData();
			for(Faction faction : data.getFactions().getFactions()) {
				for(HeadQuarter hq : faction.getHeadquarters()) {
					if(hq.contains(event.getChunk())) {
						data.getIndex().add(hq, event.getChunk());
						return;
					}
					for(Quarter quarter : hq.getQuarters()) {
						if(quarter.contains(event.getChunk())) {
							data.getIndex().add(quarter, event.getChunk());
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
			ForgeFactionData.getData().getIndex().remove(event.getChunk());
		}
	}
	
	@SubscribeEvent
	public void onEntityEnteringChunk(EntityEvent.EnteringChunk event) {
		if(!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
			TerritoryIndex index = ForgeFactionData.getData().getIndex();
			AbstractQuarter oldAbstractQuarter = index.getAbstractQuarter(event.entity.worldObj.getChunkFromChunkCoords(event.oldChunkX, event.oldChunkZ));
			AbstractQuarter newAbstractQuarter = index.getAbstractQuarter(event.entity.worldObj.getChunkFromChunkCoords(event.newChunkX, event.newChunkZ));
			
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
