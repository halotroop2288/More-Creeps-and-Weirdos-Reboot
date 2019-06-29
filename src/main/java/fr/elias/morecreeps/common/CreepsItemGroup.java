package fr.elias.morecreeps.common;

import fr.elias.morecreeps.common.lists.ItemList;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreepsItemGroup extends ItemGroup
{
	public CreepsItemGroup() 
	{
		super("creepsTab");
	}

	@Override
	public ItemStack createIcon() 
	{
		return new ItemStack(ItemList.a_floob);
	}
}