package fr.eyzox.forgefaction.territory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.eyzox.forgefaction.territory.quarter.AbstractQuarter;
import fr.eyzox.forgefaction.territory.quarter.HeadQuarter;
import net.minecraft.world.chunk.Chunk;

public class TerritoryIndex implements TerritoryAccess{

	private final static TerritoryIndex INSTANCE = new TerritoryIndex();
	public static TerritoryIndex getIndex() { return INSTANCE;}
	
	private Map<ForgeFactionChunk, AbstractQuarter> index = new HashMap<ForgeFactionChunk, AbstractQuarter>();
	
	private TerritoryIndex() {}
	
	public void add(AbstractQuarter quarter) {
		for(ForgeFactionChunk chunk : quarter.getAllChunks()) {
			this.add(quarter, chunk);
		}
	}
	
	public void remove(AbstractQuarter quarter) {
		for(ForgeFactionChunk chunk : quarter.getAllChunks()) {
			this.remove(chunk);
		}
	}
	
	public void add(AbstractQuarter quarter, ForgeFactionChunk c) {
		this.index.put(c, quarter);
	}
	
	public void remove(ForgeFactionChunk c) {
		index.remove(c);
	}
	
	public AbstractQuarter getAbstractQuarter(ForgeFactionChunk c) {
		return index.get(c);
	}
	
	public Collection<AbstractQuarter> checkConflicts(AbstractQuarter quarter) {
		List<AbstractQuarter> conflicts = new ArrayList<AbstractQuarter>();
		for(AbstractQuarter loadedQuarter : getLoadedAbstractQuarters()) {
			if(quarter.contains(loadedQuarter)) {
				conflicts.add(loadedQuarter);
			}
		}
		return conflicts;
	}
	
	public Set<AbstractQuarter> getLoadedAbstractQuarters() {
		return new HashSet<AbstractQuarter>(index.values());
	}
}
