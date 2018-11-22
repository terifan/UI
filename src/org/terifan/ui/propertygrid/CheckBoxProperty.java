package org.terifan.ui.propertygrid;

import java.io.Serializable;
import javax.swing.JCheckBox;


public class CheckBoxProperty extends Property<JCheckBox, Boolean> implements Serializable
{
	private static final long serialVersionUID = 1L;


	public CheckBoxProperty(String aLabel, boolean aSelected)
	{
		super(aLabel);
	}


	@Override
	protected JCheckBox createValueComponent()
	{
		JCheckBox c = new JCheckBox();
		c.setOpaque(false);
		c.addActionListener(e -> mValueComponent.repaint());

		return c;
	}


	@Override
	public Boolean getValue()
	{
		return mValueComponent.isSelected();
	}


	@Override
	public CheckBoxProperty setValue(Boolean aValue)
	{
		mValueComponent.setSelected(aValue);
		return this;
	}


	@Override
	public String toString()
	{
		return Boolean.toString(((JCheckBox)mValueComponent).isSelected());
	}
}
