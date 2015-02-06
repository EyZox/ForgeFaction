package fr.eyzox.forgefaction.event;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.eyzox.forgefaction.network.TerritoryChunkPacket;
import fr.eyzox.forgefaction.renderer.GridChunkRenderer;
import fr.eyzox.forgefaction.renderer.IChunkRenderer;
import fr.eyzox.forgefaction.renderer.filter.FaceFilter;
import fr.eyzox.forgefaction.renderer.filter.TerritoryFaceFilter;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;

@SideOnly(Side.CLIENT)
public class TerritoryChunkViewer implements IEvent {

	private final static TerritoryChunkViewer INSTANCE = new TerritoryChunkViewer();

	public static TerritoryChunkViewer getInstance() { return INSTANCE;}
	private TerritoryChunkViewer() {}

	
	private TerritoryFaceFilter filter = new TerritoryFaceFilter();
	private IChunkRenderer chunkRenderer = new GridChunkRenderer();
	
	private boolean enabled;

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onChunkRendered(RenderWorldLastEvent event) {
		if(!enabled || filter.getChunksToDraw().size() == 0)  {return;}

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ForgeFactionChunk chunk = new ForgeFactionChunk();
		chunk.dimensionID = Minecraft.getMinecraft().theWorld.provider.dimensionId;
		
		chunkRenderer.startRenderer();
		
		for(int xChunk = player.chunkCoordX - 2; xChunk <=player.chunkCoordX + 2; xChunk++) {
			for(int zChunk = player.chunkCoordZ - 2; zChunk <=player.chunkCoordZ + 2; zChunk++) {
				chunk.xPosition = xChunk;
				chunk.zPosition = zChunk;
				if(filter.getChunksToDraw().contains(chunk)) {
					chunkRenderer.render(chunk, filter);
				}
			}
		}
		
		chunkRenderer.stopRenderer();
	}

	public void process(TerritoryChunkPacket paquet) {
		if(!enabled) return;
		filter.getChunksToDraw().removeAll(paquet.getToRemove());
		filter.getChunksToDraw().addAll(paquet.getToAdd());
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if(!this.enabled) {
			filter.getChunksToDraw().clear();
		}
	}



}
