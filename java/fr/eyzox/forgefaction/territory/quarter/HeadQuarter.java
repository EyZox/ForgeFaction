package fr.eyzox.forgefaction.territory.quarter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgefaction.data.ForgeFactionData;
import fr.eyzox.forgefaction.exception.AlreadyClaimedException;
import fr.eyzox.forgefaction.exception.NoAdjacentChunkException;
import fr.eyzox.forgefaction.faction.Faction;
import fr.eyzox.forgefaction.territory.AbstractQuarter;
import fr.eyzox.forgefaction.territory.IParentMultipleQuarters;
import fr.eyzox.forgefaction.territory.IQuarter;
import fr.eyzox.forgefaction.territory.TerritoryAccess;
import fr.eyzox.forgefaction.territory.TerritoryIndex;

public class HeadQuarter extends AbstractQuarter implements IParentMultipleQuarters<QuarterBase>{

	private List<QuarterBase> quarters = new ArrayList<QuarterBase>();
	private Faction team;
	
	public HeadQuarter(World w, int x, int y, int z) {super(w,x,y,z);}
	public HeadQuarter(int dim, int x, int y, int z) {super(dim, x, y, z);}
	public HeadQuarter(NBTTagCompound tag) {super(tag);}

	
	public void setFaction(Faction team) {
		this.team = team;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if(!quarters.isEmpty()) {
			NBTTagList quarterList = new NBTTagList();
			for(QuarterBase quarter : quarters) {
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
				QuarterBase q = new QuarterBase(quarterList.getCompoundTagAt(i));
				q.setParent(this);
				quarters.add(q);
			}
		}
	}
	
	@Override
	public String getName() {
		return "Headquarter";
	}
	
	@Override
	public Faction getFaction() {
		return team;
	}
	
	@Override
	public int getSize() {
		return 2;
	}
	
	@Override
	public void onUnclaims() {
		super.onUnclaims();
		for(QuarterBase quarter : quarters) {
			quarter.onUnclaims();
		}
	}
	
	@Override
	public void claims(QuarterBase quarter, TerritoryAccess access) throws NoAdjacentChunkException, AlreadyClaimedException {
		if(!isAdjacent(quarter)) throw new NoAdjacentChunkException(this, quarter);
		
		Collection<IQuarter> conflicts = access.checkConflicts(quarter);
		if(conflicts.isEmpty()) throw new AlreadyClaimedException(conflicts, quarter);
		
		quarters.add(quarter);
		ForgeFactionData.getData().markDirty();
		TerritoryIndex.getIndex().add(quarter);
	}
	
	@Override
	public Collection<QuarterBase> getChilds() {
		return quarters;
	}
	
	@Override
	public void unclaims(QuarterBase quarter) {
		if(quarters.remove(quarter)) {
			ForgeFactionData.getData().markDirty();
			quarter.onUnclaims();
		}
	}

}
