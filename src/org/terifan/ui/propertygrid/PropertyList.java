package org.terifan.ui.propertygrid;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class PropertyList extends JComponent implements Iterable<Property>, Cloneable
{
	protected Property mProperty;
	protected ArrayList<Property> mElements;
	protected Function<PropertyList, String> mFunction;
	protected String mLabel;

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


	public PropertyList(String aLabel)
	{
		mElements = new ArrayList<>();
		mFunction = DEFAULT_FORMATTER;
		mLabel = aLabel;
	}


	public String getLabel()
	{
		return mLabel;
	}


	public void setLabel(String aLabel)
	{
		this.mLabel = aLabel;
	}


	protected void bindProperty(Property aProperty)
	{
		mProperty = aProperty;
	}


	public PropertyList addProperty(String aLabel, Object aValue)
	{
		mElements.add(new Property(aLabel, aValue));
		return this;
	}


	public PropertyList addProperty(Property aProperty)
	{
		mElements.add(aProperty);
		return this;
	}


	public PropertyList addProperty(PropertyList aPropertyList)
	{
		Property property = new Property(aPropertyList.getLabel(), aPropertyList);
		mElements.add(property);
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
		StyleSheet style = mProperty.getPropertyGrid().getStyleSheet();
		Color foregound;
		Color background;
		Font font;

		if (mProperty.isGroup())
		{
			font = style.getFont("group_font_value");
			background = style.getColor("indent_background");
			foregound = style.getColor("text_foreground_readonly");
		}
		else
		{
			font = style.getFont("group_font_value");
			background = style.getColor("text_background_readonly");
			foregound = style.getColor("text_foreground_readonly");
		}

		new TextBox(getPresentationValue()).setFont(font).setForeground(foregound).setBackground(background).setBounds(0, 0, getWidth(), getHeight()).setAnchor(Anchor.WEST).setMargins(0, mProperty.isGroup() ? 4 : 0, 0, 0).render(aGraphics);
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
