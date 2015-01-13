package fr.eyzox.forgefaction.territory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.chunk.Chunk;

public class TerritoryIndex implements TerritoryAccess{

	private Map<Chunk, AbstractQuarter> index = new HashMap<Chunk, AbstractQuarter>();
	
	public void add(AbstractQuarter quarter) {
		for(Chunk chunk : quarter.getAllChunks()) {
			if(chunk.isChunkLoaded) this.add(quarter, chunk);
		}
	}
	
	public void remove(AbstractQuarter quarter) {
		for(Chunk chunk : quarter.getAllChunks()) {
			this.remove(chunk);
		}
	}
	
	public void add(AbstractQuarter quarter, Chunk c) {
		this.index.put(c, quarter);
	}
	
	public void remove(Chunk c) {
		index.remove(c);
	}
	
	public AbstractQuarter getAbstractQuarter(Chunk c) {
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
