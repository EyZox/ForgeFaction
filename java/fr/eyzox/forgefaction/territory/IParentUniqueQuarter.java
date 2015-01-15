package fr.eyzox.forgefaction.territory;

import fr.eyzox.forgefaction.exception.ForgeFactionException;

public interface IParentUniqueQuarter<T> extends IParentQuarter<T> {
	public T getChild();
	public void unclaimsChild();
}
