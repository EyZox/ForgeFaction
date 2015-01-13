package fr.eyzox.forgefaction.exception;

import fr.eyzox.forgefaction.territory.quarter.Quarter;

public class AlreadyChildException extends ForgeFactionException {
	
	public final Quarter src, dest;
	
	public AlreadyChildException(Quarter src, Quarter dest) {
		super("Source quarter already has a child");
		this.src = src;
		this.dest = dest;
	}
}
