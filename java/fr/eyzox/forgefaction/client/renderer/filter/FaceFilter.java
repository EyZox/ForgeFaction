package fr.eyzox.forgefaction.client.renderer.filter;

import fr.eyzox.forgefaction.territory.ForgeFactionChunk;

public interface FaceFilter {
	public enum Face{
		Ymax, Ymin, Xmax, Xmin, Zmax, Zmin;
	}
	
	public boolean draw(ForgeFactionChunk chunk, Face face);
}
