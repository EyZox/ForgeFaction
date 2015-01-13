package fr.eyzox.forgefaction.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;

public class ExtendedPropertiesHandler implements IEvent{
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event){
		if(event.entity instanceof EntityPlayer && ForgeFactionPlayerProperties.get((EntityPlayer) event.entity) == null) {
				ForgeFactionPlayerProperties.register((EntityPlayer) event.entity);
		}
	}
	
	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
