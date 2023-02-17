package org.terifan.ui.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;


public final class ChangeUIFont
{
	/**
	 * Set the Font for all UI components. Pass a null value to NOT change that specific property.
	 */
	public static void setFont(String aFamily, Integer aStyle, Integer aSize)
	{
		if (aFamily == null && aStyle == null && aSize == null)
		{
			return;
		}

		Enumeration keys = UIManager.getDefaults().keys();

		while (keys.hasMoreElements())
		{
			Object key = keys.nextElement();
			Object value = UIManager.get(key);

			if (value instanceof FontUIResource)
			{
				FontUIResource orig = (FontUIResource)value;

				Font font = new Font(
					aFamily != null ? aFamily : orig.getFamily(),
					aStyle != null ? aStyle : orig.getStyle(),
					aSize != null ? aSize : orig.getSize()
				);

				UIManager.put(key, new FontUIResource(font));
			}
		}
	}


	/**
	 * Set the Font for all UI components.
	 */
	public static void setFont(Font aFont)
	{
		Enumeration keys = UIManager.getDefaults().keys();

		while (keys.hasMoreElements())
		{
			Object key = keys.nextElement();
			Object value = UIManager.get(key);

			if (value instanceof FontUIResource)
			{
				FontUIResource orig = (FontUIResource)value;
				UIManager.put(key, new FontUIResource(aFont));
			}
		}
	}


	/**
	 * Set the Font this component and all it's children. Pass a null value to NOT change that specific property.
	 */
	public static void changeFont(Component component, String aFamily, Integer aStyle, Integer aSize)
	{
		Font orig = component.getFont();

		Font font = new Font(
			aFamily != null ? aFamily : orig.getFamily(),
			aStyle != null ? aStyle : orig.getStyle(),
			aSize != null ? aSize : orig.getSize()
		);

		component.setFont(font);

		if (component instanceof Container)
		{
			for (Component child : ((Container)component).getComponents())
			{
				changeFont(child, aFamily, aStyle, aSize);
			}
		}
	}


	/**
	 * Set the Font this component and all it's children.
	 */
	public static void changeFont(Component component, Font aFont)
	{
		component.setFont(aFont);

		if (component instanceof Container)
		{
			for (Component child : ((Container)component).getComponents())
			{
				changeFont(child, aFont);
			}
		}
	}
}
