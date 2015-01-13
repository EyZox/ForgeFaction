package fr.eyzox.forgefaction.exception;

import fr.eyzox.forgefaction.territory.AbstractQuarter;
import fr.eyzox.forgefaction.territory.Quarter;

public class AlreadyParentException extends Exception {
	public final AbstractQuarter parent, newChild;
	
	public AlreadyParentException(AbstractQuarter parent, Quarter newChild) {
		super("New quarter already has a source");
		this.parent = parent;
		this.newChild = newChild;
	}
}
