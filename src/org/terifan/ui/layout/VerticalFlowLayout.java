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


public class VerticalFlowLayout implements LayoutManager
{
	private int mGap;
	private Anchor mAnchor;
	private Fill mFill;
	private Insets mInsets;
	private boolean mFillLast;


	public VerticalFlowLayout()
	{
		this(0, Anchor.NORTH_WEST, Fill.NONE);
	}


	public VerticalFlowLayout(int aGap)
	{
		this(aGap, Anchor.NORTH_WEST, Fill.NONE);
	}


	public VerticalFlowLayout(int aGap, Anchor aAnchor)
	{
		this(aGap, aAnchor, Fill.NONE);
	}


	public VerticalFlowLayout(int aGap, Fill aFill)
	{
		this(aGap, Anchor.NORTH_WEST, aFill);
	}


	public VerticalFlowLayout(int aGap, Anchor aAnchor, Fill aFill)
	{
		mGap = aGap;
		mAnchor = aAnchor;
		mFill = aFill;
		mInsets = new Insets(0, 0, 0, 0);
	}


	public VerticalFlowLayout setAnchor(Anchor aAnchor)
	{
		mAnchor = aAnchor;
		return this;
	}


	public VerticalFlowLayout setFill(Fill aFill)
	{
		mFill = aFill;
		return this;
	}


	public VerticalFlowLayout setInsets(Insets aInsets)
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
					total.width = Math.max(total.width, compDim.width);
					total.height += compDim.height;
					if (i > 0)
					{
						total.height += mGap;
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

			int y = 0;
			for (int i = 0; i < n; i++)
			{
				Component c = aParent.getComponent(i);
				Dimension cd = c.getPreferredSize();
				y += cd.height + mGap;
			}
			y -= mGap;

			if (mFill == Fill.BOTH || mFill == Fill.VERTICAL)
			{
				y = insets.top + mInsets.top;
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
						y = (parentDim.height - y) / 2;
						break;
					default:
						y = parentDim.height - insets.bottom - mInsets.bottom - y;
						break;
				}
			}

			for (int i = 0; i < n; i++)
			{
				Component comp = aParent.getComponent(i);
				Dimension compDimp = comp.getPreferredSize();
				int compWidth = compDimp.width;
				int compHeight = compDimp.height;
				int x;
				if (mFill == Fill.BOTH || mFill == Fill.HORIZONTAL)
				{
					x = insets.left + mInsets.left;
					compWidth = parentDim.width - mInsets.left - mInsets.right - insets.left - insets.right;
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
							x = (parentDim.width - mInsets.left - mInsets.right - compWidth) / 2 + mInsets.left + insets.left;
							break;
						default:
							x = parentDim.width - mInsets.left - mInsets.right - compWidth - insets.right - mInsets.right;
							break;
					}
				}

				if (mFillLast && i == n - 1)
				{
					compHeight = Math.max(compHeight, parentDim.height - insets.bottom - mInsets.bottom - y);
				}

				comp.setBounds(x, y, compWidth, compHeight);
				y += compHeight + mGap;
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


	public VerticalFlowLayout setFillLast(boolean aState)
	{
		mFillLast = aState;
		return this;
	}
}
