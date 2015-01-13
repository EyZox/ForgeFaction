package fr.eyzox.forgefaction.exception;

import java.util.Collection;

import fr.eyzox.forgefaction.territory.quarter.AbstractQuarter;

public class AlreadyClaimedException extends ForgeFactionException {
	public final AbstractQuarter newTerritory;
	public final Collection<AbstractQuarter> alreadyHere;

	public AlreadyClaimedException(Collection<AbstractQuarter> alreadyHere, AbstractQuarter newTerritory) {
		super("This territory is already claimed");
		this.alreadyHere = alreadyHere;
		this.newTerritory = newTerritory;
	}
	
	
}
