package org.terifan.ui.propertygrid;

import java.io.Serializable;
import javax.swing.JComboBox;


public class ComboBoxProperty extends Property<JComboBox, Object> implements Serializable
{
	private static final long serialVersionUID = 1L;

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
	public String toString()
	{
		return ((JComboBox)mValueComponent).getSelectedItem().toString();
	}
}
