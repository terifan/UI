package org.terifan.ui;

import java.awt.Rectangle;


public enum Alignment
{
	LEFT,
	CENTER,
	RIGHT,
	JUSTIFY;


	public void translate(Rectangle oBounds, Rectangle aOuter)
	{
		switch (this)
		{
			case LEFT:
				oBounds.x = aOuter.x;
				break;
			case CENTER:
				oBounds.x = aOuter.x + (aOuter.width - oBounds.width) / 2;
				break;
			case RIGHT:
				oBounds.x = aOuter.x + aOuter.width - oBounds.width;
				break;
			default:
				oBounds.width = aOuter.width;
				break;
		}
	}
}
