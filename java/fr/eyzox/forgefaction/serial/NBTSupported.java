package fr.eyzox.forgefaction.serial;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTSupported {
	public void writeToNBT(NBTTagCompound tag);
	public void readFromNBT(NBTTagCompound tag); 
}
