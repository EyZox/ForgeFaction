package fr.eyzox.forgefaction.faction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.chunk.Chunk;
import fr.eyzox.forgefaction.ForgeFactionData;
import fr.eyzox.forgefaction.exception.AlreadyClaimedException;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;
import fr.eyzox.forgefaction.serial.NBTSupported;
import fr.eyzox.forgefaction.territory.TerritoryAccess;
import fr.eyzox.forgefaction.territory.quarter.AbstractQuarter;
import fr.eyzox.forgefaction.territory.quarter.HeadQuarter;

public class Faction implements NBTSupported {

	private String name;
	/* Maybe when UUID will become an identifier
	private Set<UUID> players;
	 */
	private Set<String> players;
	private List<HeadQuarter> headquarters;

	public Faction() {
		/* Maybe when UUID will become an identifier
		players = new HashSet<UUID>();
		 */
		players = new HashSet<String>();
		headquarters = new ArrayList<HeadQuarter>();
	}
	public Faction(String name) {
		this();
		this.name = name;
	}
	public Faction(NBTTagCompound tag) {
		this();
		this.readFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setString("team-name", name);

		NBTTagList playerList = new NBTTagList();
		/* Maybe when UUID will become an identifier
		for(UUID player : players) {
			NBTTagCompound playerTag = new NBTTagCompound();
			NBTUtils.write(playerTag, player);
			playerList.appendTag(playerTag);
		}*/
		for(String player : players) {
			playerList.appendTag(new NBTTagString(player));
		}

		tag.setTag("players", playerList);

		NBTTagList headquarterList = new NBTTagList();
		for(HeadQuarter headquarter : headquarters) {
			NBTTagCompound headquarterTag = new NBTTagCompound();
			headquarter.writeToNBT(headquarterTag);
			headquarterList.appendTag(headquarterTag);
		}
		tag.setTag("headquarters", headquarterList);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		this.name = tag.getString("team-name");

		NBTTagList tagList = (NBTTagList) tag.getTag("players");
		for(int i=0; i<tagList.tagCount(); i++) {
			/* Maybe when UUID will become an identifier
			players.add(NBTUtils.readUUID(tagList.getCompoundTagAt(i)));
			 */
			players.add(tagList.getStringTagAt(i));
		}

		tagList = (NBTTagList) tag.getTag("headquarters");
		for(int i=0; i<tagList.tagCount(); i++) {
			HeadQuarter hq = new HeadQuarter(tagList.getCompoundTagAt(i));
			hq.setFaction(this);
			headquarters.add(hq);
		}

	}

	public String getName() {
		return name;
	}
	public boolean setName(String name) {
		if(name.equals(this.name) || ForgeFactionData.getData().getFactions().contains(name)) return false;
		ForgeFactionData.getData().getFactions().remove(this);
		this.name = name;
		ForgeFactionData.getData().getFactions().add(this);
		ForgeFactionData.getData().markDirty();
		return true;
	}

	public Collection<HeadQuarter> getHeadquarters() {
		return headquarters;
	}

	public boolean addPlayer(EntityPlayer player) {
		/* Maybe when UUID will become an identifier
		boolean b = players.add(player.getUniqueID());
		if(b) {
			ForgeFactionPlayerProperties.get(player).setTeam(this);
			ForgeFactionMod.getInstance().getData().markDirty();
		}
		 */
		boolean b = players.add(player.getDisplayName());
		if(b) {
			ForgeFactionPlayerProperties.get(player).setFaction(this);
			ForgeFactionData.getData().markDirty();
		}
		return b;

	}

	public boolean removePlayer(EntityPlayer player) {
		/* Maybe when UUID will become an identifier
		if(players.remove(player.getUniqueID())) ForgeFactionMod.getInstance().getData().markDirty();
		 */

		if(removePlayer(player.getDisplayName())) {
			ForgeFactionPlayerProperties.get(player).setFaction(null);
			return true;
		}
		return false;
	}

	public boolean removePlayer(String playerName) {
		if(players.remove(playerName)) {
			ForgeFactionData.getData().markDirty();
			return true;
		}
		return false;
	}

	public int getNbPlayer() {
		return players.size();
	}
	
	public void claims(HeadQuarter quarter) throws AlreadyClaimedException {
		this.claims(quarter, ForgeFactionData.getData().getFactions());
	}

	public void claims(HeadQuarter quarter, TerritoryAccess access) throws AlreadyClaimedException {
			Collection<AbstractQuarter> conflicts = access.checkConflicts(quarter);
			if(!conflicts.isEmpty()) throw new AlreadyClaimedException(conflicts, quarter);
			
			this.headquarters.add(quarter);
			ForgeFactionData.getData().markDirty();
			ForgeFactionData.getData().getIndex().add(quarter);
				
	}

	public void unclaims(HeadQuarter headquarter) {
		if(this.headquarters.remove(headquarter)) {
			headquarter.onUnclaims();
			ForgeFactionData.getData().markDirty();
		}
	}

	public Collection<EntityPlayer> getOnlinePlayers() {
		Set<EntityPlayer> onlinePlayers = new HashSet<EntityPlayer>();
		for(String playerName : players) {
			EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
			if(player != null) onlinePlayers.add(player);
		}
		return onlinePlayers;
	}

	public void sendMessage(IChatComponent message) {
		for(EntityPlayer player : getOnlinePlayers()) {
			player.addChatComponentMessage(message);
		}
	}

}
