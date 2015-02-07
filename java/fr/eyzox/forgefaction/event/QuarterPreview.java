package fr.eyzox.forgefaction.event;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import fr.eyzox.forgefaction.client.renderer.GridChunkRenderer;
import fr.eyzox.forgefaction.client.renderer.IRenderer;
import fr.eyzox.forgefaction.client.renderer.filter.FaceFilter;
import fr.eyzox.forgefaction.client.renderer.filter.TerritoryFaceFilter;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;
import fr.eyzox.forgefaction.territory.IQuarter;

@SideOnly(Side.CLIENT)
public class QuarterPreview implements IEvent{
	private Set<IQuarter> quartersToPreview = new HashSet<IQuarter>();
	private TerritoryFaceFilter filter = new TerritoryFaceFilter();
	private IRenderer<IQuarter> renderer = new Renderer();
	
	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onChunkRendered(RenderWorldLastEvent event) {
		if(!quartersToPreview.isEmpty()) {
			renderer.startRenderer();
			for(IQuarter quarter : quartersToPreview) {
				renderer.render(quarter, null);
			}
		}
	}
	
	private class Renderer implements IRenderer<IQuarter> {
		
		@Override
		public void render(IQuarter quarter, FaceFilter filter) {
			int xPosMin = ForgeFactionChunk.getBlockCoordinate(quarter.getFrom().xPosition);
			int zPosMin = ForgeFactionChunk.getBlockCoordinate(quarter.getFrom().zPosition);
			float xPosMax = xPosMin+ForgeFactionChunk.getBlockCoordinate(quarter.getFrom().xPosition+quarter.getXSize())-0.1f;
			float zPosMax = zPosMin+ForgeFactionChunk.getBlockCoordinate(quarter.getFrom().zPosition+quarter.getZSize())-0.1f;
			
			drawChunkFaceTop(xPosMin, zPosMin, xPosMax, zPosMax);
			drawChunkFaceXY(xPosMin, zPosMin, xPosMax, zPosMax);
			drawChunkFaceZY(xPosMin, zPosMin, xPosMax, zPosMax);
			
		}

		private void drawChunkFaceXY(int xPosMin, int zPosMin, float xPosMax, float zPosMax) {
			for(float y = 0; y<=Minecraft.getMinecraft().theWorld.getActualHeight(); y+=0.5F) {
				GL11.glVertex3f(xPosMin,y,zPosMin);
				GL11.glVertex3f(xPosMin,y,zPosMax);
				
				GL11.glVertex3f(xPosMax,y,zPosMin);
				GL11.glVertex3f(xPosMax,y,zPosMax);
			}
			
			for(float x = xPosMin; x<=xPosMax; x+=0.5F) {
				GL11.glVertex3f(x,0,zPosMin);
				GL11.glVertex3f(x,Minecraft.getMinecraft().theWorld.getActualHeight(),zPosMin);
				
				GL11.glVertex3f(x,0,zPosMax);
				GL11.glVertex3f(x,Minecraft.getMinecraft().theWorld.getActualHeight(),zPosMax);
			}
		}

		private void drawChunkFaceTop(int xPosMin, int zPosMin, float xPosMax, float zPosMax) {
			int y = Minecraft.getMinecraft().theWorld.getActualHeight();
			for(float x = xPosMin; x<= xPosMax; x+=0.5f) {
				GL11.glVertex3f(x,y,zPosMin);
				GL11.glVertex3f(x,y,zPosMax);
			}
			for(float z = zPosMin; z<= zPosMax; z+=0.5f) {
				GL11.glVertex3f(xPosMin,y,z);
				GL11.glVertex3f(xPosMax,y,z);
			}
		}

		private void drawChunkFaceZY(int xPosMin, int zPosMin, float xPosMax, float zPosMax) {
			for(float y = 0; y<=Minecraft.getMinecraft().theWorld.getActualHeight(); y+=0.5F) {
				GL11.glVertex3f(xPosMin,y,zPosMin);
				GL11.glVertex3f(xPosMax,y,zPosMin);
				
				GL11.glVertex3f(xPosMin,y,zPosMax);
				GL11.glVertex3f(xPosMax,y,zPosMax);
			}
			
			for(float z = zPosMin; z<=xPosMax; z+=0.5F) {
				GL11.glVertex3f(xPosMin,0,z);
				GL11.glVertex3f(xPosMin,Minecraft.getMinecraft().theWorld.getActualHeight(),z);
				
				GL11.glVertex3f(xPosMax,0,z);
				GL11.glVertex3f(xPosMax,Minecraft.getMinecraft().theWorld.getActualHeight(),z);
			}
		}

		@Override
		public void startRenderer() {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			GL11.glPushMatrix();
			GL11.glTranslated(-player.posX, -player.posY, -player.posZ);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(1F, 1F, 1F);
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
