package fr.eyzox.forgefaction.territory;

import java.util.Collection;

import net.minecraft.world.chunk.Chunk;

public interface TerritoryAccess {
	public IQuarter getIQuarter(ForgeFactionChunk c);
	public Collection<IQuarter> checkConflicts(IQuarter quarter);
}
