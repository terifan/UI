package org.terifan.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import org.terifan.ui.Anchor;
import static org.terifan.ui.Anchor.CENTER;
import static org.terifan.ui.Anchor.NORTH;
import static org.terifan.ui.Anchor.NORTH_WEST;
import static org.terifan.ui.Anchor.SOUTH;
import static org.terifan.ui.Anchor.SOUTH_WEST;
import static org.terifan.ui.Anchor.WEST;
import org.terifan.ui.Fill;


public class HorizontalFlowLayout implements LayoutManager
{
	private int mGap;
	private Anchor mAnchor;
	private Fill mFill;
	private Insets mInsets;


	public HorizontalFlowLayout()
	{
		this(0, Anchor.WEST, Fill.NONE);
	}


	public HorizontalFlowLayout(int aGap)
	{
		this(aGap, Anchor.WEST, Fill.NONE);
	}


	public HorizontalFlowLayout(int aGap, Anchor aAnchor)
	{
		this(aGap, aAnchor, Fill.NONE);
	}


	public HorizontalFlowLayout(int aGap, Fill aFill)
	{
		this(aGap, Anchor.WEST, aFill);
	}


	public HorizontalFlowLayout(int aGap, Anchor aAnchor, Fill aFill)
	{
		mGap = aGap;
		mAnchor = aAnchor;
		mFill = aFill;
		mInsets = new Insets(0, 0, 0, 0);
	}


	public HorizontalFlowLayout setAnchor(Anchor aAnchor)
	{
		mAnchor = aAnchor;
		return this;
	}


	public HorizontalFlowLayout setFill(Fill aFill)
	{
		mFill = aFill;
		return this;
	}


	public HorizontalFlowLayout setInsets(Insets aInsets)
	{
		mInsets = aInsets;
		return this;
	}


	private Dimension layoutSize(Container aParent, boolean aMinimum)
	{
		Dimension total = new Dimension(0, 0);
		synchronized (aParent.getTreeLock())
		{
			int n = aParent.getComponentCount();
			for (int i = 0; i < n; i++)
			{
				Component comp = aParent.getComponent(i);
				if (comp.isVisible())
				{
					Dimension compDim = aMinimum ? comp.getMinimumSize() : comp.getPreferredSize();
					total.width += compDim.width;
					total.height = Math.max(total.height, compDim.height);
					if (i > 0)
					{
						total.width += mGap;
					}
				}
			}
		}
		Insets insets = aParent.getInsets();
		total.width += insets.left + insets.right + mInsets.left + mInsets.right;
		total.height += insets.top + insets.bottom + mInsets.top + mInsets.bottom;
		return total;
	}


	@Override
	public void layoutContainer(Container aParent)
	{
		Insets insets = aParent.getInsets();
		synchronized (aParent.getTreeLock())
		{
			int n = aParent.getComponentCount();
			Dimension parentDim = aParent.getSize();
			int x = 0;

			for (int i = 0; i < n; i++)
			{
				Component c = aParent.getComponent(i);
				Dimension cd = c.getPreferredSize();
				x += cd.width + mGap;
			}
			x -= mGap;

			if (mFill == Fill.BOTH || mFill == Fill.HORIZONTAL)
			{
				x = insets.left + mInsets.left;
			}
			else
			{
				switch (mAnchor)
				{
					case NORTH_WEST:
					case WEST:
					case SOUTH_WEST:
						x = insets.left + mInsets.left;
						break;
					case CENTER:
					case NORTH:
					case SOUTH:
						x = (parentDim.width - x) / 2;
						break;
					default:
						x = parentDim.width - insets.right - mInsets.right - x;
						break;
				}
			}

			for (int i = 0; i < n; i++)
			{
				Component comp = aParent.getComponent(i);
				Dimension compDimp = comp.getPreferredSize();
				int compWidth = compDimp.width;
				int compHeight = compDimp.height;
				int y;
				if (mFill == Fill.BOTH || mFill == Fill.VERTICAL)
				{
					y = insets.top + mInsets.top;
					compHeight = parentDim.height - mInsets.top - mInsets.bottom;
				}
				else
				{
					switch (mAnchor)
					{
						case NORTH_WEST:
						case NORTH:
						case NORTH_EAST:
							y = insets.top + mInsets.top;
							break;
						case CENTER:
						case WEST:
						case EAST:
							y = (parentDim.height - mInsets.top - mInsets.bottom - compHeight) / 2 + mInsets.top + insets.top;
							break;
						default:
							y = parentDim.height - mInsets.top - mInsets.bottom - compHeight - insets.bottom - mInsets.bottom;
							break;
					}
				}
				comp.setBounds(x, y, compWidth, compHeight);
				x += compWidth + mGap;
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


	@Override
	public String toString()
	{
		return getClass().getName() + "[gap=" + mGap + ", anchor=" + mAnchor + "]";
	}
}
