package fr.eyzox.forgefaction.event.interract;

import net.minecraft.block.Block;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import fr.eyzox.forgefaction.ForgeFactionData;
import fr.eyzox.forgefaction.block.AbstractQuarterBlock;
import fr.eyzox.forgefaction.block.HeadquarterBlock;
import fr.eyzox.forgefaction.exception.AlreadyChildException;
import fr.eyzox.forgefaction.exception.AlreadyClaimedException;
import fr.eyzox.forgefaction.exception.AlreadyParentException;
import fr.eyzox.forgefaction.exception.ForgeFactionException;
import fr.eyzox.forgefaction.exception.NoAdjacentChunkException;
import fr.eyzox.forgefaction.faction.Faction;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;
import fr.eyzox.forgefaction.territory.quarter.AbstractQuarter;
import fr.eyzox.forgefaction.territory.quarter.HeadQuarter;
import fr.eyzox.forgefaction.territory.quarter.Quarter;

public class ClaimInterract implements InterractStrategy {

	private AbstractQuarter from;
	private boolean quit;

	/** Called when player interract
	 * Assert player has a faction and has permission : @see fr.eyzox.forgefaction.command.Claim 
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
					this.from = ForgeFactionData.getData().getIndex().getAbstractQuarter(e.world.getChunkFromBlockCoords(e.x, e.z));
					//If from == null : zone is wilderness
					if(this.from == null) {
						if(abstractQuarterBlock instanceof HeadquarterBlock) {
							HeadQuarter hq = (HeadQuarter) abstractQuarterBlock.createAbstractQuarter(e.world, e.x, e.y, e.z);
							try {
								playerProperties.getFaction().claims(hq, ForgeFactionData.getData().getIndex());
								playerProperties.setInterractStrategy(null);
								playerProperties.getFaction().sendMessage(new ChatComponentText(e.entityPlayer.getDisplayName()+" has claimed new territory "+hq.printCoordinates()));
							} catch (ForgeFactionException e1) {
								e1.printStackTrace();
								e.entityPlayer.addChatComponentMessage(new ChatComponentText(e1.getMessage()));
							}
						}else {
							e.entityPlayer.addChatComponentMessage(new ChatComponentText("You must choose a quarter block from your faction first or select a headquarter block"));
						}
					}else if(from.getFaction() == playerProperties.getFaction() && from.isQuarterBlock(e.world, e.x, e.y, e.z)) {
						//Here the player selected a valid AbstractQuarterBlock claimed by his faction
						e.entityPlayer.addChatComponentMessage(new ChatComponentText("You've selected "+this.from.printCoordinates()+" as source. Now select the new territory you want claim or right click on this block again to choose an other source"));
					}else {
						this.from = null;
					}
				}else if(from.isQuarterBlock(e.world, e.x, e.y, e.z)) {
					//Cancel the selection
					this.from = null;
					e.entityPlayer.addChatComponentMessage(new ChatComponentText("Right-Click on a quarter block to define it as source"));
				}else {
					//STEP 2 : Choose a AbstractQuarterBlock to claim
					
					if(abstractQuarterBlock instanceof HeadquarterBlock) {
						e.entityPlayer.addChatComponentMessage(new ChatComponentText("A territory could only have 1 headquarter. However, you could have as headquarter as you want as source"));
					}else { //Quarter
						Quarter newTerritory = (Quarter) abstractQuarterBlock.createAbstractQuarter(e.world, e.x, e.y, e.z);
						try {
							from.claims(newTerritory);
							playerProperties.setInterractStrategy(null);
							playerProperties.getFaction().sendMessage(new ChatComponentText(e.entityPlayer.getDisplayName()+" has claimed new territory "+newTerritory.printCoordinates()));
						} catch (ForgeFactionException e1) {
							e1.printStackTrace();
							e.entityPlayer.addChatComponentMessage(new ChatComponentText(e1.getMessage()));
						}
					}
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
