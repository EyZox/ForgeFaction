package fr.eyzox.forgefaction.exception;

import fr.eyzox.forgefaction.territory.AbstractQuarter;
import fr.eyzox.forgefaction.territory.Quarter;

public class AlreadyParentException extends Exception {
	public final AbstractQuarter quarter;
	
	public AlreadyParentException(Quarter quarter) {
		super("New quarter already has a source");
		this.quarter = quarter;
	}
}
