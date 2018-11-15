package org.terifan.ui.propertygrid;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import org.terifan.ui.Anchor;
import org.terifan.ui.Utilities;


class PropertyGridLabel extends JComponent
{
	protected Property mProperty;


	public PropertyGridLabel(Property aProperty)
	{
		mProperty = aProperty;

		if (mProperty.getPropertyGrid().getModel().getIndent(mProperty) == 0)
		{
			setFocusable(true);
		}

		addMouseListener(new PropertyClickListener(mProperty, false));
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		PropertyGrid propertyGrid = mProperty.getPropertyGrid();
		StyleSheet style = propertyGrid.getStyleSheet();
		boolean selected = propertyGrid.getSelectedProperty() == mProperty;

		Color foreground, background;
		Font font;

		if (mProperty.isGroup())
		{
			foreground = style.getColor("indent_foreground");
			background = style.getColor("indent_background");
			font = style.getFont("group");
		}
		else
		{
			if (selected && !mProperty.isReadOnly())
			{
				boolean focusOwner = Utilities.isWindowFocusOwner(propertyGrid);
				foreground = focusOwner ? style.getColor("text_foreground_selected") : style.getColor("text_background");
				background = focusOwner ? style.getColor("text_background_selected") : style.getColor("grid");
			}
			else
			{
				foreground = style.getColor("text_foreground");
				background = style.getColor("text_background");
			}
			font = style.getFont("item");
		}

		aGraphics.setFont(font);
		aGraphics.setColor(background);
		aGraphics.fillRect(0, 0, getWidth(), getHeight());

		Rectangle dim = propertyGrid.getTextRenderer().drawString(aGraphics, mProperty.getLabel(), 2, 1, getWidth() - 2, getHeight(), Anchor.WEST, foreground, background, false);

		if (selected && (mProperty.isGroup() || mProperty.isReadOnly()))
		{
			aGraphics.setColor(foreground);
			Utilities.drawDottedRect(aGraphics, 0, 0, dim.width + 2, dim.height + 2, true);
		}
	}
}
