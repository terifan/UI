package org.terifan.ui.propertygrid;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


class PropertyClickListener extends MouseAdapter
{
	private Property mProperty;
	private boolean mIndent;


	PropertyClickListener(Property aProperty, boolean aIndent)
	{
		mProperty = aProperty;
		mIndent = aIndent;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		PropertyGrid propertyGrid = mProperty.getPropertyGrid();
		StyleSheet style = propertyGrid.getStyleSheet();
		int indent = propertyGrid.getModel().getIndent(mProperty) + 1;
		int indentWidth = style.getInt("indent_width");

		if (mProperty.getChildren().getItemCount() > 0)
		{
			boolean clicked = false;

			if (mIndent)
			{
				int x = (indent - 1) * indentWidth;
				clicked = aEvent.getX() >= x && aEvent.getX() <= x + indentWidth;
			}

			if (clicked)
			{
				mProperty.setCollapsed(!mProperty.getCollapsed());
				propertyGrid.setModel(propertyGrid.getModel());
				propertyGrid.redisplay();
				return;
			}
		}

		if (mProperty.getValueComponent() != null)
		{
			mProperty.getValueComponent().requestFocus();
		}
	}
}
