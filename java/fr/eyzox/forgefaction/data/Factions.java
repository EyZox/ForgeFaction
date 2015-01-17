package fr.eyzox.forgefaction.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import fr.eyzox.forgefaction.faction.Faction;
import fr.eyzox.forgefaction.territory.AbstractQuarter;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;
import fr.eyzox.forgefaction.territory.IQuarter;
import fr.eyzox.forgefaction.territory.TerritoryAccess;
import fr.eyzox.forgefaction.territory.quarter.HeadQuarter;
import fr.eyzox.forgefaction.territory.quarter.QuarterBase;

public class Factions implements TerritoryAccess{
	protected Set<Faction> factionSet;

	public Factions() {
		factionSet = new TreeSet<Faction>(new Comparator<Faction>() {

			@Override
			public int compare(Faction o1, Faction o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
	}

	public boolean add(Faction team) {
		boolean b = factionSet.add(team);
		if(b) {
			ForgeFactionData.getData().markDirty();
		}
		return b;
	}

	public void remove(Faction team) {
		if(factionSet.remove(team)) ForgeFactionData.getData().markDirty();
	}

	public boolean contains(String name) {
		for(Faction team : factionSet) {
			if(team.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}

	public Faction get(String name) {
		for(Faction team : factionSet) {
			if(team.getName().equalsIgnoreCase(name)) return team;
		}
		return null;
	}
	
	public Collection<Faction> getFactions() {
		return factionSet;
	}

	@Override
	public IQuarter getIQuarter(ForgeFactionChunk c) {
		for(Faction faction : factionSet) {
			for(HeadQuarter hq : faction.getHeadquarters()) {
				if(hq.contains(c.dimensionID, c.xPosition, c.zPosition)) return hq;
				for(QuarterBase quarter: hq.getChilds()) {
					do {
						if(quarter.contains(c.dimensionID, c.xPosition, c.zPosition)) return quarter;
						quarter = quarter.getChild();
					}while(quarter != null);
				}
			}
		}
		return null;
	}

	@Override
	public Collection<IQuarter> checkConflicts(IQuarter quarterToCheck) {
		List<IQuarter> conflicts = new ArrayList<IQuarter>();
		for(Faction faction : factionSet) {
			for(HeadQuarter hq : faction.getHeadquarters()) {
				if(hq.contains(quarterToCheck)) conflicts.add(hq);
				for(QuarterBase quarter: hq.getChilds()) {
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
