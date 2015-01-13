package fr.eyzox.forgefaction.exception;

import fr.eyzox.forgefaction.territory.Quarter;

public class AlreadyChildException extends Exception {
	
	public final Quarter src, dest;
	
	public AlreadyChildException(Quarter src, Quarter dest) {
		super("Source quarter already has a child");
		this.src = src;
		this.dest = dest;
	}
}
