package org.terifan.ui.propertygrid;

import java.io.Serializable;
import javax.swing.JTextField;


public class TextProperty extends Property<JTextField,String> implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected Object mValue;


	public TextProperty(String aLabel, Object aValue)
	{
		super(aLabel);

		mValue = aValue;
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

		return c;
	}


	@Override
	public String getValue()
	{
		return mValueComponent.getText();
	}


	@Override
	public TextProperty setValue(String aValue)
	{
		mValueComponent.setText(aValue);
		return this;
	}


	@Override
	public String toString()
	{
		return ((JTextField)mValueComponent).getText();
	}
}
