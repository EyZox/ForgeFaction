package fr.eyzox.forgefaction.territory;

import java.util.Iterator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import fr.eyzox.forgefaction.data.ForgeFactionData;
import fr.eyzox.forgefaction.exception.AlreadyChildException;
import fr.eyzox.forgefaction.exception.AlreadyClaimedException;
import fr.eyzox.forgefaction.exception.AlreadyParentException;
import fr.eyzox.forgefaction.exception.ForgeFactionException;
import fr.eyzox.forgefaction.exception.NoAdjacentChunkException;
import fr.eyzox.forgefaction.faction.Faction;
import fr.eyzox.forgefaction.serial.NBTSupported;
import fr.eyzox.forgefaction.serial.NBTUtils;

public abstract class AbstractQuarter implements IQuarter{
	private ForgeFactionChunk ffChunk;
	protected int x,y,z;
	
	public AbstractQuarter() {}
	public AbstractQuarter(World w, int x, int y, int z) {
		this(w.provider.dimensionId,x,y,z);
	}
	public AbstractQuarter(int dim, int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.ffChunk = new ForgeFactionChunk(dim, x >> 4, z >> 4);
	}
	public AbstractQuarter(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}
	
	public boolean contains(int dimensionID, int xPosition, int zPosition) {
		return this.getChunk().dimensionID == dimensionID && (xPosition >= this.ffChunk.xPosition && xPosition < this.ffChunk.xPosition+getSize()) && (zPosition >= this.ffChunk.zPosition && zPosition < this.ffChunk.zPosition+getSize());
	}
	
	public boolean contains(IQuarter quarter) {
		return this.getChunk().dimensionID == quarter.getChunk().dimensionID
				&& this.getChunk().xPosition+this.getSize()-1 >= quarter.getChunk().xPosition && this.getChunk().xPosition < quarter.getChunk().xPosition+quarter.getSize()
				&& this.getChunk().zPosition+this.getSize()-1 >= quarter.getChunk().zPosition && this.getChunk().zPosition < quarter.getChunk().zPosition+quarter.getSize();
	}
	
	public ForgeFactionChunk getChunk() {
		return ffChunk;
	}

	public void writeToNBT(NBTTagCompound tag) {
		ffChunk.writeToNBT(tag);
		tag.setIntArray("block", new int[] {x,y,z});
	}

	public void readFromNBT(NBTTagCompound tag) {
		this.ffChunk = new ForgeFactionChunk(tag);
		int[] t = tag.getIntArray("block");
		this.x = t[0];
		this.y = t[1];
		this.z = t[2];
	}
	
	public String printCoordinates() {
		return "[("+ffChunk.xPosition+","+ffChunk.zPosition+"),("+(ffChunk.xPosition+getSize())+","+(ffChunk.zPosition+getSize())+")]";
	}
	
	public boolean isTheBlock(int dim, int x, int y, int z){
		return this.ffChunk.dimensionID == dim && this.x == x && this.y == y && this.z == z;
	}
	
	public void onUnclaims() {
		TerritoryIndex.getIndex().remove(this);
	}
	
	public boolean isAdjacent(IQuarter quarter) {
		boolean adjacent = false;
		for(int x = this.getChunk().xPosition; !adjacent && x<this.getChunk().xPosition+this.getSize(); x++) {
			if(quarter.contains(this.getChunk().dimensionID, x, this.getChunk().zPosition-1) || quarter.contains(this.getChunk().dimensionID, x, this.getChunk().zPosition+this.getSize())) {
				adjacent = true;
			}
		}
		for(int z = this.getChunk().zPosition; !adjacent && z<this.getChunk().zPosition+this.getSize(); z++) {
			if(quarter.contains(this.getChunk().dimensionID, this.getChunk().xPosition-1,z) || quarter.contains(this.getChunk().dimensionID, this.getChunk().xPosition+this.getSize(),z)) {
				adjacent = true;
			}
		}
		return adjacent;
	}
	
	public Iterator<ForgeFactionChunk> getChunkIterator() {
		return new Iterator<ForgeFactionChunk>() {
			int x = ffChunk.xPosition, z = ffChunk.zPosition;
			@Override
			public boolean hasNext() {
				return z < ffChunk.zPosition+getSize() && x<ffChunk.xPosition+getSize();
			}

			@Override
			public ForgeFactionChunk next() {
				ForgeFactionChunk c = new ForgeFactionChunk(ffChunk.dimensionID, x,z);
				x++;
				if(x >= ffChunk.xPosition+getSize()) {
					x = ffChunk.xPosition;
					z++;
				}
				return c;
			}

			@Override
			public void remove() {
				new IllegalAccessError("READ-ONLY iterator").printStackTrace();
			}
		};
	}
	
	public Iterable<ForgeFactionChunk> getAllChunks() {
		return new Iterable<ForgeFactionChunk>() {

			@Override
			public Iterator<ForgeFactionChunk> iterator() {
				return getChunkIterator();
			}
		};
	}
	
}
