package org.terifan.ui.taginput;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JTextField;


class SimpleLayoutManager implements LayoutManager
{
	private Label mEditingLabel;


	public void setEditingLabel(Label aEditingLabel)
	{
		mEditingLabel = aEditingLabel;
	}


	@Override
	public void addLayoutComponent(String aName, Component aComp)
	{
	}


	@Override
	public void removeLayoutComponent(Component aComp)
	{
	}


	@Override
	public Dimension minimumLayoutSize(Container aTarget)
	{
		return preferredLayoutSize(aTarget);
	}


	@Override
	public Dimension preferredLayoutSize(Container aTarget)
	{
		synchronized (aTarget.getTreeLock())
		{
			int width = 0;
			int height = 0;

			int n = aTarget.getComponentCount();
			for (int i = 0; i < n; i++)
			{
				Component comp = aTarget.getComponent(i);
				if (mEditingLabel != null && comp instanceof JTextField)
				{
					continue;
				}
				Dimension d = comp.getPreferredSize();
				width += d.width + 4;
				height = Math.max(height, d.height);
			}
			if (width > 0)
			{
				width -= 4;
			}

			Insets insets = aTarget.getInsets();
			width += insets.left + insets.right;
			height += insets.top + insets.bottom;
			return new Dimension(width, height);
		}
	}


	@Override
	public void layoutContainer(Container aTarget)
	{
		synchronized (aTarget.getTreeLock())
		{
			Insets insets = aTarget.getInsets();
			int x = insets.left;
			int n = aTarget.getComponentCount();
			JTextField textField = null;
			for (int i = 0; i < n; i++)
			{
				Component comp = aTarget.getComponent(i);
				if (comp instanceof JTextField v)
				{
					textField = v;
					break;
				}
			}
			for (int i = 0; i < n; i++)
			{
				Component comp = aTarget.getComponent(i);
				if (comp instanceof Label)
				{
					Dimension d = comp.getPreferredSize();
					if (comp == mEditingLabel)
					{
						comp.setVisible(false);
						comp = textField;
					}
					comp.setVisible(true);
					comp.setBounds(x, insets.top, d.width, d.height);
					x += d.width + 4;
				}
			}
			if (mEditingLabel == null)
			{
				Dimension d = textField.getPreferredSize();
				textField.setBounds(x, insets.top, d.width, d.height);
			}
		}
	}
}
