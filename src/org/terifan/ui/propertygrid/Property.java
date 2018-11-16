package org.terifan.ui.propertygrid;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.function.Function;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;


public class Property extends JComponent implements Comparable<Property>, Cloneable
{
	protected PropertyGrid mPropertyGrid;
	protected PropertyGridIndent mIndentComponent;
	protected PropertyGridLabel mLabelComponent;
	protected JComponent mValueComponent;
	protected Function<Property, Object> mPopupFunction;
	protected JButton mDetailButton;
	protected boolean mReadOnly;
	protected boolean mCollapsed;
	protected boolean mGroup;
	protected String mLabel;
	protected int mIndent;
	protected Object mValue;


	public Property(String aLabel, Object aValue)
	{
		if (aValue == null || !(aValue instanceof String || aValue instanceof Number || aValue instanceof Boolean || aValue instanceof JTextField || aValue instanceof JCheckBox || aValue instanceof JComboBox || aValue instanceof PropertyList || aValue.getClass().isArray() || aValue instanceof Function))
		{
			throw new IllegalArgumentException("Unsupported value: " + aValue);
		}

		mLabel = aLabel;
		mValue = aValue;
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
		return mDetailButton != null;
	}


	public JButton getDetailButton()
	{
		return mDetailButton;
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


	public void setValue(Object aValue)
	{
		mValue = aValue;
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


	public Property setPopupFunction(Function<Property,Object> aFunction)
	{
		mPopupFunction = aFunction;

		updateDetailsButton();

		return this;
	}


	protected void updateDetailsButton()
	{
		if (mPopupFunction == null)
		{
			mDetailButton = null;
			return;
		}

		mDetailButton = new JButton(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (mValueComponent instanceof JTextField)
				{
					Object value = mPopupFunction.apply(Property.this);
					if (value != null)
					{
						((JTextField)mValueComponent).setText(value.toString());
					}
				}

				mPropertyGrid.repaint();
			}
		});

		mDetailButton.setMargin(new Insets(0, 0, 0, 0));
		mDetailButton.setOpaque(false);
		mDetailButton.setFocusable(false);
		mDetailButton.setActionCommand(mLabel);
		mDetailButton.setText("...");
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
		return "{\"" + mLabel + "\": \"" + mValue + "\"}";
	}


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

		if (mDetailButton != null)
		{
			aPanel.add(mDetailButton);
		}
	}


	protected JComponent createValueComponent()
	{
		JComponent valueComponent;

		if (this instanceof PropertyList)
		{
			valueComponent = (JComponent)this;
		}
		else if (mValue instanceof JComponent)
		{
			valueComponent = (JComponent)mValue;
		}
		else if (mValue instanceof Boolean)
		{
			valueComponent = new JCheckBox("", (Boolean)mValue);
		}
		else if (mValue.getClass().isArray())
		{
			valueComponent = new JComboBox((Object[])mValue);
		}
		else if (mValue instanceof Function)
		{
			valueComponent = new JTextField(((Function<Property,String>)mValue).apply(this));
		}
		else
		{
			valueComponent = new JTextField(mValue.toString());
		}

		configureValueComponent(valueComponent);

		return valueComponent;
	}


	protected void configureValueComponent(JComponent aComponent)
	{
		if (aComponent instanceof JTextField)
		{
			JTextField c = (JTextField)aComponent;

			c.setBorder(null);
			c.setCaretColor(mPropertyGrid.getStyleSheet().getColor("text_foreground"));
			c.addActionListener(e -> repaint());
		}
		else if (aComponent instanceof JCheckBox)
		{
			JCheckBox c = (JCheckBox)aComponent;
			c.setOpaque(false);
			c.addActionListener(e -> repaint());
		}
		else if (aComponent instanceof JComboBox)
		{
			JComboBox c = (JComboBox)aComponent;
			c.setOpaque(false);
			c.setCursor(Cursor.getDefaultCursor());
			c.addActionListener(e -> repaint());
			c.setUI(new BasicComboBoxUI() {
				@Override
				protected ComboPopup createPopup() {
					BasicComboPopup basicComboPopup = new BasicComboPopup(comboBox);
					basicComboPopup.setBorder(new LineBorder(Color.GRAY));
					return basicComboPopup;
				}
			});
		}

		aComponent.setForeground(mPropertyGrid.getStyleSheet().getColor("text_foreground"));
		aComponent.setBackground(mPropertyGrid.getStyleSheet().getColor("text_background"));
	}


	@Override
	protected Property clone() throws CloneNotSupportedException
	{
		Property clone = (Property)super.clone();
		clone.mPropertyGrid = null;
		clone.mLabelComponent = null;
		clone.mValueComponent = null;
		clone.mDetailButton = clone.getDetailButton();
		clone.mIndentComponent = new PropertyGridIndent(clone);
		clone.mPopupFunction = mPopupFunction;

		clone.updateDetailsButton();

		if (mValue instanceof ColorChooser)
		{
			ColorChooser c = (ColorChooser)mValue;
			clone.setValue(new ColorChooser(c.getText()));
		}
		else if (mValue instanceof JTextField)
		{
			JTextField c = (JTextField)mValue;
			clone.setValue(c.getText());
		}
		else if (mValue instanceof JCheckBox)
		{
			JCheckBox c = (JCheckBox)mValue;
			clone.setValue(new JCheckBox(c.getText(), c.isSelected()));
		}
		else if (mValue instanceof JComboBox)
		{
			JComboBox c = (JComboBox)mValue;
			ComboBoxModel model = c.getModel();
			DefaultComboBoxModel modelCopy = new DefaultComboBoxModel();
			for (int i = 0; i < model.getSize(); i++)
			{
				modelCopy.addElement(model.getElementAt(i));
			}
			clone.setValue(new JComboBox(modelCopy));
		}

		return clone;
	}
}
