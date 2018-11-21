package org.terifan.ui.propertygrid;

import javax.swing.JCheckBox;
import javax.swing.JComponent;


public class CheckBoxProperty extends Property
{
	public CheckBoxProperty(String aLabel, boolean aSelected)
	{
		super(aLabel);
	}


	@Override
	protected JComponent createValueComponent()
	{
		JCheckBox c = new JCheckBox();
		c.setOpaque(false);
		c.addActionListener(e -> mValueComponent.repaint());

		return c;
	}


	@Override
	public String toString()
	{
		return Boolean.toString(((JCheckBox)mValueComponent).isSelected());
	}
}
