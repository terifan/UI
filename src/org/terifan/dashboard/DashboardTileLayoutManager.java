package org.terifan.dashboard;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;


class DashboardTileLayoutManager implements LayoutManager
{
	@Override
	public void addLayoutComponent(String aName, Component aComp)
	{
	}


	@Override
	public void removeLayoutComponent(Component aComp)
	{
	}


	@Override
	public Dimension preferredLayoutSize(Container aParent)
	{
		return new Dimension();
	}


	@Override
	public Dimension minimumLayoutSize(Container aParent)
	{
		return new Dimension();
	}


	@Override
	public void layoutContainer(Container aParent)
	{
		synchronized (aParent.getTreeLock())
		{
			for (int i = 0; i < aParent.getComponentCount(); i++)
			{
				Component comp = aParent.getComponent(i);

				if (comp.isVisible())
				{
					DashboardTile area = (DashboardTile)comp;

					comp.setBounds(area.getBounds());
				}
			}
		}
	}
}
