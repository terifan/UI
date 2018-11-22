package org.terifan.ui.propertygrid;

import javax.swing.JComboBox;


public class ComboBoxProperty extends Property<JComboBox, Object>
{
	private int mSelectedIndex;
	private Object[] mOptions;


	public ComboBoxProperty(String aLabel, Object[] aOptions, int aSelectedIndex)
	{
		super(aLabel);

		mOptions = aOptions;
		mSelectedIndex = aSelectedIndex;
	}


	@Override
	protected JComboBox createValueComponent()
	{
		JComboBox c = new JComboBox(mOptions);
		c.setSelectedIndex(mSelectedIndex);
		c.setOpaque(false);
		c.addActionListener(e -> mValueComponent.repaint());

		return c;
	}


	@Override
	public Object getValue()
	{
		return mValueComponent.getSelectedItem();
	}


	@Override
	public ComboBoxProperty setValue(Object aValue)
	{
		mValueComponent.setSelectedItem(aValue);
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
		return ((JComboBox)mValueComponent).getSelectedItem().toString();
	}
}
