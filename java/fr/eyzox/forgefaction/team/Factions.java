package fr.eyzox.forgefaction.team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.world.chunk.Chunk;
import fr.eyzox.forgefaction.ForgeFactionData;
import fr.eyzox.forgefaction.territory.AbstractQuarter;
import fr.eyzox.forgefaction.territory.HeadQuarter;
import fr.eyzox.forgefaction.territory.Quarter;
import fr.eyzox.forgefaction.territory.TerritoryAccess;

public class Factions implements TerritoryAccess{
	private Set<Faction> factions;

	public Factions() {
		factions = new TreeSet<Faction>(new Comparator<Faction>() {

			@Override
			public int compare(Faction o1, Faction o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
	}

	public boolean add(Faction team) {
		boolean b = factions.add(team);
		if(b) {
			ForgeFactionData.getData().markDirty();
		}
		return b;
	}

	public void remove(Faction team) {
		if(factions.remove(team)) ForgeFactionData.getData().markDirty();
	}

	public boolean contains(String name) {
		for(Faction team : factions) {
			if(team.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}

	public Faction get(String name) {
		for(Faction team : factions) {
			if(team.getName().equalsIgnoreCase(name)) return team;
		}
		return null;
	}
	
	public Collection<Faction> getFactions() {
		return factions;
	}

	@Override
	public AbstractQuarter getAbstractQuarter(Chunk c) {
		for(Faction faction : factions) {
			for(HeadQuarter hq : faction.getHeadquarters()) {
				if(hq.contains(c)) return hq;
				for(Quarter quarter: hq.getQuarters()) {
					do {
						if(quarter.contains(c)) return quarter;
						quarter = quarter.getChild();
					}while(quarter != null);
				}
			}
		}
		return null;
	}

	@Override
	public Collection<AbstractQuarter> checkConflicts(AbstractQuarter quarterToCheck) {
		List<AbstractQuarter> conflicts = new ArrayList<AbstractQuarter>();
		for(Faction faction : factions) {
			for(HeadQuarter hq : faction.getHeadquarters()) {
				if(hq.contains(quarterToCheck)) conflicts.add(hq);
				for(Quarter quarter: hq.getQuarters()) {
					do {
						if(quarter.contains(quarterToCheck)) conflicts.add(quarter);
						quarter = quarter.getChild();
					}while(quarter != null);
				}
			}
		}
		return conflicts;
	}
}
