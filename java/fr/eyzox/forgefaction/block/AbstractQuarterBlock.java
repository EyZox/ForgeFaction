package fr.eyzox.forgefaction.block;

import net.minecraft.world.World;
import fr.eyzox.forgefaction.client.gui.GUIQuarter;
import fr.eyzox.forgefaction.territory.AbstractQuarter;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;

public interface AbstractQuarterBlock {
	public AbstractQuarter createAbstractQuarter(World w, int x, int y, int z);
	public GUIQuarter getGUI(World w, int x, int y, int z);
}
