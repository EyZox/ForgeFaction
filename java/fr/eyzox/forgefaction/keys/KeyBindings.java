package fr.eyzox.forgefaction.keys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import fr.eyzox.forgefaction.ForgeFactionMod;
import fr.eyzox.forgefaction.client.gui.GUIHeadquarterUnclaimed;
import fr.eyzox.forgefaction.event.IEvent;
import fr.eyzox.forgefaction.event.TerritoryChunkViewer;
import fr.eyzox.forgefaction.territory.quarter.Headquarter;

public class KeyBindings {

    public static KeyBinding viewChunkTerritory;
    public static KeyBinding openGui;

    public static void init() {

    	viewChunkTerritory = new KeyBinding("key.viewterritory", Keyboard.KEY_O, "key.categories"+ForgeFactionMod.MODID);
    	
    	ClientRegistry.registerKeyBinding(viewChunkTerritory);
    	
    	openGui = new KeyBinding("key.openGui", Keyboard.KEY_P, "key.categories"+ForgeFactionMod.MODID);
    	ClientRegistry.registerKeyBinding(openGui);
    }
    
    public static class KeyInputHandler implements IEvent{

        @SubscribeEvent
        public void onKeyInput(InputEvent.KeyInputEvent event) {
        	if(viewChunkTerritory.isPressed()) {
            	TerritoryChunkViewer.getInstance().setEnabled(!TerritoryChunkViewer.getInstance().isEnabled());
            	Minecraft.getMinecraft().thePlayer.sendChatMessage("/ff-view");
            }
        	
        	if(openGui.isPressed()) {
        		Minecraft.getMinecraft().displayGuiScreen(new GUIHeadquarterUnclaimed(new Headquarter(1, 0, 0, 0)));
        	}
               
        }

		@Override
		public void register() {
			FMLCommonHandler.instance().bus().register(this);
		}

    }

}
