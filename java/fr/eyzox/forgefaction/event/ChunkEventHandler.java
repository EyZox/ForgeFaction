package fr.eyzox.forgefaction.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgefaction.ForgeFactionData;
import fr.eyzox.forgefaction.team.Faction;
import fr.eyzox.forgefaction.territory.HeadQuarter;
import fr.eyzox.forgefaction.territory.Quarter;

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
					if(event.world == hq.getChunk().worldObj) {
						if(event.getChunk() == hq.getChunk()) {
							data.getIndex().add(hq);
							return;
						}
						for(Quarter quarter : hq.getQuarters()) {
							if(event.getChunk() == quarter.getChunk()) {
								data.getIndex().add(quarter);
								return;
							}
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

}
