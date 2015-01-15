package fr.eyzox.forgefaction.exception;

import java.util.Collection;

import fr.eyzox.forgefaction.territory.IQuarter;

public class AlreadyClaimedException extends ForgeFactionException {
	public final IQuarter newTerritory;
	public final Collection<IQuarter> alreadyHere;

	public AlreadyClaimedException(Collection<IQuarter> alreadyHere, IQuarter newTerritory) {
		super("This territory is already claimed");
		this.alreadyHere = alreadyHere;
		this.newTerritory = newTerritory;
	}
	
	
}
