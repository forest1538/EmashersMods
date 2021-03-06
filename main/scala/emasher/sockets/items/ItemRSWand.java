package emasher.sockets.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemRSWand extends Item
{

	public ItemRSWand(int id)
	{
		super(id);
		
		this.setCreativeTab(SocketsMod.tabSockets);
		this.setMaxStackSize(1);
		this.setUnlocalizedName("eng_rsWand");
		this.setMaxDamage(64);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir)
	{
		this.itemIcon = ir.registerIcon("sockets:redstoneStaff");
	}
	
	@Override
	public boolean isItemTool(ItemStack par1ItemStack)
    {
		return true;
    }
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        int i1 = par3World.getBlockId(par4, par5, par6);

        if (i1 == Block.snow.blockID && (par3World.getBlockMetadata(par4, par5, par6) & 7) < 1)
        {
            par7 = 1;
        }
        else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID
                && (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(par3World, par4, par5, par6)))
        {
            if (par7 == 0)
            {
                --par5;
            }

            if (par7 == 1)
            {
                ++par5;
            }

            if (par7 == 2)
            {
                --par6;
            }

            if (par7 == 3)
            {
                ++par6;
            }

            if (par7 == 4)
            {
                --par4;
            }

            if (par7 == 5)
            {
                ++par4;
            }
        }

        if (par1ItemStack.stackSize == 0)
        {
            return false;
        }
        else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
        {
            return false;
        }
        else if (par5 == 255 && SocketsMod.tempRS.blockMaterial.isSolid())
        {
            return false;
        }
        else if (par3World.canPlaceEntityOnSide(SocketsMod.socket.blockID, par4, par5, par6, false, par7, par2EntityPlayer, par1ItemStack))
        {
            Block block = SocketsMod.tempRS;
            int j1 = 0;
            int k1 = SocketsMod.tempRS.onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, j1);

            if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, k1))
            {
                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                par1ItemStack.damageItem(1, par2EntityPlayer);
            }

            return true;
        }
        else
        {
            return false;
        }
    }
	
	 public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	    {
	       if (!world.setBlock(x, y, z, SocketsMod.tempRS.blockID, metadata, 3))
	       {
	           return false;
	       }

	       if (world.getBlockId(x, y, z) == SocketsMod.tempRS.blockID)
	       {
	           Block.blocksList[SocketsMod.tempRS.blockID].onBlockPlacedBy(world, x, y, z, player, stack);
	           Block.blocksList[SocketsMod.tempRS.blockID].onPostBlockPlaced(world, x, y, z, metadata);
	       }

	       return true;
	    }

}
