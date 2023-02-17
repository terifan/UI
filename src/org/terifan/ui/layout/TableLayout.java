package org.terifan.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JComponent;
import org.terifan.ui.Anchor;
import org.terifan.ui.Fill;


public class TableLayout implements LayoutManager2
{
	private ArrayList<Component> mCurrentRow;
	private ArrayList<ArrayList<Component>> mComponents;
	private ArrayList<Integer> mColumnWidths;
	private ArrayList<Integer> mRowHeights;
	private HashMap<Component,Params> mParams;
	private int mWidth;
	private int mHeight;
	private int mSpacingX;
	private int mSpacingY;
	private Params mDefaultParams;


	public TableLayout()
	{
		this(0, 0);
	}


	public TableLayout(int aSpacingX, int aSpacingY)
	{
		mColumnWidths = new ArrayList<>();
		mRowHeights = new ArrayList<>();
		mComponents = new ArrayList<>();
		mSpacingX = aSpacingX;
		mSpacingY = aSpacingY;

		mParams = new HashMap<>();
		mCurrentRow = new ArrayList<>();
		mComponents.add(mCurrentRow);

		mDefaultParams = new Params(Fill.BOTH, Anchor.WEST, new Dimension(0, 0));
	}


	@Override
	public void addLayoutComponent(Component aComp, Object aConstraints)
	{
		mCurrentRow.add(aComp);

		if (aConstraints != null)
		{
			mParams.put(aComp, (Params)aConstraints);
		}
	}


	@Override
	public void addLayoutComponent(String aName, Component aComp)
	{
		mCurrentRow.add(aComp);
	}


	@Override
	public void removeLayoutComponent(Component aComp)
	{
		mComponents.forEach(e -> e.remove(aComp));
	}


	@Override
	public Dimension minimumLayoutSize(Container aParent)
	{
		return layoutSize(aParent, false);
	}


	@Override
	public Dimension preferredLayoutSize(Container aParent)
	{
		return layoutSize(aParent, false);
	}


	@Override
	public float getLayoutAlignmentX(Container aTarget)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


	@Override
	public float getLayoutAlignmentY(Container aTarget)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


	@Override
	public synchronized void invalidateLayout(Container aTarget)
	{
	}


	private Dimension layoutSize(Container aParent, boolean aMinimum)
	{
		synchronized (aParent.getTreeLock())
		{
			ArrayList<Integer> columnWidths = new ArrayList<>();
			ArrayList<Integer> rowHeights = new ArrayList<>();

			int rowCount = getRowCount();
			int maxColumns = 0;

			for (int iy = 0; iy < rowCount; iy++)
			{
				int rowHeight = 0;

				for (int ix = 0; ix < mComponents.get(iy).size(); ix++)
				{
					JComponent comp = (JComponent)mComponents.get(iy).get(ix);
					Dimension dim = comp.getPreferredSize();

					rowHeight = Math.max(rowHeight, dim.height);

					if (ix == columnWidths.size())
					{
						columnWidths.add(dim.width);
					}
					else
					{
						int w = Math.max(dim.width, columnWidths.get(ix));
						columnWidths.set(ix, w);
					}
				}

				rowHeights.add(rowHeight);
				maxColumns = Math.max(maxColumns, mComponents.get(iy).size());
			}

			mColumnWidths = columnWidths;
			mRowHeights = rowHeights;
			mWidth = columnWidths.stream().mapToInt(e -> e).sum() + mSpacingX * (maxColumns - 1) + aParent.getInsets().left + aParent.getInsets().right;
			mHeight = rowHeights.stream().mapToInt(e -> e).sum() + mSpacingY * (rowCount - 1) + aParent.getInsets().top + aParent.getInsets().bottom;

			return new Dimension(mWidth, mHeight);
		}
	}


	@Override
	public synchronized void layoutContainer(Container aParent)
	{
		Insets insets = aParent.getInsets();

		synchronized (aParent.getTreeLock())
		{
			int rowCount = getRowCount();

			int extraW = 0;//aParent.getWidth() - mWidth - insets.left - insets.right;
			int extraH = 0;//aParent.getHeight() - mHeight - insets.top - insets.bottom;

			int maxCols = 0;
			for (int iy = 0; iy < mRowHeights.size() && iy < rowCount; iy++)
			{
				maxCols = Math.max(maxCols, mColumnWidths.size());
			}

			int rowY = insets.top;

			for (int iy = 0; iy < mRowHeights.size() && iy < rowCount; iy++)
			{
				int rowX = insets.left;

				ArrayList<Component> row = mComponents.get(iy);

				for (int ix = 0; ix < mColumnWidths.size() && ix < row.size(); ix++)
				{
					JComponent comp = (JComponent)row.get(ix);
					Dimension dim = comp.getPreferredSize();

					int compX = rowX;
					int compY = rowY;
					int cellW = mColumnWidths.get(ix) + extraW / maxCols;
					int cellH = mRowHeights.get(iy) + extraH / rowCount;

					Params params = mParams.getOrDefault(comp, mDefaultParams);

					Rectangle compBounds = new Rectangle(0, 0, dim.width, dim.height);
					Rectangle cellBounds = new Rectangle(compX, compY, cellW, cellH);

					params.mFill.scale(compBounds, cellBounds);
					params.mAnchor.translate(compBounds, cellBounds);

					comp.setBounds(compBounds);

					rowX += mColumnWidths.get(ix) + mSpacingX + extraW / maxCols;
				}

				rowY += mRowHeights.get(iy) + mSpacingY + extraH / rowCount;
			}
		}
	}


	public int getRowCount()
	{
		int rowCount;
		for (rowCount = mComponents.size(); rowCount > 0 && mComponents.get(rowCount - 1).isEmpty(); rowCount--)
		{
		}
		return rowCount;
	}


	public synchronized void advanceRow()
	{
		if (!mCurrentRow.isEmpty())
		{
			mCurrentRow = new ArrayList<>();
			mComponents.add(mCurrentRow);
		}
	}


	@Override
	public Dimension maximumLayoutSize(Container aTarget)
	{
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}


	public static class Params
	{
		final Fill mFill;
		final Anchor mAnchor;
		final Dimension mPadding;


		public Params(Fill aFill, Anchor aAnchor, Dimension aPadding)
		{
			mFill = aFill;
			mAnchor = aAnchor;
			mPadding = aPadding;
		}
	}
}
