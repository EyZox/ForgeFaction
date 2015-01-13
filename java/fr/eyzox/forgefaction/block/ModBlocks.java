package fr.eyzox.forgefaction.block;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
	public static final HeadquarterBlock headquarterBlock = new HeadquarterBlock();
	public static final QuarterBlock quarterBlock = new QuarterBlock();

    public static void registerBlock(){
       GameRegistry.registerBlock(headquarterBlock, headquarterBlock.getBlockName());
       GameRegistry.registerBlock(quarterBlock, quarterBlock.getBlockName());
    }
}
