package fr.eyzox.forgefaction.exception;

import java.util.Collection;

import fr.eyzox.forgefaction.territory.AbstractQuarter;

public class AlreadyClaimedException extends Exception {
	public final AbstractQuarter newTerritory;
	public final Collection<AbstractQuarter> alreadyHere;

	public AlreadyClaimedException(Collection<AbstractQuarter> alreadyHere, AbstractQuarter newTerritory) {
		super("Area already claimed");
		this.alreadyHere = alreadyHere;
		this.newTerritory = newTerritory;
	}
	
	
}
