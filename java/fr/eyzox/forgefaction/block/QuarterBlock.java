package fr.eyzox.forgefaction.block;

import fr.eyzox.forgefaction.creativetabs.ForgeFactionBlocksTab;
import fr.eyzox.forgefaction.territory.quarter.AbstractQuarter;
import fr.eyzox.forgefaction.territory.quarter.QuarterBase;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class QuarterBlock extends ModBlock implements AbstractQuarterBlock{
	
	public QuarterBlock() {
		super(Material.iron);
		setCreativeTab(ForgeFactionBlocksTab.getInstance());
	}

	@Override
	public AbstractQuarter createAbstractQuarter(World w, int x, int y, int z) {
		return new QuarterBase(w, x, y, z);
	}
}
