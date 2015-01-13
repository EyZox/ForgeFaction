package fr.eyzox.forgefaction.exception;

import fr.eyzox.forgefaction.territory.AbstractQuarter;
import fr.eyzox.forgefaction.territory.Quarter;

public class NoAdjacentChunkException extends Exception {
	public final AbstractQuarter src;
	public final Quarter dest;

	public NoAdjacentChunkException(AbstractQuarter src, Quarter dest) {
		super("Quarters don't have adjacent chunk");
		this.src = src;
		this.dest = dest;
	}
}
