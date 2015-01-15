package fr.eyzox.forgefaction.exception;

import fr.eyzox.forgefaction.territory.IParentQuarter;
import fr.eyzox.forgefaction.territory.quarter.AbstractQuarter;
import fr.eyzox.forgefaction.territory.quarter.QuarterBase;

public class NoAdjacentChunkException extends ForgeFactionException {
	public final IParentQuarter src;
	public final QuarterBase dest;

	public NoAdjacentChunkException(IParentQuarter src, QuarterBase dest) {
		super("Quarters don't have adjacent chunk");
		this.src = src;
		this.dest = dest;
	}
}
