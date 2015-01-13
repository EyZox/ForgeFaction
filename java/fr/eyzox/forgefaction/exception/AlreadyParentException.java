package fr.eyzox.forgefaction.exception;

import fr.eyzox.forgefaction.territory.quarter.AbstractQuarter;
import fr.eyzox.forgefaction.territory.quarter.Quarter;

public class AlreadyParentException extends ForgeFactionException {
	public final AbstractQuarter quarter;
	
	public AlreadyParentException(Quarter quarter) {
		super("This quarter already has a source");
		this.quarter = quarter;
	}
}
