package fr.eyzox.forgefaction.block;

import net.minecraft.world.World;
import fr.eyzox.forgefaction.territory.AbstractQuarter;

public interface AbstractQuarterBlock {
	public AbstractQuarter createAbstractQuarter(World w, int x, int y, int z);
}
