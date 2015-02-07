package fr.eyzox.forgefaction.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.eyzox.forgefaction.ForgeFactionMod;
import fr.eyzox.forgefaction.client.gui.component.GuiToggleButton;
import fr.eyzox.forgefaction.territory.IQuarter;

@SideOnly(Side.CLIENT)
public abstract class GUIQuarter extends GuiScreen {
	private final static ResourceLocation TEXTURE = new ResourceLocation(ForgeFactionMod.MODID, "textures/gui/default_bg.png");
	private int xSize = 248, ySize = 166;
	private int xOffset, yOffset;
	
	protected IQuarter quarter;
	private String name;
	private String status;
	
	protected int nextID = 0;
	
	protected GuiToggleButton previewButton = new GuiToggleButton(0, 0, 0, StatCollector.translateToLocal("gui.preview"));
	protected int previewButtonID;
	
	public GUIQuarter(IQuarter quarter) {
		this.quarter = quarter;
		this.name = quarter.getName();
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawTexture();
		drawCenteredString(mc.fontRenderer, name, this.width/2, this.yOffset+5, 0xFFFFFF);
		if(status != null) {
			drawCenteredString(mc.fontRenderer, name, this.width/2, this.yOffset+20, 0);
		}
		drawContent(p_73863_1_, p_73863_2_, p_73863_3_);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@Override
	public void initGui() {
		super.initGui();
		yOffset = (this.height - this.ySize) / 2;
		xOffset = (this.width - this.xSize) / 2;
		
		previewButtonID = ++nextID;
		previewButton.id = previewButtonID;
		
		buttonList.add(previewButton);
	}
	
	
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		super.actionPerformed(guiButton);
		if(guiButton == previewButton) {
			
		}
	}

	private void drawTexture() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);
	}
	
	public void drawContent(int p_73863_1_, int p_73863_2_, float p_73863_3_){}

	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public int getXOffset() {
		return xOffset;
	}

	public int getYOffset() {
		return yOffset;
	}
	
	public int getYContentOffset() {
		return yOffset+20+(status!=null?20:0);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
