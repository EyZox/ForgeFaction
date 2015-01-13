package fr.eyzox.forgefaction.territory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import fr.eyzox.forgefaction.ForgeFactionData;
import fr.eyzox.forgefaction.exception.AlreadyClaimedException;
import fr.eyzox.forgefaction.exception.NoAdjacentChunkException;
import fr.eyzox.forgefaction.team.Faction;

public class HeadQuarter extends AbstractQuarter {

	private List<Quarter> quarters = new ArrayList<Quarter>();
	private Faction team;
	
	public HeadQuarter(World w, int x, int y, int z) {
		super(w,x,y,z);
	}
	public HeadQuarter(NBTTagCompound tag) {
		worldLoader = DimensionManager.getWorld(tag.getInteger("dim"));
		readFromNBT(tag);
		worldLoader = null;
	}

	@Override
	public int getSize() {
		return 6;
	}
	
	@Override
	public Faction getFaction() {
		return team;
	}
	public void setFaction(Faction team) {
		this.team = team;
	}
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if(!quarters.isEmpty()) {
			NBTTagList quarterList = new NBTTagList();
			for(Quarter quarter : quarters) {
				NBTTagCompound quarterTag = new NBTTagCompound();
				quarter.writeToNBT(quarterTag);
				quarterList.appendTag(quarterTag);
			}
			tag.setTag("quarters", quarterList);
		}
	}
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagList quarterList = (NBTTagList) tag.getTag("quarters");
		if(quarterList != null) {
			for(int i=0; i<quarterList.tagCount(); i++) {
				Quarter q = new Quarter(quarterList.getCompoundTagAt(i));
				q.setParent(this);
				quarters.add(q);
			}
		}
	}
	
	public Collection<Quarter> getQuarters() {
		return quarters;
	}
	
	public void claimsNEW(Quarter quarter) throws NoAdjacentChunkException, AlreadyClaimedException {
		this.claims(quarter, ForgeFactionData.getData().getFactions());
	}
	
	public void claims(Quarter quarter, TerritoryAccess access) throws NoAdjacentChunkException, AlreadyClaimedException {
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
		if(!adjacent) throw new NoAdjacentChunkException(this, quarter);
		
		Collection<AbstractQuarter> conflicts = access.checkConflicts(quarter);
		if(conflicts.isEmpty()) throw new AlreadyClaimedException(conflicts, quarter);
		
		quarters.add(quarter);
		ForgeFactionData.getData().markDirty();
		if(quarter.getChunk().isChunkLoaded)
			ForgeFactionData.getData().getIndex().add(quarter);
	}
	
	public void unclaims(Quarter quarter) {
		if(quarters.remove(quarter)) {
			ForgeFactionData.getData().markDirty();
			quarter.onUnclaims();
		}
	}
	@Override
	public void onUnclaims() {
		super.onUnclaims();
		for(Quarter quarter : quarters) {
			quarter.onUnclaims();
		}
	}

}