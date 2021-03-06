package emasher.sockets.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemSocketRemote extends Item
{
	@SideOnly(Side.CLIENT)
	public Icon[] textures;

	public ItemSocketRemote(int id)
	{
		super(id);
		this.setCreativeTab(SocketsMod.tabSockets);
		this.setMaxStackSize(1);
		this.setUnlocalizedName("socket_remote");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir)
	{
		String cb = "";
		if(SocketsMod.cbTextures) cb = "cb";
		
		textures = new Icon[7];
		for(int i = 0; i < 7; i++)
		{
			textures[i] =  ir.registerIcon("sockets:remote_" + i + cb);
		}
		
		itemIcon = textures[0];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int damage)
	{
		return textures[damage];
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
    {
		if(player.isSneaking() && ! world.isRemote)
		{
			item.setItemDamage(item.getItemDamage() + 1);
			if(item.getItemDamage() == 7) item.setItemDamage(0);
		}
		
		return item;
    }
	
	@Override
	public boolean isItemTool(ItemStack par1ItemStack)
    {
		return true;
    }
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		String tooltip = "";
		switch(par1ItemStack.getItemDamage())
		{
		case 0: tooltip = EnumChatFormatting.BLUE + "Tank Select Mode";
		break;
		case 1: tooltip = EnumChatFormatting.GREEN + "Inventory Select Mode";
		break;
		case 2: tooltip = EnumChatFormatting.RED + "Redstone Circuit Select Mode";
		break;
		case 3: tooltip = EnumChatFormatting.DARK_PURPLE + "Redstone Latch Select Mode";
		break;
		case 4: tooltip = EnumChatFormatting.WHITE + "Generic Mode";
		break;
		case 5: tooltip = EnumChatFormatting.YELLOW + "Module Lock Mode";
		break;
		case 6: tooltip = EnumChatFormatting.AQUA + "Facade Removal Mode";
		break;
		}
		
		par3List.add(tooltip);
	}

}
