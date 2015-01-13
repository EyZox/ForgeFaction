package fr.eyzox.forgefaction.exception;

import fr.eyzox.forgefaction.territory.quarter.AbstractQuarter;
import fr.eyzox.forgefaction.territory.quarter.Quarter;

public class NoAdjacentChunkException extends ForgeFactionException {
	public final AbstractQuarter src;
	public final Quarter dest;

	public NoAdjacentChunkException(AbstractQuarter src, Quarter dest) {
		super("Quarters don't have adjacent chunk");
		this.src = src;
		this.dest = dest;
	}
}
