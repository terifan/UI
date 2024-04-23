package org.terifan.ui.buttonlist;

import java.awt.Color;
import org.terifan.ui.Anchor;
import org.terifan.ui.buttonlist.FilterButtonModel.State;
import static org.terifan.ui.buttonlist.FilterButtonModel.State.EXCLUDE;
import static org.terifan.ui.buttonlist.FilterButtonModel.State.INCLUDE;
import static org.terifan.ui.buttonlist.FilterButtonModel.State.NORMAL;
import static org.terifan.ui.buttonlist.FilterButtonModel.State.SHADED;


public class FilterButtonStyle
{
	public Color textColorIncluded = new Color(100, 100, 255);
	public Color textColorNormal = new Color(230, 230, 230);
	public Color backgroundColor = new Color(23, 23, 23);
	public Color textColorShaded = new Color(96,96,96);
	public Color textColorExcluded = new Color(255, 0, 0);
	public int padding = 10;
	public Integer minWidth;
	public Anchor textAlignment = Anchor.WEST;


	protected Color getTextColor(State aState)
	{
		switch (aState)
		{
			case EXCLUDE:
				return textColorExcluded;
			case INCLUDE:
				return textColorIncluded;
			case NORMAL:
				return textColorNormal;
			case SHADED:
				return textColorShaded;
			default:
				return Color.WHITE;
		}
	}
}
