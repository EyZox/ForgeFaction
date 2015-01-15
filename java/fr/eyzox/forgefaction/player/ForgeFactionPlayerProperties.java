package fr.eyzox.forgefaction.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import fr.eyzox.forgefaction.ForgeFactionMod;
import fr.eyzox.forgefaction.data.ForgeFactionData;
import fr.eyzox.forgefaction.event.interract.InterractStrategy;
import fr.eyzox.forgefaction.faction.Faction;

public class ForgeFactionPlayerProperties implements IExtendedEntityProperties {

	public final static String EXT_PROP_NAME = ForgeFactionMod.MODID+":PlayerProperties";

	private EntityPlayer player;
	
	private Faction faction;
	private InterractStrategy interractStrategy;

	public ForgeFactionPlayerProperties() {}
	public ForgeFactionPlayerProperties(EntityPlayer player){
		this.player = player;
	}

	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(EXT_PROP_NAME, new ForgeFactionPlayerProperties(player));
	}

	public static final ForgeFactionPlayerProperties get(EntityPlayer player) {
		return (ForgeFactionPlayerProperties) player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		if(faction != null) compound.setString("forgefaction-team", faction.getName());

	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		String teamName = compound.getString("forgefaction-team");
		if(teamName.length() > 0) {
			this.faction = ForgeFactionData.getData().getFactions().get(teamName);
		}

	}

	@Override
	public void init(Entity entity, World world) {}
	
	public Faction getFaction() {
		return faction;
	}
	public void setFaction(Faction faction) {
		this.faction = faction;
	}
	public EntityPlayer getPlayer() {
		return player;
	}
	public InterractStrategy getInterractStrategy() {
		return interractStrategy;
	}
	public void setInterractStrategy(InterractStrategy interractStrategy) {
		if(this.interractStrategy != interractStrategy) {
			this.interractStrategy = interractStrategy;
			if(interractStrategy != null) {
				IChatComponent message = interractStrategy.getInstructions();
				if(message != null) player.addChatComponentMessage(message);
			}else {
				player.addChatComponentMessage(new ChatComponentText("You are back to normal mode"));
			}
		}
	}
}
