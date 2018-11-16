package org.terifan.ui.propertygrid;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


class PropertyClickListener extends MouseAdapter
{
	private Property mProperty;
	private boolean mIndentComponent;


	PropertyClickListener(Property aProperty, boolean aIndentComponent)
	{
		mProperty = aProperty;
		mIndentComponent = aIndentComponent;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		if (mIndentComponent && mProperty.getChildren().getItemCount() > 0)
		{
			PropertyGrid propertyGrid = mProperty.getPropertyGrid();

			int indent = propertyGrid.getModel().getIndent(mProperty);
			int indentWidth = propertyGrid.getStyleSheet().getInt("indent_width");
			int x = indent * indentWidth;

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
