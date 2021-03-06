package emasher.gas.modules

import emasher.api._;
import emasher.core._;
import emasher.sockets._;
import java.util._;
import net.minecraft.item.crafting._;
import net.minecraftforge.oredict._;
import net.minecraftforge.common._;
import net.minecraftforge.fluids._;
import net.minecraft.item._;
import net.minecraft.block._;
import emasher.gas.fluids.FluidGas;

class ModGasTurbine(id: Int) extends SocketModule(id, "gascraft:gasTurbine", "gascraft:gasTurbineActive")
{
	override def getLocalizedName = "Gas Turbine";
	
	override def getToolTip(l: List[Object])
	{
		l.add("Generates power when certain");
		l.add("gases are input into it");
	}
	
	override def getIndicatorKey(l: List[Object])
	{
		l.add(SocketsMod.PREF_BLUE + "Fuel Tank");
		l.add(SocketsMod.PREF_AQUA + "Outputs 10 f/t");
	}
	
	override def getCurrentTexture(config: SideConfig):
	Int = 
	{
		if(config.rsControl(0)) 1;
		else 0;
	}
	
	override def addRecipe
	{
		CraftingManager.getInstance().getRecipeList().asInstanceOf[List[Object]].add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "ipi", "ufu", " b ", Character.valueOf('p'), EmasherCore.psu, Character.valueOf('i'), Item.ingotIron,
				Character.valueOf('u'), Item.bucketEmpty, Character.valueOf('f'), Block.glass, Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	override def hasTankIndicator = true;
	
	override def isMachine = true;
	
	override def canBeInstalled(ts: SocketTileAccess, side: ForgeDirection):
	Boolean = 
	{
		if(side == ForgeDirection.UP || side == ForgeDirection.DOWN) return false;
		
		for(i <- 0 to 5)
		{
			var m = ts.getSide(ForgeDirection.getOrientation(i))
			if(m != null && m.isMachine)
			{
				return false;
			}
		}
		
		true;
	}
	
	/*
	 * Config Dictionary
	 * 
	 * rsControl(0) -> Persistent generator is on or off
	 * 
	 * Side Inventory
	 * 
	 * itemID 		-> Energy per tick
	 * itemDamage	-> Remaining time
	*/
	
	override def updateSide(config: SideConfig, ts: SocketTileAccess, side: ForgeDirection)
	{
		if(!config.rsLatch(2))
		{
			//Compatibility
			//This code block should eventually be removed
			config.rsLatch(2) = true;
			
			config.meta = 0;
			ts.sideInventory.setInventorySlotContents(side.ordinal, null);
			config.rsControl(0) = false;
			config.rsControl(1) = false;
			config.rsControl(2) = false;
		}
		
		if(config.tank >= 0 && config.tank < 3)
		{
			var initState = config.rsControl(0);
			var newState = initState;
			
			if(initState) //Generator is on
			{
				//Check to see if there's still fuel
				var is = ts.sideInventory.getStackInSlot(side.ordinal);
				
				if(is != null && newState)
				{
					//Check if the internal capacitor can hold any more energy
					
					if(ts.getMaxEnergyStored() - ts.getEnergyStored() >= 10)
					{
						ts.addEnergy(10, false);
						is.setItemDamage(is.getItemDamage - 1);
						if(is.getItemDamage() <= 0)
						{
							ts.sideInventory.setInventorySlotContents(side.ordinal, null);
							newState = false;
						}
					}
					else
					{
						newState = false;
					}
				}
				
				if(is == null && ! newState)
				{
					var theFluid = ts.getFluidInTank(config.tank);
                    if(theFluid != null && theFluid.getFluid.isGaseous())
                    {
                    	if(theFluid.amount >= 1000 && ts.getMaxEnergyStored() - ts.getEnergyStored() >= 10)
                    	{
                    		var newStack = new ItemStack(1, 1, 80);
                    		ts.sideInventory.setInventorySlotContents(side.ordinal, newStack);
                    		config.rsControl(2) = GeneratorFuelRegistry.producesSmoke(theFluid.getFluid.getName);
                    		ts.drainInternal(config.tank, 1000, true);
                    		newState = true;
                    	}
                    }
                    
				}
			}
			else //Generator is off
			{
				var is = ts.sideInventory.getStackInSlot(side.ordinal);
				if(is == null)
				{
					var theFluid = ts.getFluidInTank(config.tank);
                    if(theFluid != null && theFluid.getFluid.isGaseous())
                    {
                    	if(theFluid.amount >= 1000 && ts.getMaxEnergyStored() - ts.getEnergyStored() >= 10)
                    	{
                    		var newStack = new ItemStack(1, 1, 80);
                    		ts.sideInventory.setInventorySlotContents(side.ordinal, newStack);
                    		config.rsControl(2) = GeneratorFuelRegistry.producesSmoke(theFluid.getFluid.getName);
                    		ts.drainInternal(config.tank, 1000, true);
                    		newState = true;
                    	}
                    }
                }
				else
				{
					if(ts.getMaxEnergyStored() - ts.getEnergyStored() >= is.itemID) newState = true;
				}
			}
			
			if(newState != initState)
			{
				config.rsControl(0) = newState;
				ts.sendClientSideState(side.ordinal);
			}
		}
		else
		{
			config.meta = 0;
			ts.sideInventory.setInventorySlotContents(side.ordinal, null);
			
			config.rsControl(1) = false;
			config.rsControl(2) = false;
			
			if(config.rsControl(0))
			{
				config.rsControl(0) = false;
				ts.sendClientSideState(side.ordinal);
			}
			
			
		}
		
	}
}