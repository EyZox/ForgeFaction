package fr.eyzox.forgefaction.client;

import java.util.Set;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import fr.eyzox.forgefaction.client.renderer.GridChunkRenderer;
import fr.eyzox.forgefaction.client.renderer.IRenderer;
import fr.eyzox.forgefaction.client.renderer.filter.FaceFilter;
import fr.eyzox.forgefaction.client.renderer.filter.TerritoryFaceFilter;
import fr.eyzox.forgefaction.event.IEvent;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;
import fr.eyzox.forgefaction.territory.IQuarter;

public class QuarterPreview implements IEvent{
	private Set<IQuarter> quartersToPreview;
	private TerritoryFaceFilter filter = new TerritoryFaceFilter();
	private IRenderer renderer = new GridChunkRenderer();
	
	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onChunkRendered(RenderWorldLastEvent event) {
		
	}
	
	private class Renderer implements IRenderer<IQuarter> {
		
		@Override
		public void render(IQuarter quarter, FaceFilter filter) {
			// TODO Auto-generated method stub
			
		}

		private void drawChunkFaceX(int xPos, int zPos) {
			for(float y = 0; y<=Minecraft.getMinecraft().theWorld.getActualHeight(); y+=0.5F) {
				GL11.glVertex3f(xPos,y,zPos);
				GL11.glVertex3f(xPos+16,y,zPos);
			}
			
			for(float x = xPos; x<=xPos+16; x+=0.5F) {
				GL11.glVertex3f(x,0,zPos);
				GL11.glVertex3f(x,Minecraft.getMinecraft().theWorld.getActualHeight(),zPos);
			}
		}

		private void drawChunkFaceY(int xPos, int zPos) {
			int y = Minecraft.getMinecraft().theWorld.getActualHeight();
			for(float x = xPos; x<=xPos+16; x+=0.5F) {
				GL11.glVertex3f(x,y,zPos);
				GL11.glVertex3f(x,y,zPos+16);
			}
			
			for(float z = zPos; z<=zPos+16; z+=0.5F) {
				GL11.glVertex3f(xPos,y,z);
				GL11.glVertex3f(xPos+16,y,z);
			}
		}

		private void drawChunkFaceZ(int xPos, int zPos) {
			for(float y = 0; y<=Minecraft.getMinecraft().theWorld.getActualHeight(); y+=0.5F) {
				GL11.glVertex3f(xPos,y,zPos);
				GL11.glVertex3f(xPos,y,zPos+16);
			}
			
			for(float z = zPos; z<=zPos+16; z+=0.5F) {
				GL11.glVertex3f(xPos,0,z);
				GL11.glVertex3f(xPos,Minecraft.getMinecraft().theWorld.getActualHeight(),z);
			}
		}

		@Override
		public void startRenderer() {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			GL11.glPushMatrix();
			GL11.glTranslated(-player.posX, -player.posY, -player.posZ);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(0F, 1F, 0F);
			GL11.glBegin(GL11.GL_LINES);
			
		}

		@Override
		public void stopRenderer() {
			GL11.glEnd();
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		
	}
	
	
}
