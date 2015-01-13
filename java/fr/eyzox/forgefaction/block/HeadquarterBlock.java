package fr.eyzox.forgefaction.block;

import fr.eyzox.forgefaction.creativetabs.ForgeFactionBlocksTab;
import fr.eyzox.forgefaction.territory.quarter.AbstractQuarter;
import fr.eyzox.forgefaction.territory.quarter.HeadQuarter;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class HeadquarterBlock extends ModBlock implements AbstractQuarterBlock{

	public HeadquarterBlock() {
		super(Material.iron);
		setCreativeTab(ForgeFactionBlocksTab.getInstance());
	}

	@Override
	public AbstractQuarter createAbstractQuarter(World w, int x, int y, int z) {
		return new HeadQuarter(w, x, y, z);
	}
	
	

}
