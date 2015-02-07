package fr.eyzox.forgefaction.territory;

import fr.eyzox.forgefaction.exception.ForgeFactionException;
import fr.eyzox.forgefaction.faction.Faction;
import fr.eyzox.forgefaction.serial.NBTSupported;
import fr.eyzox.forgefaction.territory.quarter.QuarterBase;

public interface IQuarter extends NBTSupported{
	public ForgeFactionChunk getFrom();
	public int getXSize();
	public int getZSize();
	public Faction getFaction();
	public String getName();
	public boolean contains(int dimensionID, int xPosition, int zPosition);
	public boolean contains(IQuarter quarter);
	public boolean isTheBlock(int dim, int x, int y, int z);
	public void onUnclaims();
	public boolean isAdjacent(IQuarter quarter);
	public Iterable<ForgeFactionChunk> getAllChunks();
}
