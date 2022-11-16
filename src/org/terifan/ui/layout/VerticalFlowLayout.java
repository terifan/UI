package org.terifan.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;


public class VerticalFlowLayout implements LayoutManager
{
	public final static int CENTER = 0;
	public final static int RIGHT = 1;
	public final static int LEFT = 2;
	public final static int BOTH = 3;
	public final static int TOP = 1;
	public final static int BOTTOM = 2;
	private int mVGap;
	private int mAlignment;
	private int mAnchor;


	public VerticalFlowLayout()
	{
		this(0, BOTH, TOP);
	}


	public VerticalFlowLayout(int aVGap)
	{
		this(aVGap, BOTH, TOP);
	}


	public VerticalFlowLayout(int aVGap, int aAlignment)
	{
		this(aVGap, aAlignment, TOP);
	}


	public VerticalFlowLayout(int aVGap, int aAlignment, int aAnchor)
	{
		mVGap = aVGap;
		mAlignment = aAlignment;
		mAnchor = aAnchor;
	}


	private Dimension layoutSize(Container aParent, boolean aMinimum)
	{
		Dimension dim = new Dimension(0, 0);
		Dimension d;
		synchronized (aParent.getTreeLock())
		{
			int n = aParent.getComponentCount();
			for (int i = 0; i < n; i++)
			{
				Component c = aParent.getComponent(i);
				if (c.isVisible())
				{
					d = aMinimum ? c.getMinimumSize() : c.getPreferredSize();
					dim.width = Math.max(dim.width, d.width);
					dim.height += d.height;
					if (i > 0)
					{
						dim.height += mVGap;
					}
				}
			}
		}
		Insets insets = aParent.getInsets();
		dim.width += insets.left + insets.right;
		dim.height += insets.top + insets.bottom;
		return dim;
	}


	@Override
	public void layoutContainer(Container aParent)
	{
		Insets insets = aParent.getInsets();
		synchronized (aParent.getTreeLock())
		{
			int n = aParent.getComponentCount();
			Dimension pd = aParent.getSize();
			int y = 0;

			for (int i = 0; i < n; i++)
			{
				Component c = aParent.getComponent(i);
				Dimension d = c.getPreferredSize();
				y += d.height + mVGap;
			}
			y -= mVGap; //otherwise there's a vgap too many

			if (mAnchor == TOP)
			{
				y = insets.top;
			}
			else
			{
				if (mAnchor == CENTER)
				{
					y = (pd.height - y) / 2;
				}
				else
				{
					y = pd.height - y - insets.bottom;
				}
			}

			for (int i = 0; i < n; i++)
			{
				Component c = aParent.getComponent(i);
				Dimension d = c.getPreferredSize();
				int x = insets.left;
				int wid = d.width;
				if (mAlignment == CENTER)
				{
					x = (pd.width - d.width) / 2;
				}
				else if (mAlignment == RIGHT)
				{
					x = pd.width - d.width - insets.right;
				}
				else if (mAlignment == BOTH)
				{
					wid = pd.width - insets.left - insets.right;
				}
				c.setBounds(x, y, wid, d.height);
				y += d.height + mVGap;
			}
		}
	}


	@Override
	public Dimension minimumLayoutSize(Container parent)
	{
		return layoutSize(parent, false);
	}


	@Override
	public Dimension preferredLayoutSize(Container parent)
	{
		return layoutSize(parent, false);
	}


	@Override
	public void addLayoutComponent(String name, Component comp)
	{
	}


	@Override
	public void removeLayoutComponent(Component comp)
	{
	}


	public String toString()
	{
		return getClass().getName() + "[vgap=" + mVGap + " align=" + mAlignment + " anchor=" + mAnchor + "]";
	}
}
