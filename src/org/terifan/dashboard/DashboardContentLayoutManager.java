package org.terifan.dashboard;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;


class DashboardContentLayoutManager implements LayoutManager
{
	private DashboardTile mTile;


	public DashboardContentLayoutManager(DashboardTile aTile)
	{
		mTile = aTile;
	}


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
			int w = aParent.getWidth();
			int h = aParent.getHeight();
			int s = 6;

			if (mTile.getDashboard().getSelected() == mTile)
			{
				aParent.getComponent(0).setBounds(s, s + 16 + 2, w - 2 * s, h - (s + 16 + 2 + s + 16));
			}
			else
			{
				aParent.getComponent(0).setBounds(s, s, w - 2 * s, h - 2 * s);
			}
		}
	}
}
