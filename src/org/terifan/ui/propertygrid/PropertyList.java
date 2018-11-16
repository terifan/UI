package org.terifan.ui.propertygrid;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class PropertyList extends Property implements Iterable<Property>, Cloneable
{
	protected ArrayList<Property> mChildren;

	private final static Function<PropertyList, String> DEFAULT_VALUE = aList ->
	{
		StringBuilder sb = new StringBuilder();
		if (aList.mChildren != null)
		{
			for (Property p : aList.mChildren)
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
		}
		return sb.toString();
	};


	public PropertyList(String aLabel)
	{
		super(aLabel, DEFAULT_VALUE);

		mChildren = new ArrayList<>();
	}


	public PropertyList addProperty(String aLabel, Object aValue)
	{
		mChildren.add(new Property(aLabel, aValue));
		return this;
	}


	public PropertyList addProperty(Property aProperty)
	{
		mChildren.add(aProperty);
		return this;
	}


	public int getItemCount()
	{
		return mChildren.size();
	}


	@Override
	public Iterator<Property> iterator()
	{
		return mChildren.iterator();
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		if (mPropertyGrid == null) return;
		StyleSheet style = mPropertyGrid.getStyleSheet();
		Color foregound;
		Color background;
		Font font;

		if (mGroup)
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

		new TextBox(((Function<Property,String>)mValue).apply(this)).setFont(font).setForeground(foregound).setBackground(background).setBounds(0, 0, getWidth(), getHeight()).setAnchor(Anchor.WEST).setMargins(0, mGroup ? 4 : 0, 0, 0).render(aGraphics);
	}


	@Override
	protected PropertyList clone() throws CloneNotSupportedException
	{
		PropertyList clone = (PropertyList)super.clone();

		clone.mChildren = new ArrayList<>();
		for (Property item : mChildren)
		{
			clone.mChildren.add(item.clone());
		}

		return clone;
	}


	protected void getRecursiveElements(ArrayList<Property> aList)
	{
		for (Property item : mChildren)
		{
			aList.add(item);
			if (item instanceof PropertyList && !item.getCollapsed())
			{
				((PropertyList)item).getRecursiveElements(aList);
			}
		}
	}


	protected ArrayList<Property> getChildren()
	{
		return mChildren;
	}
}
