package fr.eyzox.forgefaction.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.eyzox.forgefaction.ForgeFactionMod;

public abstract class ModBlock extends Block {
	
	protected ModBlock(Material material) {
		super(material);
		setBlockName(this.getClass().getName());
	}
	
	 @Override
    public String getUnlocalizedName(){
        return ForgeFactionMod.MODID + ":" + super.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister){
        blockIcon = iconRegister.registerIcon(ForgeFactionMod.MODID+":"+getBlockName());
    }
    
    public String getBlockName() {
    	return super.getUnlocalizedName().substring(5);
    }
}
