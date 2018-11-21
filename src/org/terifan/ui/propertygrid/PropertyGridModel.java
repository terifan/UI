package org.terifan.ui.propertygrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class PropertyGridModel implements Iterable<Property>, Cloneable
{
	protected ArrayList<Property> mChildren;


	public PropertyGridModel()
	{
		mChildren = new ArrayList<>();
	}


	public PropertyGridModel addProperty(Property aProperty)
	{
		mChildren.add(aProperty);
		return this;
	}


	public Property getProperty(int aIndex)
	{
		return mChildren.get(aIndex);
	}


	public int size()
	{
		return mChildren.size();
	}


	public ArrayList<Property> getChildren()
	{
		return mChildren;
	}


	@Override
	public Iterator<Property> iterator()
	{
		ArrayList<Property> list = new ArrayList<>(mChildren);
		Collections.sort(list);
		return list.iterator();
	}


	public Iterator<Property> getRecursiveIterator()
	{
		ArrayList<Property> out = new ArrayList<>();
		for (Property item : mChildren)
		{
			out.add(item);
			if (item instanceof PropertyList && !item.isCollapsed())
			{
				((PropertyList)item).getRecursiveElements(out);
			}
		}

		return out.iterator();
	}


//	@Override
//	public PropertyGridModel clone() throws CloneNotSupportedException
//	{
//		PropertyGridModel clone = new PropertyGridModel();
//
//		for (Property item : mChildren)
//		{
//			clone.mChildren.add(item.clone());
//		}
//
//		return clone;
//	}
}