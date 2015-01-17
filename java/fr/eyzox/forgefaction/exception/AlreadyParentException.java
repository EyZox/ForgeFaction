package fr.eyzox.forgefaction.exception;

import fr.eyzox.forgefaction.territory.AbstractQuarter;
import fr.eyzox.forgefaction.territory.IQuarter;
import fr.eyzox.forgefaction.territory.quarter.QuarterBase;

public class AlreadyParentException extends ForgeFactionException {
	public final IQuarter quarter;
	
	public AlreadyParentException(IQuarter quarter) {
		super("This quarter already has a source");
		this.quarter = quarter;
	}
}
