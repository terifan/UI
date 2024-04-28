package org.terifan.ui.taginput;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;


class TagInputLayoutManager implements LayoutManager
{
	private final static int COL_SPACING = 4;
	private final static int ROW_SPACING = 2;

	private Tag mEditingTag;
	private JLabel mLabel;
	private JTextField mCreateField;
	private JTextField mEditorField;
	private ArrayList<Dimension> mLayoutDims;
	private ArrayList<ArrayList<Component>> mLayoutCmps;


	public TagInputLayoutManager(JLabel aLabel, JTextField aTextField, JTextField aEditorLabel)
	{
		mLabel = aLabel;
		mCreateField = aTextField;
		mEditorField = aEditorLabel;
		mLayoutDims = new ArrayList<>();
		mLayoutCmps = new ArrayList<>();
	}


	public void setEditingLabel(Tag aEditingLabel)
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
		Insets insets = aTarget.getInsets();

		Dimension rowDim;
		ArrayList<Component> rowCmp;

		mLayoutCmps.clear();
		mLayoutDims.clear();
		mLayoutCmps.add(rowCmp = new ArrayList<>());
		mLayoutDims.add(rowDim = new Dimension());

		for (Component comp : rearrangeComponents(aTarget))
		{
			if (comp instanceof Tag || comp == mLabel || comp == mCreateField)
			{
				Dimension dim = comp.getPreferredSize();
				if (rowDim.width + COL_SPACING + dim.width > aTargetWidth)
				{
					mLayoutCmps.add(rowCmp = new ArrayList<>());
					mLayoutDims.add(rowDim = new Dimension());
				}
				rowDim.width += dim.width + COL_SPACING;
				rowDim.height = Math.max(rowDim.height, dim.height);
				rowCmp.add(comp);
			}
		}

		int totalWidth = 0;
		int totalHeight = 0;
		for (Dimension dim : mLayoutDims)
		{
			totalWidth = Math.max(dim.width - COL_SPACING, totalWidth);
			totalHeight += dim.height + ROW_SPACING;
		}

		totalWidth += insets.left + insets.right;
		totalHeight += insets.top + insets.bottom - ROW_SPACING;

		return new Dimension(totalWidth, totalHeight);
	}


	@Override
	public void layoutContainer(Container aTarget)
	{
		synchronized (aTarget.getTreeLock())
		{
			computeSize(aTarget, aTarget.getWidth());

			Insets insets = aTarget.getInsets();

			int y = insets.top;

			for (int row = 0; row < mLayoutDims.size(); row++)
			{
				Dimension layout = mLayoutDims.get(row);

				for (int col = 0, x = insets.left; col < mLayoutCmps.get(row).size(); col++)
				{
					Component comp = mLayoutCmps.get(row).get(col);
					Dimension dim = comp.getPreferredSize();

					if (comp == mEditingTag)
					{
						comp.setVisible(false);
						comp = mEditorField;
					}
					comp.setVisible(true);
					comp.setBounds(x, y, dim.width, layout.height);

					x += dim.width + COL_SPACING;
				}

				y += layout.height + ROW_SPACING;
			}
		}
	}


	private ArrayList<Component> rearrangeComponents(Container aTarget)
	{
		ArrayList<Component> components = new ArrayList<>(Arrays.asList(aTarget.getComponents()));
		components.remove(mLabel);
		components.remove(mCreateField);
		components.addFirst(mLabel);
		components.addLast(mCreateField);
		return components;
	}
}
