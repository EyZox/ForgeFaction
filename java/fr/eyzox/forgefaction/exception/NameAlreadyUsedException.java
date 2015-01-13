package fr.eyzox.forgefaction.exception;

import fr.eyzox.forgefaction.faction.Faction;

public class NameAlreadyUsedException extends ForgeFactionException {

	public final Faction team;
	
	public NameAlreadyUsedException(Faction team) {
		super("This name is already taken : "+team.getName());
		this.team = team;
	}
	
}
