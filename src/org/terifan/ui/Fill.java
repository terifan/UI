package org.terifan.ui;

import java.awt.Rectangle;



public enum Fill
{
	NONE,
	HORIZONTAL,
	VERTICAL,
	BOTH;


	public void scale(Rectangle aBounds, Rectangle aOuter)
	{
		if (this == HORIZONTAL || this == BOTH) aBounds.width = aOuter.width;
		if (this == VERTICAL || this == BOTH) aBounds.height = aOuter.height;
	}
}
