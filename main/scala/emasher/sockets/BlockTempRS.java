package emasher.sockets;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SocketModule;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockTempRS extends BlockContainer
{

	public BlockTempRS(int id)
	{
		super(id, Material.redstoneLight);
		setBlockBounds(0.4F, 0.4F, 0.4F, 0.6F, 0.6F, 0.6F);
		this.setCreativeTab(null);
		this.setUnlocalizedName("tempRS");
		this.setLightValue(5.0F);
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		blockIcon = ir.registerIcon("sockets:tempRS");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileTempRS();
	}
	
	@Override
	public boolean hasTileEntity()
	{
		return true;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public int getRenderBlockPass()
    {
        return 0;
    }

	@Override
	public boolean canBeReplacedByLeaves(World world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
		return 15;
    }
	
	@Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
		return 15;
    }
	
	@Override
	public boolean canProvidePower()
    {
        return true;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
		double d0 = (double)par2 + 0.5D + ((double)par5Random.nextFloat() - 0.5D) * 0.2D;
        double d1 = (double)((float)par3 + 0.0625F);
        double d2 = (double)par4 + 0.5D + ((double)par5Random.nextFloat() - 0.5D) * 0.2D;
        float f = 1.0F;
        float f1 = f * 0.6F + 0.4F;

        f1 = 0.0F;

        float f2 = f * f * 0.7F - 0.5F;
        float f3 = f * f * 0.6F - 0.7F;

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        par1World.spawnParticle("reddust", d0, d1 + 0.7F, d2, (double)f1, (double)f2, (double)f3);
    }
	
}
