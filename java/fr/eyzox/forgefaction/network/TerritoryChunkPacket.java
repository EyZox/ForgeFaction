package fr.eyzox.forgefaction.network;

import io.netty.buffer.ByteBuf;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;

public class TerritoryChunkPacket implements IMessage {
	
	private Set<ForgeFactionChunk> toAdd = new HashSet<ForgeFactionChunk>();
	private Set<ForgeFactionChunk> toRemove = new HashSet<ForgeFactionChunk>();
	
	public TerritoryChunkPacket() {
		toAdd = new HashSet<ForgeFactionChunk>();
		toRemove = new HashSet<ForgeFactionChunk>();
	}
	
	public TerritoryChunkPacket(Set<ForgeFactionChunk> toAdd, Set<ForgeFactionChunk> toRemove) {
		this.toAdd = toAdd;
		this.toRemove = toRemove;
	}
	
	public Set<ForgeFactionChunk> getToAdd() {
		return toAdd;
	}

	public void setToAdd(Set<ForgeFactionChunk> toAdd) {
		this.toAdd = toAdd;
	}

	public Set<ForgeFactionChunk> getToRemove() {
		return toRemove;
	}

	public void setToRemove(Set<ForgeFactionChunk> toRemove) {
		this.toRemove = toRemove;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound t = ByteBufUtils.readTag(buf);
		NBTTagList tagList = (NBTTagList) t.getTag("toAdd");
		if(tagList != null) {
			for(int i=0; i<tagList.tagCount(); i++) {
				ForgeFactionChunk ffchunk = new ForgeFactionChunk();
				ffchunk.readFromNBT(tagList.getCompoundTagAt(i));
				toAdd.add(ffchunk);
			}
		}
		
		tagList = (NBTTagList) t.getTag("toRemove");
		if(tagList != null) {
			for(int i=0; i<tagList.tagCount(); i++) {
				ForgeFactionChunk ffchunk = new ForgeFactionChunk();
				ffchunk.readFromNBT(tagList.getCompoundTagAt(i));
				toRemove.add(ffchunk);
			}
		}

	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound t = new NBTTagCompound();
		
		if(toAdd != null && !toAdd.isEmpty()) {
			NBTTagList tagList = new NBTTagList();
			for(ForgeFactionChunk ffchunk : toAdd) {
				NBTTagCompound ffchunkTag = new NBTTagCompound();
				ffchunk.writeToNBT(ffchunkTag);
				tagList.appendTag(ffchunkTag);
			}
			t.setTag("toAdd", tagList);
		}
		
		if(toRemove != null & !toRemove.isEmpty()) {
			NBTTagList tagList = new NBTTagList();
			for(ForgeFactionChunk ffchunk : toRemove) {
				NBTTagCompound ffchunkTag = new NBTTagCompound();
				ffchunk.writeToNBT(ffchunkTag);
				tagList.appendTag(ffchunkTag);
			}
			t.setTag("toRemove", tagList);
		}
		
		ByteBufUtils.writeTag(buf, t);

	}

}
