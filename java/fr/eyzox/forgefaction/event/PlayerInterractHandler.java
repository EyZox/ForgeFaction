package fr.eyzox.forgefaction.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgefaction.event.interract.InterractStrategy;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PlayerInterractHandler implements IEvent{

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void playerInterractEvent(PlayerInteractEvent event) {
		ForgeFactionPlayerProperties p = ForgeFactionPlayerProperties.get(event.entityPlayer);
		if(p.getInterractStrategy() != null) p.getInterractStrategy().onPlayerInterract(event);
			defaultPlayerInterract(event);
	}

	public void defaultPlayerInterract(PlayerInteractEvent e) {
		
	}

}
