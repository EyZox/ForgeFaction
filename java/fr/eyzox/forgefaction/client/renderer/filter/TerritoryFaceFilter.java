package fr.eyzox.forgefaction.client.renderer.filter;

import java.util.HashSet;
import java.util.Set;

import fr.eyzox.forgefaction.client.renderer.filter.FaceFilter.Face;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;

public class TerritoryFaceFilter implements FaceFilter {
	private Set<ForgeFactionChunk> chunksToDraw = new HashSet<ForgeFactionChunk>();
	private ForgeFactionChunk c = new ForgeFactionChunk();
	@Override
	public boolean draw(ForgeFactionChunk chunk, Face face) {
		c.dimensionID = chunk.dimensionID;
		c.xPosition = chunk.xPosition;
		c.zPosition = chunk.zPosition;
		
		switch (face) {
		case Xmax:
			c.zPosition++;
			break;
		case Xmin:
			c.zPosition--;
			break;
		case Zmax:
			c.xPosition++;
			break;
		case Zmin:
			c.xPosition--;
			break;
		default:
			return true;
		}
		
		return !chunksToDraw.contains(c);
	}
	public Set<ForgeFactionChunk> getChunksToDraw() {
		return chunksToDraw;
	}
}
