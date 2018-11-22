package org.terifan.ui.propertygrid;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;


public abstract class Property<T extends JComponent,R> implements Comparable<Property>, Cloneable
{
	protected String mLabel;
	protected boolean mCollapsed;
	protected boolean mReadOnly;

	protected transient PropertyGrid mPropertyGrid;
	protected transient PropertyGridIndent mIndentComponent;
	protected transient PropertyGridLabel mLabelComponent;
	protected transient T mValueComponent;
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


	protected Property cloneImpl() throws CloneNotSupportedException
	{
		return (Property)super.clone();
	}


	protected abstract T createValueComponent();


	public abstract R getValue();


	public abstract Property clone() throws CloneNotSupportedException;
}
