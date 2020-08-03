package org.terifan.dashboard;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.terifan.bundle.Bundlable;
import org.terifan.bundle.Bundle;


public class DashboardTile extends JPanel// implements Bundlable
{
	private static final long serialVersionUID = 1L;

	private Rectangle mBounds;
	private Dashboard mDashboard;
	private String mTitle;


	public DashboardTile(int aX, int aY, int aWidth, int aHeight)
	{
		this(aX, aY, aWidth, aHeight, null, null);
	}


	public DashboardTile(int aX, int aY, int aWidth, int aHeight, String aTitle)
	{
		this(aX, aY, aWidth, aHeight, aTitle, null);
	}


	public DashboardTile(int aX, int aY, int aWidth, int aHeight, String aTitle, JComponent aComponent)
	{
		mTitle = aTitle;
		mBounds = new Rectangle(aX, aY, aWidth, aHeight);

		DashboardMouseListener listener = new DashboardMouseListener(this);

		super.setLayout(new DashboardContentLayoutManager(this));
		super.addMouseListener(listener);
		super.addMouseMotionListener(listener);
		super.setBackground(Color.WHITE);
		super.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		if (aComponent != null)
		{
			super.add(aComponent);
			aComponent.setBackground(Color.red);
		}
	}


	public void bind(Dashboard aDashboard)
	{
		if (mDashboard  == null)
		{
			mBounds.x *= aDashboard.getGridSize();
			mBounds.y *= aDashboard.getGridSize();
			mBounds.width *= aDashboard.getGridSize();
			mBounds.height *= aDashboard.getGridSize();
		}

		mDashboard = aDashboard;
	}


	public Dashboard getDashboard()
	{
		return mDashboard;
	}


	@Override
	public void invalidate()
	{
		super.invalidate();
	}


	public String getTitle()
	{
		return mTitle;
	}


	@Override
	public Rectangle getBounds()
	{
		return mBounds;
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		int w = getWidth();
		int h = getHeight();

		aGraphics.setColor(new Color(33,91,125));
		aGraphics.fillRect(0, 0, w, h);

		aGraphics.translate(2, 2);
		w -= 4;
		h -= 4;

		aGraphics.setColor(getBackground());
		aGraphics.fillRect(0, 0, w, h);

		if (mDashboard.getSelected() == this)
		{
			// border
			aGraphics.setColor(Color.GRAY);
			aGraphics.drawRect(0, 0, w - 1, h - 1);

			// move box
			aGraphics.translate(4, 4);
			aGraphics.setColor(new Color(220, 220, 220));
			aGraphics.drawRect(0, 0, 16, 16);
			aGraphics.drawRect(1, 1, 14, 14);
			aGraphics.drawRect(2, 2, 12, 12);
			aGraphics.translate(-4, -4);

			// size box
			aGraphics.translate(w - 19, h - 19);
			aGraphics.setColor(new Color(220, 220, 220));
			aGraphics.fillRect(12, 0, 3, 15);
			aGraphics.fillRect(0, 12, 15, 3);
			aGraphics.translate(-(w - 19), -(h - 19));

			if (mTitle != null)
			{
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawString(mTitle, 3 + 16 + 3, 17);
			}

			aGraphics.setColor(new Color(156, 213, 244));
			aGraphics.drawRect(0, 0, w - 1, h - 1);
			aGraphics.drawRect(1, 1, w - 3, h - 3);
			aGraphics.drawRect(2, 2, w - 5, h - 5);
		}

		aGraphics.translate(-2, -2);
	}


	@Override
	public String toString()
	{
		return mTitle;
	}


//	@Override
//	public void writeExternal(Bundle aBundle)
//	{
//		int gs = mDashboard.getGridSize();
//
//		aBundle.putString("title", mTitle).putNumber("x", mBounds.x / gs).putNumber("y", mBounds.y / gs).putNumber("width", mBounds.width / gs).putNumber("height", mBounds.height / gs);
//	}
//
//
//	@Override
//	public void readExternal(Bundle aBundle)
//	{
//		int gs = mDashboard.getGridSize();
//
//		mTitle = aBundle.getString("title");
//		mBounds.x = gs * aBundle.getInt("x");
//		mBounds.y = gs * aBundle.getInt("y");
//		mBounds.width = gs * aBundle.getInt("width");
//		mBounds.height = gs * aBundle.getInt("height");
//	}
}
