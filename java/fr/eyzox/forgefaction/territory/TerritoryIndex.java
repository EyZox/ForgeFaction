package fr.eyzox.forgefaction.territory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TerritoryIndex implements TerritoryAccess{

	private final static TerritoryIndex INSTANCE = new TerritoryIndex();
	public static TerritoryIndex getIndex() { return INSTANCE;}
	
	private Map<ForgeFactionChunk, IQuarter> index = new HashMap<ForgeFactionChunk, IQuarter>();
	
	private TerritoryIndex() {}
	
	public void add(IQuarter quarter) {
		for(ForgeFactionChunk chunk : quarter.getAllChunks()) {
			this.add(quarter, chunk);
		}
	}
	
	public void remove(IQuarter quarter) {
		for(ForgeFactionChunk chunk : quarter.getAllChunks()) {
			this.remove(chunk);
		}
	}
	
	public void add(IQuarter quarter, ForgeFactionChunk c) {
		this.index.put(c, quarter);
	}
	
	public void remove(ForgeFactionChunk c) {
		index.remove(c);
	}
	
	public IQuarter getIQuarter(ForgeFactionChunk c) {
		return index.get(c);
	}
	
	public Collection<IQuarter> checkConflicts(IQuarter quarter) {
		List<IQuarter> conflicts = new ArrayList<IQuarter>();
		for(IQuarter loadedQuarter : getLoadedAbstractQuarters()) {
			if(quarter.contains(loadedQuarter)) {
				conflicts.add(loadedQuarter);
			}
		}
		return conflicts;
	}
	
	public Set<IQuarter> getLoadedAbstractQuarters() {
		return new HashSet<IQuarter>(index.values());
	}
}
