package fr.eyzox.forgefaction.event;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.eyzox.forgefaction.network.TerritoryChunkPacket;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;

@SideOnly(Side.CLIENT)
public class TerritoryChunkViewer implements IEvent {

	private final static TerritoryChunkViewer INSTANCE = new TerritoryChunkViewer();
	private final static ResourceLocation DEFAUT_GUI_TEXTURE = new ResourceLocation("textures/chunkviewer/demo_background.png");
	
	public static TerritoryChunkViewer getInstance() { return INSTANCE;}
	private TerritoryChunkViewer() {}

	private Set<ForgeFactionChunk> claimedChunk = new HashSet<ForgeFactionChunk>();
	private boolean enabled;

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onChunkRendered(RenderWorldLastEvent event) {
		if(!enabled || claimedChunk.size() == 0)  {return;}
		
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ForgeFactionChunk chunk = new ForgeFactionChunk();
		chunk.dimensionID = Minecraft.getMinecraft().theWorld.provider.dimensionId;
		
		GL11.glPushMatrix();
		GL11.glTranslated(-player.posX, -player.posY + 0.1, -player.posZ);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4ub((byte)0,(byte)255,(byte)0, (byte)70);
		GL11.glBegin(GL11.GL_QUADS);
		
		
		for(int xChunk = player.chunkCoordX - 2; xChunk <=player.chunkCoordX + 2; xChunk++) {
			for(int zChunk = player.chunkCoordZ - 2; zChunk <=player.chunkCoordZ + 2; zChunk++) {
				chunk.xPosition = xChunk;
				chunk.zPosition = zChunk;
				if(claimedChunk.contains(chunk)) {
					highligthChunk(chunk);
				}
			}
		}
		
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void process(TerritoryChunkPacket paquet) {
		if(!enabled) return;
		claimedChunk.removeAll(paquet.getToRemove());
		claimedChunk.addAll(paquet.getToAdd());
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if(!this.enabled) {
			claimedChunk.clear();
		}
	}
	
	private void highligthChunk(ForgeFactionChunk c) {
		
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		int xPos = c.xPosition<<4;
		int zPos = c.zPosition<<4;
		
		drawChunkFaceY(player, xPos, zPos);
		
		c.xPosition--;
		if(!claimedChunk.contains(c)) {
		drawChunkFaceX(player, xPos, zPos);
		}
			
		
		c.xPosition+=2;
		if(!claimedChunk.contains(c)) {
	
		drawChunkFaceX(player, xPos+16, zPos);
		}
		
		c.xPosition--;
		c.zPosition--;
		if(!claimedChunk.contains(c)) {
			drawChunkFaceZ(player, xPos, zPos);
		}
		
		c.zPosition+=2;
		if(!claimedChunk.contains(c)) {
			drawChunkFaceZ(player, xPos, zPos+16);
		}
		c.zPosition--;	
	}
	
	private void drawChunkFaceX(EntityPlayer player, int xPos, int zPos) {
		if(player.posX < xPos) {
			GL11.glVertex3f(xPos,player.worldObj.getActualHeight(),zPos);
			GL11.glVertex3f(xPos,0,zPos);
			GL11.glVertex3f(xPos,0,zPos+16);
			GL11.glVertex3f(xPos,player.worldObj.getActualHeight(),zPos+16);
		}else {
			GL11.glVertex3f(xPos,player.worldObj.getActualHeight(),zPos+16);
			GL11.glVertex3f(xPos,0,zPos+16);
			GL11.glVertex3f(xPos,0,zPos);
			GL11.glVertex3f(xPos,player.worldObj.getActualHeight(),zPos);
		}
	}
	
	private void drawChunkFaceY(EntityPlayer player, int xPos, int zPos) {
		int y = player.worldObj.getActualHeight();
		if(player.posY < y) {
			GL11.glVertex3f(xPos+16,y,zPos+16);
			GL11.glVertex3f(xPos,y,zPos+16);
			GL11.glVertex3f(xPos,y,zPos);
			GL11.glVertex3f(xPos+16,y,zPos);
		}else {
			GL11.glVertex3f(xPos+16,y,zPos);
			GL11.glVertex3f(xPos,y,zPos);
			GL11.glVertex3f(xPos,y,zPos+16);
			GL11.glVertex3f(xPos+16,y,zPos+16);
		}
	}
	
	private void drawChunkFaceZ(EntityPlayer player, int xPos, int zPos) {
		if(player.posZ < zPos) {
			GL11.glVertex3f(xPos+16,player.worldObj.getActualHeight(),zPos);
			GL11.glVertex3f(xPos+16,0,zPos);
			GL11.glVertex3f(xPos,0,zPos);
			GL11.glVertex3f(xPos,player.worldObj.getActualHeight(),zPos);
			
		}else {
			GL11.glVertex3f(xPos,player.worldObj.getActualHeight(),zPos);
			GL11.glVertex3f(xPos,0,zPos);
			GL11.glVertex3f(xPos+16,0,zPos);
			GL11.glVertex3f(xPos+16,player.worldObj.getActualHeight(),zPos);
		}
	}
	
	

}
