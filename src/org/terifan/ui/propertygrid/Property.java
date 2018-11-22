package org.terifan.ui.propertygrid;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.function.Function;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;


public abstract class Property<T extends JComponent,R> implements Comparable<Property>, Cloneable
{
	protected String mLabel;
	protected boolean mEditable;
	protected Function<Property,R> mFunction;

	protected transient JButton mActionButton;
	protected transient PropertyGrid mPropertyGrid;
	protected transient PropertyGridIndent mIndentComponent;
	protected transient PropertyGridLabel mLabelComponent;
	protected transient T mValueComponent;
	protected transient boolean mGroup;
	protected transient int mIndent;


	public Property(String aLabel)
	{
		mLabel = aLabel;
		mEditable = true;
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


	public boolean isEditable()
	{
		return mEditable;
	}


	public Property<T, R> setEditable(boolean aEditable)
	{
		mEditable = aEditable;
		return this;
	}


	public JButton getActionButton()
	{
		if (mActionButton == null)
		{
			mActionButton = createActionButton();
		}
		return mActionButton;
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


	protected Integer getIndent()
	{
		return mIndent;
	}


	public PropertyGrid getPropertyGrid()
	{
		return mPropertyGrid;
	}


	protected T getValueComponent()
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


	@Override
	public int compareTo(Property aProperty)
	{
		return mLabel.compareTo(aProperty.getLabel());
	}


	protected JButton createActionButton()
	{
		if (mFunction == null)
		{
			return null;
		}

		AbstractAction action = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				R value = mFunction.apply(Property.this);

				if (value != null)
				{
					setValue(value);
					mPropertyGrid.repaint();
				}
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


	protected void buildItem(PropertyGrid aPropertyGrid, JPanel aPanel, int aIndent)
	{
		mPropertyGrid = aPropertyGrid;
		mIndent = aIndent;

		mIndentComponent = new PropertyGridIndent(this);
		mLabelComponent = new PropertyGridLabel(this);

		mValueComponent = createValueComponent();
		mValueComponent.addFocusListener(new PropertyGridEditorListener(this));
		mValueComponent.setFont(mPropertyGrid.getStyleSheet().getFont("item_font"));

		aPanel.add(mIndentComponent);
		aPanel.add(mLabelComponent);
		aPanel.add(mValueComponent);

		JButton button = getActionButton();
		if (button != null)
		{
			aPanel.add(button);
		}
	}


	protected Property cloneImpl() throws CloneNotSupportedException
	{
		Property clone = (Property)super.clone();
		clone.mActionButton = null;

		return clone;
	}


	protected abstract T createValueComponent();


	public abstract R getValue();


	public abstract Property<T,R> setValue(R aValue);


	public abstract Property clone() throws CloneNotSupportedException;
}
