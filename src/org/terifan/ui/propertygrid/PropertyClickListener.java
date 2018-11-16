package org.terifan.ui.propertygrid;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


class PropertyClickListener extends MouseAdapter
{
	private Property mProperty;
	private boolean mIndentComponent;


	PropertyClickListener(Property aProperty)
	{
		mProperty = aProperty;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		if (mProperty instanceof PropertyList && !((PropertyList)mProperty).getChildren().isEmpty())
		{
			PropertyGrid propertyGrid = mProperty.getPropertyGrid();

			int indentWidth = propertyGrid.getStyleSheet().getInt("indent_width");
			int x = mProperty.getIndent() * indentWidth;

			if (aEvent.getX() >= x && aEvent.getX() <= x + indentWidth)
			{
				mProperty.setCollapsed(!mProperty.getCollapsed());
				propertyGrid.setModel(propertyGrid.getModel());
				propertyGrid.redisplay();
			}
		}

		if (mProperty.getValueComponent() != null)
		{
			mProperty.getValueComponent().requestFocus();
		}
	}
}
