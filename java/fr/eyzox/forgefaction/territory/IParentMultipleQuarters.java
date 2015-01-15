package fr.eyzox.forgefaction.territory;

import java.util.Collection;

public interface IParentMultipleQuarters<T> extends IParentQuarter<T> {
	public Collection<T> getChilds();
	public void unclaims(T child);
}
