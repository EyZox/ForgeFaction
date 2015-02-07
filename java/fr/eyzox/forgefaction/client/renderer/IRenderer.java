package fr.eyzox.forgefaction.client.renderer;

import fr.eyzox.forgefaction.client.renderer.filter.FaceFilter;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;

public interface IRenderer<E> {
	public void render(E e, FaceFilter filter);
	public void startRenderer();
	public void stopRenderer();
}
