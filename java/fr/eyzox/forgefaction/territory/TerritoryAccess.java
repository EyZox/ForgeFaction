package fr.eyzox.forgefaction.territory;

import java.util.Collection;

import net.minecraft.world.chunk.Chunk;

public interface TerritoryAccess {
	public AbstractQuarter getAbstractQuarter(Chunk c);
	public Collection<AbstractQuarter> checkConflicts(AbstractQuarter quarter);
}
