package fr.eyzox.forgefaction.creativetabs;

import fr.eyzox.forgefaction.ForgeFactionMod;
import fr.eyzox.forgefaction.block.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ForgeFactionBlocksTab extends CreativeTabs {
	
	private static ForgeFactionBlocksTab instance;
	
	static {
		instance = new ForgeFactionBlocksTab();
	}
	
	public ForgeFactionBlocksTab(int index) {
		super(index, ForgeFactionMod.MODID+" Blocks");
		instance = this;
	}

	public ForgeFactionBlocksTab() {
		super(ForgeFactionMod.MODID+" Blocks");
		instance = this;
	}

	@Override
	public Item getTabIconItem() {
		return Item.getItemFromBlock(ModBlocks.headquarterBlock);
	}
	
	public static ForgeFactionBlocksTab getInstance() {
		return instance;
	}

}
