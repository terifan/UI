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
	protected JButton mActionButton;
	protected Function<Property,Object> mFunction;
	protected Object mValue;


	public PropertyPopup(String aLabel, Object aValue, Function<Property,Object> aFunction)
	{
		super(aLabel);

		mFunction = aFunction;
		mValue = aValue;

		setEditable(false);
	}


	public PropertyPopup setEditable(boolean aState)
	{
		if (mValueComponent instanceof JTextField)
		{
			((JTextField)mValueComponent).setEditable(aState);
		}
		return this;
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


//	@Override
//	public PropertyPopup clone() throws CloneNotSupportedException
//	{
//		PropertyPopup clone = new PropertyPopup(null, mFunction); //PropertyPopup)super.clone();
//		if (mValueComponent instanceof JTextField)
//		{
//			clone.mValueComponent = new JTextField(((JTextField)mValueComponent).getText());
//		}
//		else
//		{
//			clone.mValueComponent = new JLabel("");
//		}
//		System.out.println("*");
//		return clone;
//	}


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
	public Object getValue()
	{
		return mValue;
	}


	@Override
	public JButton getActionButton()
	{
		if (mActionButton == null)
		{
			mActionButton = createPopupButton();
		}
		return mActionButton;
	}


	protected JButton createPopupButton()
	{
		AbstractAction action = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onClick();
			}
		};

		JButton button = new JButton(action);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setOpaque(false);
		button.setFocusable(false);
		button.setIcon(new ImageIcon(mPropertyGrid.getStyleSheet().getImage("popup_icon")));

		mActionButton = button;

		return button;
	}


	@Override
	public String toString()
	{
		return ((JTextField)mValueComponent).getText();
	}
}
