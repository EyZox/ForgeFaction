package fr.eyzox.forgefaction.proxy;

import fr.eyzox.forgefaction.event.TerritoryChunkViewer;
import fr.eyzox.forgefaction.keys.KeyBindings;


public class ClientProxy extends CommonProxy {

	@Override
	public void registerEvent() {
		super.registerEvent();
		KeyBindings.init();
		new KeyBindings.KeyInputHandler().register();
		TerritoryChunkViewer.getInstance().register();
	}


}
