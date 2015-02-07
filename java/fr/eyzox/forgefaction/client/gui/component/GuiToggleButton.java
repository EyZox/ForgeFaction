package fr.eyzox.forgefaction.client.gui.component;

import net.minecraft.client.gui.GuiButton;

public class GuiToggleButton extends GuiButton {

	protected boolean activated;
	
	public GuiToggleButton(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_,
			int p_i1021_4_, int p_i1021_5_, String p_i1021_6_) {
		super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, p_i1021_6_+" : Off");
	}

	public GuiToggleButton(int p_i1020_1_, int p_i1020_2_, int p_i1020_3_,
			String p_i1020_4_) {
		super(p_i1020_1_, p_i1020_2_, p_i1020_3_, p_i1020_4_+" : Off");
	}

	public boolean isActivated() {
		return activated;
	}

	@Override
	public void mouseReleased(int p_146118_1_, int p_146118_2_) {
		super.mouseReleased(p_146118_1_, p_146118_2_);
		setActivated(!this.activated);
		
	}
	
	public void setActivated(boolean activated) {
		this.activated = activated;
		updateText();
	}
	
	private void updateText() {
		String baseText = this.displayString.substring(0, this.displayString.length()-6);
		if(isActivated()) {
			this.displayString = baseText+" : On ";
		}else {
			this.displayString = baseText+" : Off";
		}
	}

	
}
