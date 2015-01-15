package fr.eyzox.forgefaction.territory;

import fr.eyzox.forgefaction.exception.ForgeFactionException;

public interface IParentQuarter<E> extends IQuarter{
	public void claims(E child, TerritoryAccess access) throws ForgeFactionException;
}
