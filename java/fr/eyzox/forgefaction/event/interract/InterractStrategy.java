package fr.eyzox.forgefaction.event.interract;

import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public interface InterractStrategy {
	public void onPlayerInterract(PlayerInteractEvent e);
	public IChatComponent getInstructions();
}
