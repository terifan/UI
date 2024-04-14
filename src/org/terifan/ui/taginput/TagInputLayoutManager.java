package org.terifan.ui.taginput;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTextField;


class TagInputLayoutManager implements LayoutManager
{
	private final static int COL_SPACING = 4;
	private final static int ROW_SPACING = 2;

	private XTag mEditingTag;
	private JLabel mLabel;
	private JTextField mTextField;
	private ArrayList<Rectangle> mLayoutInfo;


	public TagInputLayoutManager(JLabel aLabel, JTextField aTextField)
	{
		mLabel = aLabel;
		mTextField = aTextField;
		mLayoutInfo = new ArrayList<>();
	}


	public void setEditingLabel(XTag aEditingLabel)
	{
		mEditingTag = aEditingLabel;
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
		return new Dimension(8, 8);
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

		aTargetWidth -= aTarget.getInsets().left + aTarget.getInsets().right;

		mLayoutInfo.clear();

		int n = aTarget.getComponentCount();

		int rowHeight = 0;
		for (int i = 0; i < n; i++)
		{
			Component comp = aTarget.getComponent(i);
			rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
		}

		int rowCount = 0;
		for (int i = 0; i < n; i++)
		{
			Component comp = aTarget.getComponent(i);
			if (comp instanceof JLabel)
			{
				Dimension d = comp.getPreferredSize();
				if (rowWidth + d.width >= aTargetWidth)
				{
					totalWidth = Math.max(totalWidth, rowWidth);
					totalHeight += rowHeight;
					mLayoutInfo.add(new Rectangle(rowCount, 0, rowWidth, rowHeight));
					rowWidth = 0;
					rowCount = 0;
				}
				rowWidth += (rowCount > 0 ? COL_SPACING : 0) + d.width;
				rowCount++;
			}
		}

		if (mEditingTag == null)
		{
			Dimension d = mTextField.getPreferredSize();
			if (rowWidth + d.width >= aTargetWidth)
			{
				totalWidth = Math.max(totalWidth, rowWidth);
				totalHeight += rowHeight;
				mLayoutInfo.add(new Rectangle(rowCount, 0, rowWidth, rowHeight));
				rowWidth = 0;
				rowCount = 0;
			}
			rowWidth += (rowCount > 0 ? COL_SPACING : 0) + d.width;
			rowCount++;
		}

		totalWidth = Math.max(totalWidth, rowWidth);
		totalHeight += rowHeight + ROW_SPACING * mLayoutInfo.size();

		Insets insets = aTarget.getInsets();
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

			Dimension d = mLabel.getPreferredSize();
			mLabel.setBounds(x, y, d.width, layout.height);
			x += d.width + COL_SPACING;

			for (int i = 0, col = 1, row = 0; i < n; i++)
			{
				Component comp = aTarget.getComponent(i);
				d = comp.getPreferredSize();
				if (comp instanceof XTag)
				{
					layout = mLayoutInfo.get(row);
					if (comp == mEditingTag)
					{
						comp.setVisible(false);
						comp = mTextField;
					}
					comp.setVisible(true);
					comp.setBounds(x, y, d.width, layout.height);

					x += d.width + COL_SPACING;
					col++;
				}

				if (col >= layout.x)
				{
					x = insets.left;
					y += layout.height + ROW_SPACING;
					row++;
					col = 0;
				}
			}

			if (mEditingTag == null)
			{
				d = mTextField.getPreferredSize();
				mTextField.setBounds(x, y, d.width, d.height);
			}
		}
	}
}
