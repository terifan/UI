package org.terifan.ui.propertygrid;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.function.Function;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;


public class PropertyPopup extends Property<JTextField,Object> implements Cloneable
{
	protected Object mValue;


	public PropertyPopup(String aLabel, Object aValue, Function<Property,Object> aFunction)
	{
		super(aLabel);

		mFunction = aFunction;
		mValue = aValue;

		setEditable(false);
	}


	public void onClick()
	{
		Object value = mFunction.apply(this);

		if (value != null)
		{
			if (mValueComponent instanceof JTextField)
			{
				((JTextField)mValueComponent).setText(value.toString());
			}

			mPropertyGrid.repaint();
		}
	}


	@Override
	public Property clone() throws CloneNotSupportedException
	{
		return cloneImpl();
	}


	@Override
	protected JTextField createValueComponent()
	{
		JTextField c = new JTextField(mValue.toString());
		c.setBorder(null);
		c.setCaretColor(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.addActionListener(e -> mValueComponent.repaint());
		c.setForeground(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.setBackground(mPropertyGrid.mStyleSheet.getColor("text_background"));
		c.setEditable(mEditable);

		return c;
	}


	@Override
	public Object getValue()
	{
		return mValue;
	}


	@Override
	public PropertyPopup setValue(Object aValue)
	{
		mValueComponent.setText(aValue.toString());
		return this;
	}


	@Override
	public String toString()
	{
		return ((JTextField)mValueComponent).getText();
	}
}
