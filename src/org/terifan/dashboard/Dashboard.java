package org.terifan.dashboard;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import org.terifan.bundle.Array;
import org.terifan.bundle.Bundlable;
import org.terifan.bundle.Bundle;


public class Dashboard extends JPanel implements Bundlable
{
	private static final long serialVersionUID = 1L;

	private DashboardTile mSelected;
	private int mGridSize;


	public Dashboard()
	{
		mGridSize = 100;

		super.setBackground(new Color(33,91,125));
		super.setLayout(new DashboardTileLayoutManager());
		super.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent aEvent)
			{
				setSelected(null);
				repaint();
			}
		});
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		aGraphics.setColor(getBackground());
		aGraphics.fillRect(0, 0, getWidth(), getHeight());
	}


	public void add(DashboardTile aDashboard)
	{
		aDashboard.bind(this);

		super.add(aDashboard);
	}


	void moveToTop(DashboardTile aComponent)
	{
		super.remove(aComponent);
		super.add(aComponent, 0);
	}


	DashboardTile getSelected()
	{
		return mSelected;
	}


	public void setSelected(DashboardTile aSelected)
	{
		DashboardTile old = mSelected;
		mSelected = aSelected;

		if (old != null)
		{
			old.invalidate();
			old.validate();
		}
		if (mSelected != null)
		{
			mSelected.invalidate();
			mSelected.validate();
		}
	}


	public int getGridSize()
	{
		return mGridSize;
	}


	public void setGridSize(int aGridSize)
	{
		mGridSize = aGridSize;
	}


	@Override
	public void writeExternal(Bundle aBundle)
	{
		aBundle.putArray("tiles", Array.of((Object[])getComponents()));
	}


	@Override
	public void readExternal(Bundle aBundle)
	{
		for (Component c : getComponents())
		{
			DashboardTile tile = (DashboardTile)c;

			for (Bundle b : aBundle.getBundleArray("tiles"))
			{
				if (tile.getTitle().equals(b.getString("title")))
				{
					tile.readExternal(b);
				}
			}
		}
	}
}
