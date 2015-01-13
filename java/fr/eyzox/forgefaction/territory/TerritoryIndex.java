package fr.eyzox.forgefaction.territory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.chunk.Chunk;
import fr.eyzox.forgefaction.team.Faction;
import fr.eyzox.forgefaction.team.Factions;

public class TerritoryIndex implements TerritoryAccess{

	private List<List<AbstractQuarter>> index;

	public TerritoryIndex() {
		index = new ArrayList<List<AbstractQuarter>>();
	}

	public boolean add(AbstractQuarter add) {
		List<AbstractQuarter> zList;

		if(index.isEmpty()) {
			zList = new ArrayList<AbstractQuarter>();
			zList.add(add);
			index.add(zList);
			return true;
		}


		int i = search(add.getChunk());
		if(i >= 0) {
			zList = index.get(i);
		}else {
			zList = new ArrayList<AbstractQuarter>();
			zList.add(add);
			index.add((++i)*-1, zList);
			return true;
		}

		i = search(zList, add.getChunk());
		if(i >= 0) return false; // Already exist

		zList.add((++i)*-1, add);
		return true;
	}

	public void remove(Chunk c) {
		if(index.isEmpty()) return;
		int i = search(c);
		if(i < 0) return;
		List<AbstractQuarter> zList = index.get(i);
		int iz = search(zList, c);
		if(iz < 0) return;
		if(zList.size() > 1) {
			zList.remove(iz);
		}else {
			index.remove(i);
		}

	}

	public AbstractQuarter getAbstractQuarter(Chunk c) {
		int i = search(c);
		if(i < 0) return null;
		List<AbstractQuarter> zList = index.get(i);
		i = search(zList, c);
		if(i < 0) return null;
		return zList.get(i);
	}
	
	public List<AbstractQuarter> checkConflicts(AbstractQuarter quarter) {
		List<AbstractQuarter> conflicts = new ArrayList<AbstractQuarter>();
		for(int idx=0; idx<index.size() && index.get(idx).get(0).getChunk().xPosition <=quarter.getChunk().xPosition+quarter.getSize(); idx++) {
			for(int idz=0; idz<index.get(idx).size() && index.get(idx).get(idz).getChunk().zPosition<=quarter.getChunk().zPosition+quarter.getSize(); idz++) {
				AbstractQuarter cursor = index.get(idx).get(idz);
				if(cursor.getChunk().xPosition+cursor.getSize()-1>=quarter.getChunk().xPosition && cursor.getChunk().zPosition+cursor.getSize()-1>=quarter.getChunk().zPosition) {
					conflicts.add(cursor);
				}
			}
		}
		return conflicts;
	}
	
	private int search(Chunk val) {

		int id = 0;
		int ifin = index.size();
		int im = 0;


		while((ifin - id) > 1){
			im = (id + ifin)/2;
			int trouve = index.get(im).get(0).compareX(val);
			if(trouve>0) ifin = im;
			else if(trouve<0) id = im;
			else return im;
		}


		return -id-1;

	}

	private int search(List<AbstractQuarter> list, Chunk val) {

		int id = 0;
		int ifin = list.size();
		int im = 0;


		while((ifin - id) > 1){
			im = (id + ifin)/2;
			int trouve = list.get(im).compareZ(val);
			if(trouve>0) ifin = im;
			else if(trouve<0)id = im;
			else return im;
		}

		return -id-1;

	}

}
