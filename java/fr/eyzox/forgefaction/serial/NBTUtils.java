package fr.eyzox.forgefaction.serial;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;

public class NBTUtils {

	public static void write(NBTTagCompound tag, Chunk c) {
		tag.setIntArray("chunk", new int[] {c.worldObj.provider.dimensionId, c.xPosition, c.zPosition});
	}
	
	public static Chunk readChunk(NBTTagCompound tag) {
		int[] t = tag.getIntArray("chunk");
		if(t.length == 3) {
			World w = DimensionManager.getWorld(t[0]);
			if(w == null) return null;
			return w.getChunkFromChunkCoords(t[1], t[2]);
		}else {
			return null;
		}
	}
	
	public static void write(NBTTagCompound tag, UUID uuid) {
		 tag.setLong("UUIDMost", uuid.getMostSignificantBits());
         tag.setLong("UUIDLeast", uuid.getLeastSignificantBits());
	}
	
	public static UUID readUUID(NBTTagCompound tag) {
		return new UUID(tag.getLong("UUIDMost"), tag.getLong("UUIDLeast"));
	}
	
}
