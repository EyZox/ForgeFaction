package fr.eyzox.forgefaction.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLLog;
import fr.eyzox.forgefaction.ForgeFactionMod;
import fr.eyzox.forgefaction.faction.Faction;
import fr.eyzox.forgefaction.territory.TerritoryIndex;

public class ForgeFactionData extends WorldSavedData {
	
	public static final String KEY = ForgeFactionMod.MODID+":Data";
	
	private Factions factions = new Factions();
	
	public ForgeFactionData(String key) {
		super(KEY);
	}

	public static ForgeFactionData getData() {
		MapStorage storage = DimensionManager.getWorlds()[0].mapStorage;
		ForgeFactionData result = (ForgeFactionData)storage.loadData(ForgeFactionData.class, KEY);
		if (result == null) {
			result = new ForgeFactionData(KEY);
			storage.setData(KEY, result);
			FMLLog.info("["+ForgeFactionMod.MODID+"]Unable to find data, new data created");
		}
		return result;
	}
	
	
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		NBTTagList teamList = (NBTTagList) tag.getTag("teams");
		for(int i=0; i<teamList.tagCount(); i++) {
			factions.factionSet.add(new Faction(teamList.getCompoundTagAt(i)));
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagList teamList = new NBTTagList();
		for(Faction team : factions.getFactions()) {
			NBTTagCompound teamTag = new NBTTagCompound();
			team.writeToNBT(teamTag);
			teamList.appendTag(teamTag);
		}
		tag.setTag("teams", teamList);
	}
	
	public Factions getFactions() { return factions; }

}
