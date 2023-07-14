package org.terifan.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JLabel;


public class TitleLabel extends JLabel
{
	private final static long serialVersionUID = 1L;

	protected Color mLineColor;


	public TitleLabel(String aText, Color aLineColor)
	{
		super(aText);
		super.setOpaque(true);
		mLineColor = aLineColor;
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		int w = getWidth();
		int h = getHeight();

		TextBox textBox = new TextBox(getText()).setBounds(0, 0, w, h).setAnchor(Anchor.WEST).setForeground(getForeground()).setBackground(getBackground());

		switch (getHorizontalAlignment())
		{
			case CENTER:
				textBox.setAnchor(Anchor.CENTER);
				break;
			case RIGHT:
				textBox.setAnchor(Anchor.EAST);
				break;
			default:
				textBox.setAnchor(Anchor.WEST);
				break;
		}

		Rectangle rect = textBox.render(aGraphics, true).measure();

		switch (getHorizontalAlignment())
		{
			case CENTER:
				aGraphics.setColor(mLineColor);
				aGraphics.drawLine(0, rect.height / 2, rect.x - 5, rect.height / 2);
				aGraphics.drawLine(rect.x + rect.width + 5, rect.height / 2, w, rect.height / 2);
				break;
			case RIGHT:
				aGraphics.setColor(mLineColor);
				aGraphics.drawLine(0, rect.height / 2, rect.x - 5, rect.height / 2);
				break;
			default:
				aGraphics.setColor(mLineColor);
				aGraphics.drawLine(rect.x + rect.width + 5, rect.height / 2, w, rect.height / 2);
				break;
		}
	}
}
