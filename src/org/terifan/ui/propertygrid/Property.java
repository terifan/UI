package org.terifan.ui.propertygrid;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;


public class Property implements Comparable<Property>, Cloneable
{
	private PropertyGrid mPropertyGrid;
	private String mLabel;
	private boolean mReadOnly;
	private PropertyGridLabel mLabelComponent;
	private JComponent mValueComponent;
	private Object mUserObject;
	private boolean mCollapsed;
	private boolean mGroup;
	private PropertyGridIndent mIndentComponent;
	private Function<Property, Object> mFunction;
	protected JButton mDetailsButton;


	public Property(String aLabel, Object aValue)
	{
		if (aValue == null || !(aValue instanceof String || aValue instanceof Number || aValue instanceof Boolean || aValue instanceof JTextField || aValue instanceof JCheckBox || aValue instanceof JComboBox || aValue instanceof PropertyList || aValue.getClass().isArray()))
		{
			throw new IllegalArgumentException("Unsupported value: " + aValue);
		}

		mLabel = aLabel;
		mIndentComponent = new PropertyGridIndent(this);

		setValueComponent(aValue);
	}


	public Property setPopupHandler(Function<Property,Object> aFunction)
	{
		mFunction = aFunction;

		updateDetailsButton();

		return this;
	}


	protected void updateDetailsButton()
	{
		if (mFunction == null)
		{
			mDetailsButton = null;
			return;
		}

		mDetailsButton = new JButton(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (mValueComponent instanceof JTextField)
				{
					Object value = mFunction.apply(Property.this);
					if (value != null)
					{
						((JTextField)mValueComponent).setText(value.toString());
					}
				}

				mPropertyGrid.repaint();
			}
		});

		mDetailsButton.setMargin(new Insets(0, 0, 0, 0));
		mDetailsButton.setOpaque(false);
		mDetailsButton.setFocusable(false);
		mDetailsButton.setActionCommand(mLabel);
		mDetailsButton.setText("...");
	}


	public Object getUserObject()
	{
		return mUserObject;
	}


	public Property setUserObject(Object aObject)
	{
		mUserObject = aObject;
		return this;
	}


	public String getLabel()
	{
		return mLabel;
	}


	public Property setLabel(String aLabel)
	{
		mLabel = aLabel;
		return this;
	}


	public boolean isReadOnly()
	{
		return mReadOnly;
	}


	public Property setReadOnly(boolean aReadOnly)
	{
		mReadOnly = aReadOnly;
		return this;
	}


	public boolean isDetailsButtonVisible()
	{
		return mDetailsButton != null;
	}


	public JButton getDetailButton()
	{
		return mDetailsButton;
	}


	public boolean isGroup()
	{
		return mGroup;
	}


	public Property setGroup(boolean aGroup)
	{
		mGroup = aGroup;
		return this;
	}


	public Object getComponentValue()
	{
		return getComponentValue(mValueComponent);
	}


	protected Object getComponentValue(JComponent aComponent)
	{
		if (aComponent instanceof JTextField)
		{
			return ((JTextField)aComponent).getText();
		}
		if (aComponent instanceof JCheckBox)
		{
			return ((JCheckBox)aComponent).isSelected();
		}
		if (aComponent instanceof JComboBox)
		{
			return ((JComboBox)aComponent).getSelectedItem();
		}
		if (aComponent instanceof PropertyList)
		{
			return ((PropertyList)aComponent).getPresentationValue();
		}

		System.out.println("Unsupported component: " + aComponent);

		return null;
	}


	protected void configureValueComponent(JComponent aComponent)
	{
		if (aComponent instanceof JTextField)
		{
			aComponent.setBorder(null);
		}
		else if (aComponent instanceof JCheckBox)
		{
			aComponent.setOpaque(false);
			aComponent.setCursor(Cursor.getDefaultCursor());
			((JCheckBox)aComponent).addActionListener(e -> mPropertyGrid.repaint());
		}
		else if (aComponent instanceof JComboBox)
		{
			aComponent.setOpaque(false);
			aComponent.setCursor(Cursor.getDefaultCursor());
			((JComboBox)aComponent).addActionListener(e -> mPropertyGrid.repaint());

			((JComboBox)aComponent).setUI(new BasicComboBoxUI() {
				@Override
				protected ComboPopup createPopup() {
					BasicComboPopup basicComboPopup = new BasicComboPopup(comboBox);
					basicComboPopup.setBorder(new LineBorder(Color.GRAY));
					return basicComboPopup;
				}
			});
		}
	}


	protected JComponent getValueComponent()
	{
		return mValueComponent;
	}


	public Property setValueComponent(final Object aValue)
	{
		if (aValue instanceof PropertyList)
		{
			PropertyList comp = (PropertyList)aValue;
			comp.bindProperty(this);

			mReadOnly = true;
			mValueComponent = comp;
		}
		else if (aValue instanceof JComponent)
		{
			mValueComponent = (JComponent)aValue;
		}
		else if (aValue instanceof Boolean)
		{
			mValueComponent = new JCheckBox("", (Boolean)aValue);
		}
		else if (aValue.getClass().isArray())
		{
			mValueComponent = new JComboBox((Object[])aValue);
		}
		else
		{
			mValueComponent = new JTextField(aValue.toString());
		}

		configureValueComponent(mValueComponent);

		return this;
	}


	protected JComponent getLabelComponent()
	{
		if (mLabelComponent == null)
		{
			mLabelComponent = new PropertyGridLabel(this);
		}

		return mLabelComponent;
	}


	protected JComponent getIndentComponent()
	{
		return mIndentComponent;
	}


	@Override
	public int hashCode()
	{
		return mLabel == null ? 0 : mLabel.hashCode();
	}


	@Override
	public boolean equals(Object aObject)
	{
		if (aObject instanceof Property)
		{
			Property p = (Property)aObject;
			return p.mLabel == null ? false : p.getLabel().equals(mLabel);
		}
		return false;
	}


	@Override
	public String toString()
	{
		return mLabel;
	}


	@Override
	public int compareTo(Property aProperty)
	{
		return mLabel.compareTo(aProperty.getLabel());
	}


	public boolean getCollapsed()
	{
		return mCollapsed;
	}


	public void setCollapsed(boolean aCollapsed)
	{
		mCollapsed = aCollapsed;
	}


	protected PropertyList getChildren()
	{
		if (mValueComponent instanceof PropertyList)
		{
			return (PropertyList)mValueComponent;
		}
		return new PropertyList("");
	}


	protected void getRecursiveElements(ArrayList<Property> aList)
	{
		if (mValueComponent instanceof PropertyList)
		{
			for (Property item : (PropertyList)mValueComponent)
			{
				aList.add(item);
				if (!item.getCollapsed())
				{
					item.getRecursiveElements(aList);
				}
			}
		}
	}


	protected Integer getIndent(Property aProperty, int aIndent)
	{
		for (Property item : getChildren())
		{
			if (item == aProperty)
			{
				return aIndent;
			}
			Integer i = item.getIndent(aProperty, aIndent + 1);
			if (i != null)
			{
				return i;
			}
		}
		return null;
	}


	public PropertyGrid getPropertyGrid()
	{
		return mPropertyGrid;
	}


	protected void setPropertyGrid(PropertyGrid aPropertyGrid)
	{
		mPropertyGrid = aPropertyGrid;

		mValueComponent.setForeground(mPropertyGrid.getStyleSheet().getColor("text_foreground"));
		mValueComponent.setBackground(mPropertyGrid.getStyleSheet().getColor("text_background"));
	}


	@Override
	protected Property clone() throws CloneNotSupportedException
	{
		Property clone = (Property)super.clone();
		clone.mPropertyGrid = null;
		clone.mLabelComponent = null;
		clone.mValueComponent = null;
		clone.mDetailsButton = clone.getDetailButton();
		clone.mIndentComponent = new PropertyGridIndent(clone);
		clone.mFunction = mFunction;

		clone.updateDetailsButton();

		if (mValueComponent instanceof JTextField)
		{
			JTextField c = (JTextField)mValueComponent;
			clone.setValueComponent(c.getText());
		}
		else if (mValueComponent instanceof JCheckBox)
		{
			JCheckBox c = (JCheckBox)mValueComponent;
			clone.setValueComponent(new JCheckBox(c.getText(), c.isSelected()));
		}
		else if (mValueComponent instanceof JComboBox)
		{
			JComboBox c = (JComboBox)mValueComponent;
			ComboBoxModel model = c.getModel();
			DefaultComboBoxModel modelCopy = new DefaultComboBoxModel();
			for (int i = 0; i < model.getSize(); i++)
			{
				modelCopy.addElement(model.getElementAt(i));
			}
			clone.setValueComponent(new JComboBox(modelCopy));
		}
		else if (mValueComponent instanceof PropertyList)
		{
			PropertyList c = (PropertyList)mValueComponent;
			PropertyList copy = c.clone();
			copy.mProperty = clone;
			clone.setValueComponent(copy);
		}
		return clone;
	}
}
