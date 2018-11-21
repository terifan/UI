package org.terifan.ui.propertygrid;

import javax.swing.JComponent;
import javax.swing.JTextField;


public class TextProperty extends Property
{
	protected Object mValue;


	public TextProperty(String aLabel, Object aValue)
	{
		super(aLabel);

		mValue = aValue;
	}


	@Override
	protected JComponent createValueComponent()
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
	public String toString()
	{
		return ((JTextField)mValueComponent).getText();
	}
}
