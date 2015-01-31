package fr.eyzox.forgefaction.keys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import fr.eyzox.forgefaction.ForgeFactionMod;
import fr.eyzox.forgefaction.event.IEvent;
import fr.eyzox.forgefaction.event.TerritoryChunkViewer;

public class KeyBindings {

    public static KeyBinding viewChunkTerritory;

    public static void init() {

    	viewChunkTerritory = new KeyBinding("key.viewterritory", Keyboard.KEY_O, "key.categories"+ForgeFactionMod.MODID);
    	
    	ClientRegistry.registerKeyBinding(viewChunkTerritory);
    }
    
    public static class KeyInputHandler implements IEvent{

        @SubscribeEvent
        public void onKeyInput(InputEvent.KeyInputEvent event) {
        	if(viewChunkTerritory.isPressed()) {
            	TerritoryChunkViewer.getInstance().setEnabled(!TerritoryChunkViewer.getInstance().isEnabled());
            	Minecraft.getMinecraft().thePlayer.sendChatMessage("/ff-view");
            }
               
        }

		@Override
		public void register() {
			FMLCommonHandler.instance().bus().register(this);
		}

    }

}
