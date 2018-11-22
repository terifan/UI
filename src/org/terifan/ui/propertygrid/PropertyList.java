package org.terifan.ui.propertygrid;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;
import javax.swing.JLabel;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class PropertyList extends Property<JLabel, String> implements Iterable<Property>, Cloneable
{
	protected ArrayList<Property> mChildren;

	private final static Function<PropertyList, String> DEFAULT_VALUE = aList ->
	{
		StringBuilder sb = new StringBuilder();
		for (Property p : aList.mChildren)
		{
			if (!(p instanceof PropertyList))
			{
				if (sb.length() > 0)
				{
					sb.append("; ");
				}
				sb.append(p);
			}
		}

		return sb.toString();
	};


	public PropertyList(String aLabel)
	{
		this(false, aLabel);
	}


	public PropertyList(boolean aGroup, String aLabel)
	{
		super(aLabel);

		mGroup = aGroup;
		mChildren = new ArrayList<>();
	}


	public PropertyList addProperty(String aLabel, Object aValue)
	{
		if (aValue == null)
		{
			throw new IllegalArgumentException("aValue is null");
		}

		if (aValue instanceof Boolean)
		{
			return addProperty(new CheckBoxProperty(aLabel, (boolean)aValue));
		}
		if (aValue instanceof Number)
		{
			return addProperty(new NumberProperty(aLabel, (Number)aValue));
		}
		if (aValue instanceof Color)
		{
			return addProperty(new ColorChooserProperty(aLabel, (Color)aValue));
		}
		if (aValue.getClass().isArray())
		{
			return addProperty(new ComboBoxProperty(aLabel, (Object[])aValue, 0));
		}

		return addProperty(new TextProperty(aLabel, aValue));
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
	protected JLabel createValueComponent()
	{
		return new JLabel()
		{
			@Override
			protected void paintComponent(Graphics aGraphics)
			{
				if (mPropertyGrid == null)
				{
					return;
				}

				StyleSheet style = mPropertyGrid.getStyleSheet();
				Color foregound;
				Color background;
				Font font;

				if (mGroup)
				{
					font = style.getFont("group_font_value");
					background = style.getColor("indent_background");
					foregound = style.getColor("indent_foreground_value");
				}
				else
				{
					font = style.getFont("group_font_value");
					background = style.getColor("text_background_readonly");
					foregound = style.getColor("text_foreground_readonly");
				}

				new TextBox(PropertyList.this.toString()).setFont(font).setForeground(foregound).setBackground(background).setBounds(0, 0, getWidth(), getHeight()).setAnchor(Anchor.WEST).setMargins(0, mGroup ? 4 : 0, 0, 0).render(aGraphics);
			}
		};
	}


	@Override
	public String getValue()
	{
		return mValueComponent.getText();
	}


	@Override
	public String toString()
	{
		return DEFAULT_VALUE.apply(PropertyList.this);
	}


	@Override
	public PropertyList clone() throws CloneNotSupportedException
	{
		PropertyList clone = (PropertyList)cloneImpl();

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
			if (item instanceof PropertyList && !item.isCollapsed())
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
