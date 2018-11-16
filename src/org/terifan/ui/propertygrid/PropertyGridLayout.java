package org.terifan.ui.propertygrid;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.font.FontRenderContext;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComponent;


class PropertyGridLayout implements LayoutManager //, PropertyGridModel.Callback
{
	public PropertyGridLayout()
	{
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
	public Dimension preferredLayoutSize(Container target)
	{
		return ((PropertyGridListPane)target).getPreferredSize();
	}


	@Override
	public Dimension minimumLayoutSize(Container target)
	{
		return ((PropertyGridListPane)target).getPreferredSize();
	}


	@Override
	public void layoutContainer(Container target)
	{
		synchronized (target.getTreeLock())
		{
			PropertyGrid propertyGrid = ((PropertyGridListPane)target).mPropertyGrid;
			PropertyGridModel model = propertyGrid.getModel();
			StyleSheet style = propertyGrid.getStyleSheet();
			int rowHeight = propertyGrid.getRowHeight();

			int y = 0;
			int dividerX = propertyGrid.getDividerPosition();
			int width = target.getWidth();
			int indentWidth = style.getInt("indent_width");

			for (Iterator<Property> it = model.getRecursiveIterator(); it.hasNext();)
			{
				layoutPropertyComponents(propertyGrid, it.next(), dividerX, y, width, indentWidth, rowHeight);

				y += rowHeight;
			}
		}
	}


	protected void layoutPropertyComponents(PropertyGrid propertyGrid, Property item, int dividerX, int y, int width, int indentWidth, int rowHeight)
	{
		PropertyGridModel model = propertyGrid.getModel();

		int indent = model.getIndent(item) + 1;

		item.getIndentComponent().setBounds(0, y, indent * indentWidth, rowHeight - 1);

		JComponent component = item.getValueComponent();

		JButton button = item.getDetailButton();

		int btnWidth = 0;
		if (button != null)
		{
			btnWidth = button.getPreferredSize().width;
			button.setBounds(width - btnWidth, y, btnWidth, rowHeight - 1);
			btnWidth += 4;
		}

		component.setBounds(dividerX + (item.isGroup() ? 0 : 4), y, width - dividerX - btnWidth - (item.isGroup() ? 0 : 4), rowHeight - 1);

		component = item.getLabelComponent();
		component.setBounds(indent * indentWidth, y, dividerX - (indent * indentWidth), rowHeight - 1);
	}
}
