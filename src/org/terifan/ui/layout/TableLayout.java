package org.terifan.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JComponent;
import org.terifan.ui.Anchor;
import org.terifan.ui.Fill;
import org.terifan.ui.Orientation;


public class TableLayout implements LayoutManager2
{
	private ArrayList<Component> mCurrentRow;
	private ArrayList<ArrayList<Component>> mComponents;
	private ArrayList<Integer> mColumnWidths;
	private ArrayList<Integer> mRowHeights;
	private HashMap<Component, LayoutParams> mParams;
//	private int mWidth;
//	private int mHeight;
	private int mSpacingX;
	private int mSpacingY;
	private LayoutParams mDefaultParams;
	private final Orientation mOrientation;
	private final int mCellCount;
	private final float[] mWeights;
	private int mCounter;


	public TableLayout(Orientation aOrientation, float... aWeights)
	{
		this(aOrientation, aWeights.length);

		System.arraycopy(aWeights, 0, mWeights, 0, aWeights.length);
	}


	public TableLayout(Orientation aOrientation, int aCellCount)
	{
		mOrientation = aOrientation;
		mCellCount = aCellCount;

		mColumnWidths = new ArrayList<>();
		mRowHeights = new ArrayList<>();
		mComponents = new ArrayList<>();
		mWeights = new float[aCellCount];

		Arrays.fill(mWeights, 1f);

		mParams = new HashMap<>();

		mDefaultParams = new LayoutParams().setFill(Fill.NONE).setAnchor(Anchor.WEST).setPadding(new Dimension(0, 0)).setMargins(new Insets(0, 0, 0, 0));
	}


	public int getSpacingX()
	{
		return mSpacingX;
	}


	public TableLayout setSpacingX(int aSpacingX)
	{
		this.mSpacingX = aSpacingX;
		return this;
	}


	public int getSpacingY()
	{
		return mSpacingY;
	}


	public TableLayout setSpacingY(int aSpacingY)
	{
		this.mSpacingY = aSpacingY;
		return this;
	}


	public LayoutParams getDefaultParams()
	{
		return mDefaultParams;
	}


	public TableLayout setDefaultParams(LayoutParams aDefaultParams)
	{
		mDefaultParams = aDefaultParams;
		return this;
	}


	@Override
	public void addLayoutComponent(Component aComponent, Object aConstraints)
	{
		if ((mCounter % mCellCount) == 0)
		{
			mCurrentRow = new ArrayList<>();
			mComponents.add(mCurrentRow);
		}

		if (aConstraints instanceof LayoutParams v)
		{
			mParams.put(aComponent, v);
		}
		else if (aConstraints instanceof Anchor v)
		{
			mParams.put(aComponent, new LayoutParams().setAnchor(v));
		}
		else if (aConstraints instanceof Fill v)
		{
			mParams.put(aComponent, new LayoutParams().setFill(v));
		}

		mCurrentRow.add(aComponent);
		mCounter += getColSpan(aComponent);
	}


	@Override
	public void addLayoutComponent(String aName, Component aComponent)
	{
		if ((mCounter % mCellCount) == 0)
		{
			mCurrentRow = new ArrayList<>();
			mComponents.add(mCurrentRow);
		}

		mCurrentRow.add(aComponent);
		mCounter += getColSpan(aComponent);
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
					Insets margins = getMargins(comp) instanceof Insets v ? v : mDefaultParams.mMargins;

					dim.width += margins.left + margins.right;
					dim.height += margins.top + margins.bottom;

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
			int mWidth = columnWidths.stream().mapToInt(e -> e).sum() + mSpacingX * (maxColumns - 1) + aParent.getInsets().left + aParent.getInsets().right;
			int mHeight = rowHeights.stream().mapToInt(e -> e).sum() + mSpacingY * (rowCount - 1) + aParent.getInsets().top + aParent.getInsets().bottom;

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

			Dimension size = layoutSize(aParent, false);

			int extraW = aParent.getWidth() - size.width;
			int extraH = aParent.getHeight() - size.height;

			double totalWeight = 0;
			for (int i = 0; i < mCellCount; i++)
			{
				totalWeight += mWeights[i];
			}
			if (totalWeight == 0)
			{
				totalWeight = 1;
			}

			double[] columnWidths = new double[mCellCount];
			for (int i = 0; i < mCellCount; i++)
			{
				columnWidths[i] = mColumnWidths.get(i) + extraW * mWeights[i] / totalWeight;
			}

			int rowY = insets.top;

			for (int iy = 0; iy < mRowHeights.size() && iy < rowCount; iy++)
			{
				int rowX = insets.left;

				ArrayList<Component> row = mComponents.get(iy);

				double realX = 0;
				double realY = 0;

				for (int colIndex = 0; colIndex < mColumnWidths.size() && colIndex < row.size();)
				{
					Component comp = row.get(colIndex);
					Dimension dim = comp.getPreferredSize();
					Insets margins = getMargins(comp) instanceof Insets v ? v : mDefaultParams.mMargins;
					Dimension padding = getPadding(comp);

					dim.width += padding.width;
					dim.height += margins.top + margins.bottom + padding.height;

					realX += columnWidths[colIndex];

					int compX = rowX;
					int compY = rowY;
					int cellW = (int)realX;
					int cellH = (int)realY;

					Rectangle compBounds = new Rectangle(0, 0, dim.width, dim.height);
					Rectangle cellBounds = new Rectangle(compX, compY, cellW, cellH);

					compBounds.x += margins.left;
					compBounds.y += margins.top;

					getFill(comp).scale(compBounds, cellBounds);
					getAnchor(comp).translate(compBounds, cellBounds);

					comp.setBounds(compBounds);

					for (int i = 0; i < getColSpan(comp); i++, colIndex++)
					{
						rowX += columnWidths[colIndex] + mSpacingX;
					}
					realX -= cellW;
					realY -= cellH;
				}

				rowY += mRowHeights.get(iy) + mSpacingY;
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


	@Override
	public Dimension maximumLayoutSize(Container aTarget)
	{
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}


	private Fill getFill(Component aComponent)
	{
		LayoutParams params = mParams.getOrDefault(aComponent, mDefaultParams);
		return params.mFill != null ? params.mFill : mDefaultParams.mFill;
	}


	private Anchor getAnchor(Component aComponent)
	{
		LayoutParams params = mParams.getOrDefault(aComponent, mDefaultParams);
		return params.mAnchor != null ? params.mAnchor : mDefaultParams.mAnchor;
	}


	private Insets getMargins(Component aComponent)
	{
		LayoutParams params = mParams.getOrDefault(aComponent, mDefaultParams);
		return params.mMargins != null ? params.mMargins : mDefaultParams.mMargins;
	}


	private Dimension getPadding(Component aComponent)
	{
		LayoutParams params = mParams.getOrDefault(aComponent, mDefaultParams);
		return params.mPadding != null ? params.mPadding : mDefaultParams.mPadding;
	}


	private int getColSpan(Component aComponent)
	{
		return mParams.getOrDefault(aComponent, mDefaultParams).mColSpan;
	}


	public static class LayoutParams
	{
		Fill mFill;
		Anchor mAnchor;
		Dimension mPadding;
		Insets mMargins;
		int mColSpan = 1;
		int mRowSpan = 1;


		public LayoutParams()
		{
		}


		public Insets getMargins()
		{
			return mMargins;
		}


		public LayoutParams setMargins(Insets aMargins)
		{
			this.mMargins = aMargins;
			return this;
		}


		public LayoutParams setFill(Fill aFill)
		{
			this.mFill = aFill;
			return this;
		}


		public LayoutParams setAnchor(Anchor aAnchor)
		{
			this.mAnchor = aAnchor;
			return this;
		}


		public LayoutParams setPadding(Dimension aPadding)
		{
			this.mPadding = aPadding;
			return this;
		}


		public LayoutParams setColSpan(int aColSpan)
		{
			this.mColSpan = aColSpan;
			return this;
		}


		public LayoutParams setRowSpan(int aRowSpan)
		{
			this.mRowSpan = aRowSpan;
			return this;
		}
	}
}
