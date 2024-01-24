package org.terifan.ui;

import java.awt.Rectangle;


public enum Anchor
{
	NORTH_WEST,
	NORTH,
	NORTH_EAST,
	CENTER,
	SOUTH_WEST,
	SOUTH,
	SOUTH_EAST,
	WEST,
	EAST;


	public void translate(Rectangle oBounds, Rectangle aOuter)
	{
		switch (this)
		{
			case WEST:
			case NORTH_WEST:
			case SOUTH_WEST:
				oBounds.x = aOuter.x;
				break;
			case CENTER:
			case NORTH:
			case SOUTH:
				oBounds.x = aOuter.x + (aOuter.width - oBounds.width) / 2;
				break;
			default:
				oBounds.x = aOuter.x + aOuter.width - oBounds.width;
				break;
		}
		switch (this)
		{
			case NORTH_WEST:
			case NORTH:
			case NORTH_EAST:
				oBounds.y = aOuter.y;
				break;
			case WEST:
			case CENTER:
			case EAST:
				oBounds.y = aOuter.y + (aOuter.height - oBounds.height) / 2;
				break;
			default:
				oBounds.y = aOuter.y + aOuter.height - oBounds.height;
				break;
		}
	}
}
