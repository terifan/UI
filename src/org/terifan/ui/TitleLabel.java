package org.terifan.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JLabel;


public class TitleLabel extends JLabel
{
	private Color mLineColor;


	public TitleLabel(String aText, Color aLineColor)
	{
		super(aText);
		super.setOpaque(true);
		mLineColor = aLineColor;
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		TextBox textBox = new TextBox(getText()).setBounds(0, 0, getWidth(), getHeight()).setAnchor(Anchor.WEST).setForeground(getForeground()).setBackground(getBackground());

		if (getHorizontalAlignment() == CENTER)
		{
			textBox.setAnchor(Anchor.CENTER);
		}
		else if (getHorizontalAlignment() == RIGHT)
		{
			textBox.setAnchor(Anchor.EAST);
		}

		Rectangle rect = textBox.render(aGraphics, true).measure();

		aGraphics.setColor(mLineColor);
		if (getHorizontalAlignment() == CENTER)
		{
			aGraphics.drawLine(0, rect.height / 2, rect.x - 5, rect.height / 2);
			aGraphics.drawLine(rect.x + rect.width + 5, rect.height / 2, getWidth(), rect.height / 2);
		}
		else if (getHorizontalAlignment() == RIGHT)
		{
			aGraphics.drawLine(0, rect.height / 2, rect.x - 5, rect.height / 2);
		}
		else
		{
			aGraphics.drawLine(rect.x + rect.width + 5, rect.height / 2, getWidth(), rect.height / 2);
		}
	}
}
