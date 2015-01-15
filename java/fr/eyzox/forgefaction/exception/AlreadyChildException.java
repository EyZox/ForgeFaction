package fr.eyzox.forgefaction.exception;

import fr.eyzox.forgefaction.territory.IParentQuarter;
import fr.eyzox.forgefaction.territory.IQuarter;

public class AlreadyChildException extends ForgeFactionException {
	
	public final IParentQuarter src;
	public final IQuarter dest;
	
	public AlreadyChildException(IParentQuarter src, IQuarter dest) {
		super("Source quarter already has a child");
		this.src = src;
		this.dest = dest;
	}
}
