package fr.eyzox.forgefaction.block;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import fr.eyzox.forgefaction.client.gui.GUIHeadquarterUnclaimed;
import fr.eyzox.forgefaction.client.gui.GUIQuarter;
import fr.eyzox.forgefaction.creativetabs.ForgeFactionBlocksTab;
import fr.eyzox.forgefaction.territory.AbstractQuarter;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;
import fr.eyzox.forgefaction.territory.IQuarter;
import fr.eyzox.forgefaction.territory.TerritoryIndex;
import fr.eyzox.forgefaction.territory.quarter.Headquarter;

public class HeadquarterBlock extends ModBlock implements AbstractQuarterBlock{

	public HeadquarterBlock() {
		super(Material.iron);
		setCreativeTab(ForgeFactionBlocksTab.getInstance());
	}

	@Override
	public AbstractQuarter createAbstractQuarter(World w, int x, int y, int z) {
		return new Headquarter(w, x, y, z);
	}

	@SuppressWarnings("unused")
	@Override
	public GUIQuarter getGUI(World w, int x, int y, int z) {
		IQuarter quarter = TerritoryIndex.getIndex().getIQuarter(new ForgeFactionChunk(w.provider.dimensionId, ForgeFactionChunk.getChunkPosition(x), ForgeFactionChunk.getChunkPosition(z)));
		if(quarter instanceof Headquarter) {
			if(quarter != null) {
				return null;
			}else {
				return new GUIHeadquarterUnclaimed(new Headquarter(w, x, y, z));
			}
		}
		return null;
	}
}
