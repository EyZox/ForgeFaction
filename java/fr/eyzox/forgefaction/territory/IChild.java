package fr.eyzox.forgefaction.territory;

public interface IChild<E> extends IQuarter {
	public E getParent();
	public void setParent(E parent);
}
