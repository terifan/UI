package org.terifan.ui.propertygrid;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;


public abstract class Property implements Comparable<Property>, Cloneable
{
	protected String mLabel;
	protected boolean mCollapsed;
	protected boolean mReadOnly;

	protected transient PropertyGrid mPropertyGrid;
	protected transient PropertyGridIndent mIndentComponent;
	protected transient PropertyGridLabel mLabelComponent;
	protected transient JComponent mValueComponent;
	protected transient boolean mGroup;
	protected transient int mIndent;


	public Property(String aLabel)
	{
		mLabel = aLabel;
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


	public JButton getActionButton()
	{
		return null;
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


	public Object getValue()
	{
		if (mValueComponent instanceof JTextField)
		{
			return ((JTextField)mValueComponent).getText();
		}
		if (mValueComponent instanceof JCheckBox)
		{
			return ((JCheckBox)mValueComponent).isSelected();
		}
		if (mValueComponent instanceof JComboBox)
		{
			return ((JComboBox)mValueComponent).getSelectedItem();
		}

		return mValueComponent;
	}


	public boolean isCollapsed()
	{
		return mCollapsed;
	}


	public void setCollapsed(boolean aCollapsed)
	{
		mCollapsed = aCollapsed;
	}


	protected Integer getIndent()
	{
		return mIndent;
	}


	public PropertyGrid getPropertyGrid()
	{
		return mPropertyGrid;
	}


	protected JComponent getValueComponent()
	{
		return mValueComponent;
	}


	protected JComponent getLabelComponent()
	{
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


//	@Override
//	public String toString()
//	{
//		return "{\"" + mLabel + "\": \"" + mValue + "\"}";
//	}


	@Override
	public int compareTo(Property aProperty)
	{
		return mLabel.compareTo(aProperty.getLabel());
	}


	protected void buildItem(PropertyGrid aPropertyGrid, JPanel aPanel, int aIndent)
	{
		mPropertyGrid = aPropertyGrid;
		mIndent = aIndent;

		mIndentComponent = new PropertyGridIndent(this);
		mValueComponent = createValueComponent();
		mLabelComponent = new PropertyGridLabel(this);

		aPanel.add(mIndentComponent);
		aPanel.add(mLabelComponent);

		if (mValueComponent != null)
		{
			aPanel.add(mValueComponent);
			mValueComponent.addFocusListener(new PropertyGridEditorListener(this));
			mValueComponent.setFont(mPropertyGrid.getStyleSheet().getFont("item_font"));
		}

		JButton button = getActionButton();
		if (button != null)
		{
			aPanel.add(button);
		}
	}


	protected abstract JComponent createValueComponent();


//	protected JComponent createValueComponent()
//	{
//		JComponent valueComponent;
//
//		if (this instanceof PropertyList)
//		{
//			valueComponent = (JComponent)mValueComponent;
//		}
//		else if (mValue instanceof JComponent)
//		{
//			valueComponent = (JComponent)mValue;
//		}
//		else if (mValue instanceof Boolean)
//		{
//			valueComponent = new JCheckBox("", (Boolean)mValue);
//		}
//		else if (mValue.getClass().isArray())
//		{
//			valueComponent = new JComboBox((Object[])mValue);
//		}
//		else if (mValue instanceof Function)
//		{
//			valueComponent = new JTextField(((Function<Property,String>)mValue).apply(this));
//		}
//		else if (mValue instanceof PropertyPopup)
//		{
//			valueComponent = ((PropertyPopup)mValue).getComponent();
//		}
//		else
//		{
//			valueComponent = new JTextField(mValue.toString());
//		}
//
//		configureValueComponent(valueComponent);
//
//		return valueComponent;
//	}
//
//
//	protected void configureValueComponent(JComponent aComponent)
//	{
//		StyleSheet styleSheet = mPropertyGrid.getStyleSheet();
//
//		if (aComponent instanceof JTextField)
//		{
//			JTextField c = (JTextField)aComponent;
//
//			c.setBorder(null);
//			c.setCaretColor(styleSheet.getColor("text_foreground"));
//			c.addActionListener(e -> mValueComponent.repaint());
//		}
//		else if (aComponent instanceof JCheckBox)
//		{
//			JCheckBox c = (JCheckBox)aComponent;
//			c.setOpaque(false);
//			c.addActionListener(e -> mValueComponent.repaint());
//		}
//		else if (aComponent instanceof JComboBox)
//		{
//			JComboBox c = (JComboBox)aComponent;
//			c.setOpaque(false);
//			c.setCursor(Cursor.getDefaultCursor());
//			c.addActionListener(e -> mValueComponent.repaint());
//			c.setUI(new BasicComboBoxUI() {
//				@Override
//				protected ComboPopup createPopup() {
//					BasicComboPopup basicComboPopup = new BasicComboPopup(comboBox);
//					basicComboPopup.setBorder(new LineBorder(Color.GRAY));
//					return basicComboPopup;
//				}
//			});
//		}
//
//		aComponent.setForeground(styleSheet.getColor("text_foreground"));
//		aComponent.setBackground(styleSheet.getColor("text_background"));
//	}
//
//
//	@Override
//	protected Property clone() throws CloneNotSupportedException
//	{
//		Property clone = (Property)super.clone();
//		clone.mPropertyGrid = null;
//		clone.mLabelComponent = null;
//		clone.mValueComponent = null;
//		clone.mActionButton = null;
//		clone.mIndentComponent = new PropertyGridIndent(clone);
//
//		if (mValue instanceof ColorChooser)
//		{
//			ColorChooser c = (ColorChooser)mValue;
//			clone.setValue(new ColorChooser(c.getText()));
//		}
//		else if (mValue instanceof Cloneable)
//		{
//			try
//			{
//				System.out.println(mValue.getClass());
//				Method method = mValue.getClass().getMethod("clone");
//				method.setAccessible(true);
//				clone.mValue = method.invoke(mValue);
//			}
//			catch (Exception e)
//			{
//				System.out.println(e);
//			}
//		}
//		else if (mValue instanceof JTextField)
//		{
//			JTextField c = (JTextField)mValue;
//			clone.setValue(c.getText());
//		}
//		else if (mValue instanceof JCheckBox)
//		{
//			JCheckBox c = (JCheckBox)mValue;
//			clone.setValue(new JCheckBox(c.getText(), c.isSelected()));
//		}
//		else if (mValue instanceof JComboBox)
//		{
//			JComboBox c = (JComboBox)mValue;
//			ComboBoxModel model = c.getModel();
//			DefaultComboBoxModel modelCopy = new DefaultComboBoxModel();
//			for (int i = 0; i < model.getSize(); i++)
//			{
//				modelCopy.addElement(model.getElementAt(i));
//			}
//			clone.setValue(new JComboBox(modelCopy));
//		}
//
//		return clone;
//	}
}
