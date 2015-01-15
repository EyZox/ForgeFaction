package fr.eyzox.forgefaction.territory;

import java.util.Collection;

import fr.eyzox.forgefaction.territory.quarter.AbstractQuarter;
import net.minecraft.world.chunk.Chunk;

public interface TerritoryAccess {
	public AbstractQuarter getAbstractQuarter(ForgeFactionChunk c);
	public Collection<AbstractQuarter> checkConflicts(AbstractQuarter quarter);
}
