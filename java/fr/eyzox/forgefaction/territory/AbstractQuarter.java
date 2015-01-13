package fr.eyzox.forgefaction.territory;

import java.util.Iterator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import fr.eyzox.forgefaction.ForgeFactionData;
import fr.eyzox.forgefaction.serial.NBTSupported;
import fr.eyzox.forgefaction.serial.NBTUtils;
import fr.eyzox.forgefaction.team.Faction;

public abstract class AbstractQuarter {
	protected static World worldLoader;
	private Chunk chunk;
	protected int x,y,z;
	private int attackTimer;
	
	public AbstractQuarter() {}
	public AbstractQuarter(World w, int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.chunk = w.getChunkFromBlockCoords(x, z);
	}
	public AbstractQuarter(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}
	
	public abstract int getSize();
	public abstract Faction getFaction();
	
	public boolean isAttacked() {
		return attackTimer>=0;
	}
	
	public int getTimeLeft() {
		return attackTimer;
	}
	
	public int compareX(Chunk c) {
		return this.chunk.xPosition - c.xPosition;
	}
	
	public int compareZ(Chunk c) {
		return this.chunk.zPosition - c.zPosition;
	}
	
	public boolean contains(Chunk c) {
		return contains(c.worldObj, c.xPosition, c.zPosition);
	}
	
	public boolean contains(World world, int xPosition, int zPosition) {
		return this.getChunk().worldObj == world && (xPosition >= this.chunk.xPosition && xPosition < this.chunk.xPosition+getSize()) && (zPosition >= this.chunk.zPosition && zPosition < this.chunk.zPosition+getSize());
	}
	
	public boolean contains(AbstractQuarter quarter) {
		return this.getChunk().worldObj == quarter.getChunk().worldObj
				&& this.getChunk().xPosition+this.getSize()-1 >= quarter.getChunk().xPosition && this.getChunk().xPosition < quarter.getChunk().xPosition+quarter.getSize()
				&& this.getChunk().zPosition+this.getSize()-1 >= quarter.getChunk().zPosition && this.getChunk().zPosition < quarter.getChunk().zPosition+quarter.getSize();
	}
	
	public Chunk getChunk() {
		return chunk;
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setIntArray("chunk", new int[] {chunk.xPosition, chunk.zPosition});
		tag.setIntArray("block", new int[] {x,y,z});
	}

	public void readFromNBT(NBTTagCompound tag) {
		int[] t = tag.getIntArray("chunk");
		this.chunk = worldLoader.getChunkFromChunkCoords(t[0], t[1]);
		t = tag.getIntArray("block");
		this.x = t[0];
		this.y = t[1];
		this.z = t[2];
	}
	
	public String printCoordinates() {
		return "[("+chunk.xPosition+","+chunk.zPosition+"),("+(chunk.xPosition+getSize())+","+(chunk.zPosition+getSize())+")]";
	}
	
	public boolean isQuarterBlock(World w, int x, int y, int z){
		return this.chunk.worldObj == w && this.x == x && this.y == y && this.z == z;
	}
	
	public void onUnclaims() {
		ForgeFactionData.getData().getIndex().remove(this);
	}
	
	public boolean isAdjacent(AbstractQuarter quarter) {
		boolean adjacent = false;
		for(int x = this.getChunk().xPosition; !adjacent && x<this.getChunk().xPosition+this.getSize(); x++) {
			if(quarter.contains(this.getChunk().worldObj, x, this.getChunk().zPosition-1) || quarter.contains(this.getChunk().worldObj, x, this.getChunk().zPosition+this.getSize())) {
				adjacent = true;
			}
		}
		for(int z = this.getChunk().zPosition; !adjacent && z<this.getChunk().zPosition+this.getSize(); x++) {
			if(quarter.contains(this.getChunk().worldObj, this.getChunk().xPosition-1,z) || quarter.contains(this.getChunk().worldObj, this.getChunk().xPosition+this.getSize(),z)) {
				adjacent = true;
			}
		}
		return adjacent;
	}
	
	public Iterator<Chunk> getChunkIterator() {
		return new Iterator<Chunk>() {
			int x = chunk.xPosition, z = chunk.zPosition;
			@Override
			public boolean hasNext() {
				return z < chunk.zPosition+getSize() && x<chunk.xPosition+getSize();
			}

			@Override
			public Chunk next() {
				Chunk c = chunk.worldObj.getChunkFromChunkCoords(x, z);
				x++;
				if(x >= chunk.xPosition+getSize()) {
					x = chunk.xPosition;
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
	
	public Iterable<Chunk> getAllChunks() {
		return new Iterable<Chunk>() {

			@Override
			public Iterator<Chunk> iterator() {
				return getChunkIterator();
			}
		};
	}
	
}
