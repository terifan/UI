package org.terifan.ui.propertygrid;

import javax.swing.JCheckBox;


public class CheckBoxProperty extends Property<JCheckBox, Boolean>
{
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
	public Property clone() throws CloneNotSupportedException
	{
		return cloneImpl();
	}


	@Override
	public String toString()
	{
		return Boolean.toString(((JCheckBox)mValueComponent).isSelected());
	}
}
