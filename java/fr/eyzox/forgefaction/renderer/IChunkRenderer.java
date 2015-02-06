package fr.eyzox.forgefaction.renderer;

import fr.eyzox.forgefaction.renderer.filter.FaceFilter;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;

public interface IChunkRenderer {
	public void render(ForgeFactionChunk chunk, FaceFilter filter);
	public void startRenderer();
	public void stopRenderer();
}
