package org.terifan.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;


public class WrapHorFlowLayoutManager implements LayoutManager
{
	private ArrayList<Rectangle> mLayoutInfo;
	private int mColSpacing;
	private int mRowSpacing;


	public WrapHorFlowLayoutManager()
	{
		mLayoutInfo = new ArrayList<>();
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
	public Dimension minimumLayoutSize(Container aTarget)
	{
		return new Dimension(1, 1);
	}


	@Override
	public Dimension preferredLayoutSize(Container aTarget)
	{
		synchronized (aTarget.getTreeLock())
		{
			return computeSize(aTarget, aTarget.getParent().getWidth());
		}
	}


	private Dimension computeSize(Container aTarget, int aTargetWidth)
	{
		int rowWidth = 0;
		int totalWidth = 0;
		int totalHeight = 0;

		Insets insets = aTarget.getInsets();

		aTargetWidth -= insets.left + insets.right;

		mLayoutInfo.clear();

		int n = aTarget.getComponentCount();

		int rowHeight = 0;
		int rowCount = 0;
		for (int i = 0; i < n; i++)
		{
			Component comp = aTarget.getComponent(i);
			if (comp.isVisible())
			{
				Dimension d = comp.getPreferredSize();
				if (rowWidth + d.width >= aTargetWidth)
				{
					totalWidth = Math.max(totalWidth, rowWidth);
					totalHeight += rowHeight + mRowSpacing;
					mLayoutInfo.add(new Rectangle(rowCount, 0, rowWidth, rowHeight));
					rowWidth = 0;
					rowHeight = 0;
					rowCount = 0;
				}
				rowWidth += (rowCount > 0 ? mColSpacing : 0) + d.width;
				rowHeight = Math.max(rowHeight, d.height);
				rowCount++;
			}
		}

		totalWidth = Math.max(totalWidth, rowWidth);
		totalHeight += rowHeight + mRowSpacing;

		totalWidth += insets.left + insets.right;
		totalHeight += insets.top + insets.bottom;

		mLayoutInfo.add(new Rectangle(rowCount, 0, rowWidth, rowHeight));

		return new Dimension(totalWidth, totalHeight);
	}


	@Override
	public void layoutContainer(Container aTarget)
	{
		synchronized (aTarget.getTreeLock())
		{
			computeSize(aTarget, aTarget.getWidth());

			Insets insets = aTarget.getInsets();

			int n = aTarget.getComponentCount();
			int x = insets.left;
			int y = insets.top;

			Rectangle layout = mLayoutInfo.getFirst();

			for (int i = 0, col = 0, row = 0; i < n; i++)
			{
				Component comp = aTarget.getComponent(i);
				Dimension d = comp.getPreferredSize();

				if (comp.isVisible())
				{
					layout = mLayoutInfo.get(row);
					comp.setBounds(x, y, d.width, layout.height);
					x += d.width + mColSpacing;
					col++;
				}

				if (col >= layout.x)
				{
					x = insets.left;
					y += layout.height + mRowSpacing;
					row++;
					col = 0;
				}
			}
		}
	}
}
