package org.terifan.ui.propertygrid;

import java.io.Serializable;
import javax.swing.JTextField;


public class NumberProperty extends Property<JTextField, Number> implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected Number mValue;


	public NumberProperty(String aLabel, Number aValue)
	{
		super(aLabel);

		mValue = aValue;
	}


	@Override
	protected JTextField createValueComponent()
	{
		JTextField c = new JTextField(mValue.toString());
		c.setCaretColor(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.setForeground(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.setBackground(mPropertyGrid.mStyleSheet.getColor("text_background"));
		c.addActionListener(e -> mValueComponent.repaint());
		c.setBorder(null);

		return c;
	}


	@Override
	public Number getValue()
	{
		return mValue;
	}


	@Override
	public NumberProperty setValue(Number aValue)
	{
		mValue = aValue;
		mValueComponent.setText(mValue.toString());
		return this;
	}


	@Override
	public String toString()
	{
		return ((JTextField)mValueComponent).getText();
	}
}
