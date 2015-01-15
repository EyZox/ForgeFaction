package fr.eyzox.forgefaction.territory.quarter;

import java.util.Collection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import fr.eyzox.forgefaction.data.ForgeFactionData;
import fr.eyzox.forgefaction.exception.AlreadyChildException;
import fr.eyzox.forgefaction.exception.AlreadyClaimedException;
import fr.eyzox.forgefaction.exception.AlreadyParentException;
import fr.eyzox.forgefaction.exception.NoAdjacentChunkException;
import fr.eyzox.forgefaction.faction.Faction;
import fr.eyzox.forgefaction.territory.TerritoryAccess;
import fr.eyzox.forgefaction.territory.TerritoryIndex;

public class Quarter extends AbstractQuarter {

	private AbstractQuarter parent;
	private Quarter child;
	
	public Quarter(World w, int x, int y, int z) {super(w,x,y,z);}
	public Quarter(int dim, int x, int y, int z) {super(dim, x, y, z);}
	public Quarter(NBTTagCompound tag) {super(tag);}
	
	@Override
	public int getSize() {
		return 3;
	}
	
	public AbstractQuarter getParent() {
		return parent;
	}
	protected void setParent(AbstractQuarter parent) {
		this.parent = parent;
	}
	public Quarter getChild() {
		return child;
	}
	
	public void claims(Quarter child, TerritoryAccess access) throws AlreadyChildException, AlreadyParentException, NoAdjacentChunkException, AlreadyClaimedException{
		if(this.child != null) throw new AlreadyChildException(this, child);
		if(child.getParent() != null) throw new AlreadyParentException(child);
		if(!this.isAdjacent(child)) throw new NoAdjacentChunkException(this, child);
		Collection<AbstractQuarter> conflicts = access.checkConflicts(child);
		if(!conflicts.isEmpty()) throw new AlreadyClaimedException(conflicts, child);
		
		this.child = child;
		this.child.setParent(this);
		ForgeFactionData.getData().markDirty();
		TerritoryIndex.getIndex().add(child);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if(child != null) {
			NBTTagCompound childTag = new NBTTagCompound();
			child.writeToNBT(childTag);
			tag.setTag("child", childTag);
		}
	}
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagCompound childTag = (NBTTagCompound) tag.getTag("child");
		if(childTag != null) {
			child = new Quarter(childTag);
			child.setParent(this);
		}
	}
	@Override
	public Faction getFaction() {
		return parent.getFaction();
		
	}
	@Override
	public void onUnclaims() {
		super.onUnclaims();
		if(this.child != null) this.child.onUnclaims();
		
	}
	@Override
	public String getName() {
		return "quarter";
	}

}
