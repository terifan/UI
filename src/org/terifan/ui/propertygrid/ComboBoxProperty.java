package org.terifan.ui.propertygrid;

import java.io.Serializable;
import javax.swing.JComboBox;
import org.terifan.bundle.Bundle;


public class ComboBoxProperty extends Property<JComboBox, Object> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private int mSelectedIndex;
	private Object[] mOptions;
	private Object mValue;


	public ComboBoxProperty(String aLabel, Object[] aOptions, int aSelectedIndex)
	{
		super(aLabel);

		mOptions = aOptions;
		mSelectedIndex = aSelectedIndex;
		mValue = mOptions[mSelectedIndex];
	}


	@Override
	protected JComboBox createValueComponent()
	{
		JComboBox c = new JComboBox(mOptions);
//		c.setBackground(mPropertyGrid.mStyleSheet.getColor("text_background"));
//		c.setForeground(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.setSelectedIndex(mSelectedIndex);
		c.addActionListener(e -> mValueComponent.repaint());

		return c;
	}


	@Override
	public Object getValue()
	{
		return mValue;
	}


	@Override
	public ComboBoxProperty setValue(Object aValue)
	{
		mValue = aValue;
		if (mValueComponent != null)
		{
			mValueComponent.setSelectedItem(aValue);
		}
		return this;
	}


	@Override
	protected void updateValue()
	{
		mValue = mValueComponent.getSelectedItem();
	}


	@Override
	void marshal(Bundle aBundle)
	{
		aBundle.putString(mLabel, mValue.toString());
	}


	@Override
	public String toString()
	{
		return ((JComboBox)mValueComponent).getSelectedItem().toString();
	}
}
