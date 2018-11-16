package org.terifan.ui.propertygrid;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JTextField;


public class ColorChooser extends JTextField implements Cloneable
{
	protected String mOldValue;


	public ColorChooser(String aColor)
	{
		setText(aColor);
	}


	@Override
	public void setText(String aText)
	{
		Color color = decodeColor(aText);

		if (color == null && mOldValue == null)
		{
			color = new Color(128, 128, 128);
		}
		else if (color == null)
		{
			setColor(decodeColor(mOldValue));
			return;
		}

		setColor(color);

		mOldValue = aText;
	}


	public ColorChooser setColor(Color aColor)
	{
		if (aColor.getAlpha() != 255)
		{
			super.setText(aColor.getRed() + "," + aColor.getGreen() + "," + aColor.getBlue() + "," + aColor.getAlpha());
		}
		else
		{
			super.setText(aColor.getRed() + "," + aColor.getGreen() + "," + aColor.getBlue());
		}
		return this;
	}


	public Color getColor()
	{
		Color color = decodeColor(getText());
		if (color == null)
		{
			color = decodeColor(mOldValue);
		}
		return color;
	}


	private Color decodeColor(String aColor)
	{
		Color color;

		try
		{
			if (aColor.startsWith("["))
			{
				aColor = aColor.substring(1);
			}
			if (aColor.endsWith("]"))
			{
				aColor = aColor.substring(0, aColor.length() - 1);
			}

			String[] compStr = aColor.split(",");

			if (compStr.length == 1 && aColor.length() == 6)
			{
				return new Color(Integer.parseInt(aColor, 16));
			}

			int[] comp = new int[compStr.length];
			for (int i = 0; i < compStr.length; i++)
			{
				comp[i] = Integer.parseInt(compStr[i]);
			}

			if (compStr.length == 3)
			{
				color = new Color(comp[0], comp[1], comp[2]);
			}
			else
			{
				color = new Color(comp[0], comp[1], comp[2], comp[3]);
			}
		}
		catch (Exception e)
		{
			color = null;
		}

		return color;
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		if (isFocusOwner())
		{
			super.paintComponent(aGraphics);
			return;
		}

		Color color = getColor();
		mOldValue = super.getText();

		aGraphics.setColor(getBackground());
		aGraphics.fillRect(0, 0, getWidth(), getHeight());
		aGraphics.setColor(color);
		aGraphics.fillRect(0, 1, 12, 12);
		aGraphics.setColor(getForeground());
		aGraphics.drawRect(0, 1, 12, 12);
		aGraphics.drawString("[" + getText() + "]", 18, 11);
	}


	@Override
	protected ColorChooser clone() throws CloneNotSupportedException
	{
		return new ColorChooser(getText());
	}
}
