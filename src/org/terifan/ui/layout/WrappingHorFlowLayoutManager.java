package org.terifan.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.terifan.ui.Anchor;


public class WrappingHorFlowLayoutManager implements LayoutManager
{
	private ArrayList<Rectangle> mLayoutInfo;
	private int mColSpacing;
	private int mRowSpacing;
	private Anchor mAnchor;


	public WrappingHorFlowLayoutManager()
	{
		this(Anchor.WEST);
	}


	public WrappingHorFlowLayoutManager(Anchor aAnchor)
	{
		mLayoutInfo = new ArrayList<>();
		mAnchor = aAnchor;
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
//		return new Dimension(1, 1);
		synchronized (aTarget.getTreeLock())
		{
			return computeSize(aTarget, aTarget.getParent().getWidth(), true);
		}
	}


	@Override
	public Dimension preferredLayoutSize(Container aTarget)
	{
		synchronized (aTarget.getTreeLock())
		{
			return computeSize(aTarget, aTarget.getParent().getWidth(), false);
		}
	}


	private Dimension computeSize(Container aTarget, int aTargetWidth, boolean aMinimum)
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
				Dimension compDim = aMinimum ? comp.getMinimumSize() : comp.getPreferredSize();
				int compW = compDim.width;
				int compH = compDim.height;
				if (rowWidth > 0 && rowWidth + compW >= aTargetWidth)
				{
					totalWidth = Math.max(totalWidth, rowWidth);
					totalHeight += rowHeight + mRowSpacing;
					mLayoutInfo.add(new Rectangle(rowCount, 0, rowWidth, rowHeight));
					rowWidth = 0;
					rowHeight = 0;
					rowCount = 0;
				}
				rowWidth += (rowCount > 0 ? mColSpacing : 0) + compW;
				rowHeight = Math.max(rowHeight, compH);
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
//			computeSize(aTarget, aTarget.getWidth(), false);

			Insets insets = aTarget.getInsets();

			int n = aTarget.getComponentCount();

			Rectangle layout = mLayoutInfo.getFirst();

			int x = aTarget.getWidth() - layout.width - insets.right;
			int y = insets.top;

			for (int i = 0, col = 0, row = 0; i < n; i++)
			{
				Component comp = aTarget.getComponent(i);
				Dimension d = comp.getPreferredSize();

				if (comp.isVisible())
				{
					layout = mLayoutInfo.get(row);
					comp.setBounds(x, y, d.width, layout.height);
					comp.revalidate();
					x += d.width + mColSpacing;
					col++;
				}

				if (col >= layout.x)
				{
					x = aTarget.getWidth() - layout.width - insets.right;
					y += layout.height + mRowSpacing;
					row++;
					col = 0;
				}
			}
		}
	}
}
