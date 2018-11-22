package org.terifan.ui.propertygrid;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.function.Function;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;


public abstract class Property<T extends JComponent,R> implements Comparable<Property>, Cloneable, Serializable
{
	private static final long serialVersionUID = 1L;

	protected String mLabel;
	protected boolean mEditable;
	protected boolean mHasFunction; // this exists in order for the model to recreate functions when being deserisalized

	protected transient Function<Property,R> mFunction;
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


	public Function<Property, R> getFunction()
	{
		return mFunction;
	}


	public Property<T, R> setFunction(Function<Property, R> aFunction)
	{
		mFunction = aFunction;
		mHasFunction = mFunction != null;
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

		if (mHasFunction && mFunction == null)
		{
			Function<Property, Function<Property, Object>> factory = mPropertyGrid.getFunctionFactory();
			if (factory != null)
			{
				mFunction = (Function<Property,R>)factory.apply(this);
				mHasFunction = mFunction != null;
			}
		}

		aPanel.add(mIndentComponent);
		aPanel.add(mLabelComponent);
		aPanel.add(mValueComponent);

		JButton button = getActionButton();
		if (button != null)
		{
			aPanel.add(button);
		}
	}


	protected Property clone() throws CloneNotSupportedException
	{
		Property clone = (Property)super.clone();
		clone.mActionButton = null;

		return clone;
	}


	protected abstract T createValueComponent();


	public abstract R getValue();


	public abstract Property<T,R> setValue(R aValue);
}
