package org.terifan.ui.propertygrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class PropertyGridModel implements Iterable<Property>, Cloneable
{
	protected ArrayList<Property> mElements;


	public PropertyGridModel()
	{
		mElements = new ArrayList<>();
	}


	public void add(Property aProperty)
	{
		mElements.add(aProperty);
	}


	public Property getProperty(int aIndex)
	{
		return mElements.get(aIndex);
	}


	public int getPropertyCount()
	{
		return mElements.size();
	}


	protected int getIndent(Property aProperty)
	{
		for (Property item : mElements)
		{
			Integer tmp = item.getIndent(aProperty, 1);
			if (tmp != null)
			{
				return tmp;
			}
		}

		return 0;
	}


	@Override
	public Iterator<Property> iterator()
	{
		ArrayList<Property> list = new ArrayList<>(mElements);
		Collections.sort(list);
		return list.iterator();
	}


	public Iterator<Property> getRecursiveIterator()
	{
		ArrayList<Property> list = new ArrayList<>(mElements);
		Collections.sort(list);

		ArrayList<Property> out = new ArrayList<>();
		for (Property property : list)
		{
			out.add(property);
			if (!property.getCollapsed())
			{
				property.getRecursiveElements(out);
			}
		}

		return out.iterator();
	}


	@Override
	public PropertyGridModel clone() throws CloneNotSupportedException
	{
		PropertyGridModel clone = new PropertyGridModel();

		for (Property item : mElements)
		{
			clone.mElements.add(item.clone());
		}

		return clone;
	}
}