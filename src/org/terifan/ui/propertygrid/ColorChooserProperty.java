package org.terifan.ui.propertygrid;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.JTextField;
import org.terifan.bundle.Array;
import org.terifan.bundle.Bundle;


public class ColorChooserProperty extends Property<JTextField, Color> implements Cloneable, Serializable
{
	private static final long serialVersionUID = 1L;

	protected Color mColor;


	public ColorChooserProperty(String aLabel, Color aColor)
	{
		super(aLabel);

		mColor = aColor;
	}


	@Override
	public Color getValue()
	{
		return mColor;
	}


	@Override
	public ColorChooserProperty setValue(Color aColor)
	{
		mColor = aColor;

		if (mValueComponent != null)
		{
			mValueComponent.setText(toColorString());
		}

		return this;
	}


	@Override
	protected void updateValue()
	{
		String text = mValueComponent.getText();

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


	@Override
	void marshal(Bundle aBundle)
	{
		if (mColor.getAlpha() != 255)
		{
			aBundle.putArray(mLabel, new Array().add(mColor.getRed(), mColor.getGreen(), mColor.getBlue(), mColor.getAlpha()));
		}
		else
		{
			aBundle.putArray(mLabel, new Array().add(mColor.getRed(), mColor.getGreen(), mColor.getBlue()));
		}
	}


	@Override
	public String toString()
	{
		return "[" + mValueComponent.getText() + "]";
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
				aGraphics.drawString("[" + mValueComponent.getText() + "]", 18, 11);
			}
		};

		c.setBorder(null);
		c.setCaretColor(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.addActionListener(e -> mValueComponent.repaint());
		c.setForeground(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.setBackground(mPropertyGrid.mStyleSheet.getColor("text_background"));

		return c;
	}


	private String toColorString()
	{
		if (mColor.getAlpha() != 255)
		{
			return mColor.getRed() + "," + mColor.getGreen() + "," + mColor.getBlue() + "," + mColor.getAlpha();
		}

		return mColor.getRed() + "," + mColor.getGreen() + "," + mColor.getBlue();
	}
}
