package org.terifan.ui.inputdialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;


class BevelBorder implements Border
{
	boolean top;


	public BevelBorder(boolean top)
	{
		this.top = top;
	}


	@Override
	public Insets getBorderInsets(Component c)
	{
		return new Insets(0, top ? 2 : 0, 0, top ? 0 : 2);
	}


	@Override
	public boolean isBorderOpaque()
	{
		return true;
	}


	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
	{
		if (top)
		{
			y += height - 2;
		}
		g.setColor(c.getBackground().darker());
		g.drawLine(x, y, x + width, y);
		g.setColor(Color.WHITE);
		g.drawLine(x, y + 1, x + width, y + 1);
	}
}
