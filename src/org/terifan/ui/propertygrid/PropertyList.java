package org.terifan.ui.propertygrid;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;


public class PropertyList extends JComponent implements Iterable<Property>, Cloneable
{
	protected Property mProperty;
	protected ArrayList<Property> mElements;
	protected Function<PropertyList, String> mFunction;

	private Function<PropertyList, String> DEFAULT_FORMATTER = aList ->
	{
		StringBuilder sb = new StringBuilder();
		for (Property p : aList.mElements)
		{
			Object v = p.getValueComponent();
			if (!(v instanceof PropertyList))
			{
				if (sb.length() > 0)
				{
					sb.append("; ");
				}
				if (v instanceof JTextField)
				{
					sb.append(((JTextField)v).getText());
				}
				else if (v instanceof JComboBox)
				{
					sb.append(((JComboBox)v).getSelectedItem());
				}
				else if (v instanceof JCheckBox)
				{
					sb.append(((JCheckBox)v).isSelected());
				}
				else
				{
					sb.append(v);
				}
			}
		}
		return sb.toString();
	};


	public PropertyList()
	{
		mElements = new ArrayList<>();
		mFunction = DEFAULT_FORMATTER;
	}


	protected void bindProperty(Property aProperty)
	{
		mProperty = aProperty;
	}


	public PropertyList add(Property aProperty)
	{
		mElements.add(aProperty);
		return this;
	}


	public int getItemCount()
	{
		return mElements.size();
	}


	@Override
	public Iterator<Property> iterator()
	{
		return mElements.iterator();
	}


	public PropertyList setFormatter(Function<PropertyList, String> aFunction)
	{
		mFunction = aFunction;
		return this;
	}


	public String getPresentationValue()
	{
		return mFunction.apply(this);
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		String t = getPresentationValue();

		if (mProperty.isGroup())
		{
			StyleSheet style = mProperty.getPropertyGrid().getStyleSheet();
			aGraphics.setColor(style.getColor("indent_background"));
			aGraphics.fillRect(0, 0, getWidth(), getHeight());
			aGraphics.setColor(new Color(100,100,100));
		}
		else
		{
			aGraphics.setColor(new Color(128, 128, 128));
		}

		aGraphics.drawString(t, mProperty.isGroup() ? 4 : 0, 11);
	}


	@Override
	protected PropertyList clone() throws CloneNotSupportedException
	{
		PropertyList clone = (PropertyList)super.clone();

		clone.mElements = new ArrayList<>();
		for (Property item : mElements)
		{
			clone.mElements.add(item.clone());
		}

		return clone;
	}
}
