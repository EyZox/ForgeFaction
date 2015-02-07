package fr.eyzox.forgefaction.territory;

import java.util.Iterator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
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
	protected ForgeFactionChunk from;
	protected int x,y,z;
	
	public AbstractQuarter() {}
	public AbstractQuarter(World w, int x, int y, int z) {
		this(w.provider.dimensionId,x,y,z);
	}
	public AbstractQuarter(int dim, int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public AbstractQuarter(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}
	
	public boolean contains(ForgeFactionChunk chunk) {
		return contains(chunk.dimensionID, chunk.xPosition, chunk.zPosition);
	}
	
	public boolean contains(int dimensionID, int xPosition, int zPosition) {
		return this.getFrom().dimensionID == dimensionID
				&& (xPosition >= this.from.xPosition && xPosition < this.from.xPosition+getXSize())
				&& (zPosition >= this.from.zPosition && zPosition < this.from.zPosition+getZSize());
	}
	
	public boolean contains(IQuarter quarter) {
		return this.getFrom().dimensionID == quarter.getFrom().dimensionID
				&& this.getFrom().xPosition+this.getXSize()-1 >= quarter.getFrom().xPosition && this.getFrom().xPosition < quarter.getFrom().xPosition+quarter.getXSize()
				&& this.getFrom().zPosition+this.getZSize()-1 >= quarter.getFrom().zPosition && this.getFrom().zPosition < quarter.getFrom().zPosition+quarter.getZSize();
	}
	
	public ForgeFactionChunk getFrom() {
		return from;
	}

	public void writeToNBT(NBTTagCompound tag) {
		from.writeToNBT(tag);
		tag.setIntArray("block", new int[] {x,y,z});
	}

	public void readFromNBT(NBTTagCompound tag) {
		this.from = new ForgeFactionChunk(tag);
		int[] t = tag.getIntArray("block");
		this.x = t[0];
		this.y = t[1];
		this.z = t[2];
	}
	
	@Override
	public String toString() {
		return getName()+" : [("+from.xPosition+","+from.zPosition+"),("+(from.xPosition+getXSize()-1)+","+(from.zPosition+getZSize()-1)+")]";
	}
	
	public boolean isTheBlock(int dim, int x, int y, int z){
		return this.from.dimensionID == dim && this.x == x && this.y == y && this.z == z;
	}
	
	public void onUnclaims() {
		TerritoryIndex.getIndex().remove(this);
	}
	
	public boolean isAdjacent(IQuarter quarter) {
		for(int x = this.getFrom().xPosition; x<this.getFrom().xPosition+this.getXSize(); x++) {
			if(quarter.contains(this.getFrom().dimensionID, x, this.getFrom().zPosition-1) || quarter.contains(this.getFrom().dimensionID, x, this.getFrom().zPosition+this.getZSize())) {
				return true;
			}
		}
		for(int z = this.getFrom().zPosition; z<this.getFrom().zPosition+this.getZSize(); z++) {
			if(quarter.contains(this.getFrom().dimensionID, this.getFrom().xPosition-1,z) || quarter.contains(this.getFrom().dimensionID, this.getFrom().xPosition+this.getXSize(),z)) {
				return true;
			}
		}
		return false;
	}
	
	public Iterator<ForgeFactionChunk> getChunkIterator() {
		return new Iterator<ForgeFactionChunk>() {
			int x = from.xPosition, z = from.zPosition;
			@Override
			public boolean hasNext() {
				return z < from.zPosition+getZSize() && x<from.xPosition+getXSize();
			}

			@Override
			public ForgeFactionChunk next() {
				ForgeFactionChunk c = new ForgeFactionChunk(from.dimensionID, x,z);
				x++;
				if(x >= from.xPosition+getXSize()) {
					x = from.xPosition;
					z++;
				}
				return c;
			}

			@Override
			public void remove() {
				new RuntimeException("READ-ONLY iterator").printStackTrace();
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
	
	@Override
	public final String getName() {
		return StatCollector.translateToLocal("quarter."+getUnlocalizedName());
	}
	
	public abstract String getUnlocalizedName();
	
}
