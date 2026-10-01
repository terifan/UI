package org.terifan.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import org.terifan.ui.Orientation;


public class Box extends JComponent implements LayoutManager
{
	private final static long serialVersionUID = 1L;

	private Orientation mOrientation;


	public Box(Orientation aOrientation, Component... aComponents)
	{
		mOrientation = aOrientation;

		setLayout(this);

		for (Component c : aComponents)
		{
			add(c);
		}
	}


	private Dimension layoutSize(Container aTarget, boolean aMinimum)
	{
		int width = 0;
		int height = 0;

		synchronized (aTarget.getTreeLock())
		{
			int n = aTarget.getComponentCount();

			for (int i = 0; i < n; i++)
			{
				Component comp = aTarget.getComponent(i);
				if (comp.isVisible())
				{
					Dimension compDimp = aMinimum ? comp.getMinimumSize() : comp.getPreferredSize();

					if (mOrientation == Orientation.HORIZONTAL)
					{
						width += compDimp.width;
						height = Math.max(height, compDimp.height);
					}
					else
					{
						width = Math.max(width, compDimp.width);
						height += compDimp.height;
					}
				}
			}
		}

		Dimension size = new Dimension(width, height);
		Insets insets = aTarget.getInsets();
		size.width += insets.left + insets.right;
		size.height += insets.top + insets.bottom;
		return size;
	}


	@Override
	public void layoutContainer(Container aTarget)
	{
		Insets insets = aTarget.getInsets();

		synchronized (aTarget.getTreeLock())
		{
			int n = aTarget.getComponentCount();

			for (int i = 0, x = insets.left, y = insets.top; i < n; i++)
			{
				Component comp = aTarget.getComponent(i);

				if (comp.isVisible())
				{
					Dimension compDimp = comp.getPreferredSize();
					int compWidth = compDimp.width;
					int compHeight = compDimp.height;

					comp.setBounds(x, y, compWidth, compHeight);
					comp.revalidate();

					if (mOrientation == Orientation.HORIZONTAL)
					{
						x += compDimp.width;
					}
					else
					{
						y += compDimp.height;
					}
				}
			}
		}
	}


	@Override
	public Dimension minimumLayoutSize(Container parent)
	{
		return layoutSize(parent, true);
	}


	@Override
	public Dimension preferredLayoutSize(Container parent)
	{
		return layoutSize(parent, false);
	}


	@Override
	public void addLayoutComponent(String name, Component comp)
	{
	}


	@Override
	public void removeLayoutComponent(Component comp)
	{
	}
}
