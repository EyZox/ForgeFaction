package fr.eyzox.forgefaction.event.interract;

import net.minecraft.block.Block;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import fr.eyzox.forgefaction.ForgeFactionData;
import fr.eyzox.forgefaction.block.AbstractQuarterBlock;
import fr.eyzox.forgefaction.block.HeadquarterBlock;
import fr.eyzox.forgefaction.exception.AlreadyClaimedException;
import fr.eyzox.forgefaction.player.ForgeFactionPlayerProperties;
import fr.eyzox.forgefaction.team.Faction;
import fr.eyzox.forgefaction.territory.AbstractQuarter;
import fr.eyzox.forgefaction.territory.HeadQuarter;

public class ClaimInterract implements InterractStrategy {

	private AbstractQuarter from, to;
	private boolean quit;


	/*
	@Override
	public void onPlayerInterract(PlayerInteractEvent e) {
		/*
	 * Assert player has a faction and has permission : @see fr.eyzox.forgefaction.command.Claim 
	 */
	/*
		if(e.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
			if(quit) {
				ForgeFactionPlayerProperties.get(e.entityPlayer).setInterractStrategy(null);
			}else {
				quit = true;
			}
		}

		else if(e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			quit = false;
			AbstractQuarterBlock abstractQuarterBlock = getAbstractQuarter(e);

			if(abstractQuarterBlock == null) return;

			ForgeFactionPlayerProperties playerProperties = ForgeFactionPlayerProperties.get(e.entityPlayer);
			Chunk chunk = e.world.getChunkFromBlockCoords(e.x, e.z);
			if(from == null) {

				this.from = ForgeFactionData.getData().getIndex().getAbstractQuarter(chunk);
				if(this.from == null) { //Check if zone is wilderness

					if(abstractQuarterBlock instanceof HeadquarterBlock) {
						//Claim this zone
						HeadQuarter hq = (HeadQuarter) abstractQuarterBlock.createAbstractQuarter(e.world, e.x, e.y, e.z);
						playerProperties.getFaction().claims(hq);
						playerProperties.setInterractStrategy(null);
						playerProperties.getFaction().sendMessage(new ChatComponentText(e.entityPlayer.getDisplayName()+" has claimed new territory "+hq.printCoordinates()));
					}else {
						e.entityPlayer.addChatComponentMessage(new ChatComponentText("You must choose a quarter block from your faction first"));
					}

				}else if(this.from.isQuarterBlock(e.world, e.x, e.y, e.z) && this.from.getFaction() == playerProperties.getFaction()){
					e.entityPlayer.addChatComponentMessage(new ChatComponentText("You've selected "+this.from.printCoordinates()+" as source. Now select the new territory you want claim or right click on this block again to choose an other source"));
				}
			}else {
				AbstractQuarter newTerritory = ForgeFactionData.getData().getIndex().getAbstractQuarter(chunk);
				if(newTerritory == null) {
					if(abstractQuarterBlock instanceof HeadquarterBlock) {
						e.entityPlayer.addChatComponentMessage(new ChatComponentText("A territory could only have 1 headquarter. However, you could have as headquarter as you want as source"));
					}else { //Forcement un Quarter
						newTerritory = abstractQuarterBlock.createAbstractQuarter(e.world, e.x, e.y, e.z);

					}
				}else if(newTerritory == this.from && this.from.isQuarterBlock(e.world, e.x, e.y, e.z)){
					this.from = null;
					e.entityPlayer.addChatComponentMessage(new ChatComponentText("Right-Click on a quarter block to define it as source"));
				}else {
					e.entityPlayer.addChatComponentMessage(new ChatComponentText("this area is already claimed"));
				}
			}

		}
	}
	 */

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
				Chunk selectedChunk = e.world.getChunkFromBlockCoords(e.x, e.z);
				//step 1 : select a source
				if(from == null) {
					this.from = ForgeFactionData.getData().getIndex().getAbstractQuarter(selectedChunk);
					//If from == null : zone is wilderness
					if(this.from == null) {
						if(abstractQuarterBlock instanceof HeadquarterBlock) {
							HeadQuarter hq = (HeadQuarter) abstractQuarterBlock.createAbstractQuarter(e.world, e.x, e.y, e.z);
							try {
								playerProperties.getFaction().claims(hq, ForgeFactionData.getData().getIndex());
								playerProperties.setInterractStrategy(null);
								playerProperties.getFaction().sendMessage(new ChatComponentText(e.entityPlayer.getDisplayName()+" has claimed new territory "+hq.printCoordinates()));
							} catch (AlreadyClaimedException e1) {
								e1.printStackTrace();
								e.entityPlayer.addChatComponentMessage(new ChatComponentText("this area is already claimed"));
							}
						}else {
							e.entityPlayer.addChatComponentMessage(new ChatComponentText("You must choose a quarter block from your faction first or select a headquarter block"));
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
