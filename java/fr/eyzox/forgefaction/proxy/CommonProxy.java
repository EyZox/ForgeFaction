package fr.eyzox.forgefaction.proxy;

import fr.eyzox.forgefaction.event.ChunkEventHandler;
import fr.eyzox.forgefaction.event.ExtendedPropertiesHandler;
import fr.eyzox.forgefaction.event.PlayerInterractHandler;



public class CommonProxy {

	public void registerEvent() {
		new ExtendedPropertiesHandler().register();
		new PlayerInterractHandler().register();
		new ChunkEventHandler().register();
	}
	
	
}
