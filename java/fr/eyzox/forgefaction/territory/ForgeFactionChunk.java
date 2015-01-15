package fr.eyzox.forgefaction.territory;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import fr.eyzox.forgefaction.serial.NBTSupported;

public class ForgeFactionChunk implements NBTSupported{
	public int dimensionID, xPosition, zPosition;
	
	public ForgeFactionChunk(){}
	public ForgeFactionChunk(Chunk c) {
		this.dimensionID = c.worldObj.provider.dimensionId;
		this.xPosition = c.xPosition;
		this.zPosition = c.zPosition;
	}
	public ForgeFactionChunk(NBTTagCompound tag) { this(); readFromNBT(tag);}
	public ForgeFactionChunk(int dimensionID, int xPosition, int zPosition) {
		this.dimensionID = dimensionID;
		this.xPosition = xPosition;
		this.zPosition = zPosition;
	}


	public World getWorld() {
		return DimensionManager.getWorld(dimensionID);
	}
	
	public Chunk getChunk() {
		World w = getWorld();
		if(w == null) return null;
		return w.getChunkFromChunkCoords(xPosition, zPosition);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setIntArray("FFChunk", new int[]{dimensionID, xPosition, zPosition});
	}
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		int[] t = tag.getIntArray("FFChunk");
		this.dimensionID = t[0];
		this.xPosition = t[1];
		this.zPosition = t[2];
	}
	
	@Override
	public int hashCode() {
		return (((17+dimensionID)*17+xPosition)*17+zPosition)*17;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Chunk) {
			Chunk c = (Chunk) obj;
			return c.worldObj.provider.dimensionId == dimensionID && c.xPosition == xPosition && c.zPosition == zPosition;
		}
		
		if(obj instanceof ForgeFactionChunk) {
			ForgeFactionChunk c = (ForgeFactionChunk) obj;
			return c.dimensionID == dimensionID && c.xPosition == xPosition && c.zPosition == zPosition;
		}
		
		return super.equals(obj);
	}
	@Override
	public String toString() {
		return "FFChunk [dim=" + dimensionID + ", xPosition="
				+ xPosition + ", zPosition=" + zPosition + "]";
	}
	
	public static final int getChunkPosition(int blockPosition) {
		return (blockPosition >> 4);
	}
	
	
}
