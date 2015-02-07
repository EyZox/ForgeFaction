package fr.eyzox.forgefaction.event.interract;

import net.minecraft.block.Block;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import fr.eyzox.forgefaction.block.AbstractQuarterBlock;
import fr.eyzox.forgefaction.block.HeadquarterBlock;
import fr.eyzox.forgefaction.exception.ForgeFactionException;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;
import fr.eyzox.forgefaction.territory.ForgeFactionChunk;
import fr.eyzox.forgefaction.territory.IParentQuarter;
import fr.eyzox.forgefaction.territory.IQuarter;
import fr.eyzox.forgefaction.territory.TerritoryIndex;
import fr.eyzox.forgefaction.territory.quarter.Headquarter;
import fr.eyzox.forgefaction.territory.quarter.QuarterBase;

public class ClaimInterract implements InterractStrategy {

	private IParentQuarter from;
	private boolean quit;

	/** Called when player interract
	 * Assert player has a faction and has permission : @see fr.eyzox.forgefaction.command.ClaimCommand
	 */
	@Override
	public void onPlayerInterract(PlayerInteractEvent e) {
		if(e.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
			if(quit) {
				ForgeFactionPlayerProperties.get(e.entityPlayer).setInterractStrategy(null);
			}else {
				quit = true;
			}
		}

		else if(e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			quit = false;

			//Test if player click on a AbstractQuarterBlock
			AbstractQuarterBlock abstractQuarterBlock = getAbstractQuarter(e);
			if(abstractQuarterBlock == null) return;

			///////SERVER ONLY///////
			if(!e.world.isRemote) {
				ForgeFactionPlayerProperties playerProperties = ForgeFactionPlayerProperties.get(e.entityPlayer);
				//step 1 : select a source
				if(from == null) {
					IQuarter unverifiedFrom = TerritoryIndex.getIndex().getIQuarter(new ForgeFactionChunk(e.world.provider.dimensionId, ForgeFactionChunk.getChunkPosition(e.x), ForgeFactionChunk.getChunkPosition(e.z)));
					//If unverifiedFrom == null : zone is wilderness
					if(unverifiedFrom == null) {
						if(abstractQuarterBlock instanceof HeadquarterBlock) {
							Headquarter hq = (Headquarter) abstractQuarterBlock.createAbstractQuarter(e.world, e.x, e.y, e.z);
							try {
								playerProperties.getFaction().claims(hq, TerritoryIndex.getIndex());
								playerProperties.setInterractStrategy(null);
								playerProperties.getFaction().sendMessage(new ChatComponentText(e.entityPlayer.getDisplayName()+" has claimed new territory "+hq.toString()));
							} catch (ForgeFactionException e1) {
								e1.printStackTrace();
								e.entityPlayer.addChatComponentMessage(new ChatComponentText(e1.getMessage()));
							}
						}else {
							e.entityPlayer.addChatComponentMessage(new ChatComponentText("You must choose a quarter block from your faction first or select a headquarter block"));
						}
					}else {
						if(!(unverifiedFrom instanceof IParentQuarter)) {
							e.entityPlayer.addChatComponentMessage(new ChatComponentText("You can't use this block as source"));
							return;
						}

						this.from = (IParentQuarter) unverifiedFrom;
						if(from.getFaction() == playerProperties.getFaction() && from.isTheBlock(e.world.provider.dimensionId, e.x, e.y, e.z)) {
							//Here the player selected a valid AbstractQuarterBlock claimed by his faction
							e.entityPlayer.addChatComponentMessage(new ChatComponentText("You've selected "+this.from.toString()+" as source. Now select the new territory you want claim or right click on this block again to choose an other source"));
						}else {
							this.from = null;
						}
					}
				}else if(from.isTheBlock(e.world.provider.dimensionId, e.x, e.y, e.z)) {
					//Cancel the selection
					this.from = null;
					e.entityPlayer.addChatComponentMessage(new ChatComponentText("Right-Click on a quarter block to define it as source"));
				}else {
					//STEP 2 : Choose a AbstractQuarterBlock to claim

					if(abstractQuarterBlock instanceof HeadquarterBlock) {
						e.entityPlayer.addChatComponentMessage(new ChatComponentText("A territory could only have 1 headquarter. However, you could have as headquarter as you want as source"));
					}else { //Quarter
						QuarterBase newTerritory = (QuarterBase) abstractQuarterBlock.createAbstractQuarter(e.world, e.x, e.y, e.z);
						try {
							from.claims(newTerritory, TerritoryIndex.getIndex());
							playerProperties.getFaction().sendMessage(new ChatComponentText(e.entityPlayer.getDisplayName()+" has claimed new territory "+newTerritory.toString()));
						} catch (ForgeFactionException e1) {
							e1.printStackTrace();
							e.entityPlayer.addChatComponentMessage(new ChatComponentText(e1.getMessage()));
						}
					}
					playerProperties.setInterractStrategy(null);
				}
			}
			/////////////////////////
		}
	}

	private AbstractQuarterBlock getAbstractQuarter(PlayerInteractEvent e) {
		Block block = e.world.getBlock(e.x, e.y, e.z);
		if(block instanceof AbstractQuarterBlock) {
			return (AbstractQuarterBlock) block;
		}
		return null;
	}

	@Override
	public IChatComponent getInstructions() {
		return new ChatComponentText("Claiming mode : Right-Click on a quarter block to define it as source, 2 consecutives left-click to cancel");
	}

}
