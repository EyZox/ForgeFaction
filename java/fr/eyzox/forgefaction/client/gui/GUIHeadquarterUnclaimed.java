package fr.eyzox.forgefaction.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import fr.eyzox.forgefaction.territory.quarter.Headquarter;

@SideOnly(Side.CLIENT)
public class GUIHeadquarterUnclaimed extends GUIQuarter {
	
	private GuiButton change;
	
	private GuiButton claim;
	
	public GUIHeadquarterUnclaimed(Headquarter headquarter) {
		super(headquarter);
	}

	@Override
	public void initGui() {
		super.initGui();
		
		previewButton.yPosition = getYContentOffset();
		change = new GuiButton(1, 0, previewButton.yPosition+previewButton.height+5, StatCollector.translateToLocal("gui.change"));
		claim = new GuiButton(2, 0, change.yPosition+change.height+5, StatCollector.translateToLocal("gui.claim"));
		
		previewButton.xPosition = (this.width-previewButton.width)/2;
		
		
		change.xPosition = (this.width-change.width)/2;
		claim.xPosition = (this.width-claim.width)/2;
		
		buttonList.add(change);
		buttonList.add(claim);
	}
	
	

}
