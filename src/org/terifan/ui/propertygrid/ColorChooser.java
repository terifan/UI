package org.terifan.ui.propertygrid;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JTextField;


public class ColorChooser extends Property implements Cloneable
{
	protected String mValue;


	public ColorChooser(String aLabel, String aColor)
	{
		super(aLabel);

		mValue = aColor;
	}


	public ColorChooser setColor(Color aColor)
	{
		if (aColor.getAlpha() != 255)
		{
			((JTextField)mValueComponent).setText(aColor.getRed() + "," + aColor.getGreen() + "," + aColor.getBlue() + "," + aColor.getAlpha());
		}
		else
		{
			((JTextField)mValueComponent).setText(aColor.getRed() + "," + aColor.getGreen() + "," + aColor.getBlue());
		}
		return this;
	}


	public Color getColor()
	{
		Color color = decodeColor(((JTextField)mValueComponent).getText());
		if (color == null)
		{
			color = decodeColor(mValue);
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
	protected JComponent createValueComponent()
	{
		JTextField c = new JTextField()
		{
			@Override
			public void setText(String aText)
			{
				Color color = decodeColor(aText);

				if (color == null && mValue == null)
				{
					color = new Color(128, 128, 128);
				}
				else if (color == null)
				{
					setColor(decodeColor(mValue));
					return;
				}

				setColor(color);

				mValue = aText;
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
				mValue = super.getText();

				aGraphics.setColor(getBackground());
				aGraphics.fillRect(0, 0, getWidth(), getHeight());
				aGraphics.setColor(color);
				aGraphics.fillRect(0, 1, 12, 12);
				aGraphics.setColor(getForeground());
				aGraphics.drawRect(0, 1, 12, 12);
				aGraphics.drawString("[" + getText() + "]", 18, 11);
			}
		};

		c.setBorder(null);
		c.setCaretColor(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.addActionListener(e -> mValueComponent.repaint());
		c.setForeground(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.setBackground(mPropertyGrid.mStyleSheet.getColor("text_background"));

		return c;
	}


	@Override
	public String toString()
	{
		return "[" + ((JTextField)mValueComponent).getText() + "]";
	}
}
