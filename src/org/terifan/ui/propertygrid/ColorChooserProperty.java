package org.terifan.ui.propertygrid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;


public class ColorChooserProperty extends Property<JTextField, Color> implements Cloneable
{
	protected Color mColor;


	public ColorChooserProperty(String aLabel, Color aColor)
	{
		super(aLabel);

		mColor = aColor;
	}


	public ColorChooserProperty setColor(Color aColor)
	{
		mColor = aColor;

		JTextField comp = (JTextField)mValueComponent;
		if (comp != null)
		{
			comp.setText(toColorString());
		}

		return this;
	}


	private void decodeColor()
	{
		String text = getText();

		try
		{
			if (text.startsWith("["))
			{
				text = text.substring(1);
			}
			if (text.endsWith("]"))
			{
				text = text.substring(0, text.length() - 1);
			}

			String[] compStr = text.split(",");

			if (compStr.length == 1 && text.length() == 6)
			{
				mColor = new Color(Integer.parseInt(text, 16));
			}
			else
			{
				int[] comp = new int[compStr.length];
				for (int i = 0; i < compStr.length; i++)
				{
					comp[i] = Integer.parseInt(compStr[i]);
				}

				if (compStr.length == 3)
				{
					mColor = new Color(comp[0], comp[1], comp[2]);
				}
				else
				{
					mColor = new Color(comp[0], comp[1], comp[2], comp[3]);
				}
			}
		}
		catch (Exception e)
		{
		}
	}


	private String getText()
	{
		return ((JTextField)mValueComponent).getText();
	}


	private String toColorString()
	{
		if (mColor.getAlpha() != 255)
		{
			return mColor.getRed() + "," + mColor.getGreen() + "," + mColor.getBlue() + "," + mColor.getAlpha();
		}

		return mColor.getRed() + "," + mColor.getGreen() + "," + mColor.getBlue();
	}


	@Override
	protected JTextField createValueComponent()
	{
		JTextField c = new JTextField(toColorString())
		{
			@Override
			protected void paintComponent(Graphics aGraphics)
			{
				if (isFocusOwner())
				{
					super.paintComponent(aGraphics);
					return;
				}

				aGraphics.setColor(getBackground());
				aGraphics.fillRect(0, 0, getWidth(), getHeight());
				aGraphics.setColor(mColor);
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
		c.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent aE)
			{
				decodeColor();
			}
		});

		return c;
	}


	@Override
	public Color getValue()
	{
		return mColor;
	}


	@Override
	public String toString()
	{
		return "[" + getText() + "]";
	}
}
