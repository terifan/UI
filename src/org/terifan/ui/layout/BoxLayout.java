package org.terifan.ui.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class BoxLayout implements LayoutManager
{
	public final static int CENTER = 0;
	public final static int RIGHT = 1;
	public final static int LEFT = 2;
	public final static int BOTH = 3;
	public final static int TOP = 1;
	public final static int BOTTOM = 2;
	private int mVGap;
	private int mAlignment;
	private int mAnchor;
	private Insets mInsets;


	public BoxLayout()
	{
		this(0, BOTH, TOP);
	}


	public BoxLayout(int aVGap)
	{
		this(aVGap, BOTH, TOP);
	}


	public BoxLayout(int aVGap, int aAlignment)
	{
		this(aVGap, aAlignment, TOP);
	}


	public BoxLayout(int aVGap, int aAlignment, int aAnchor)
	{
		mVGap = aVGap;
		mAlignment = aAlignment;
		mAnchor = aAnchor;
		mInsets = new Insets(0, 0, 0, 0);
	}


	public BoxLayout setInsets(Insets aInsets)
	{
		this.mInsets = aInsets;
		return this;
	}


	private Dimension layoutSize(Container aParent, boolean aMinimum)
	{
		Dimension total = new Dimension(0, 0);
		Dimension cd;
		synchronized (aParent.getTreeLock())
		{
			int n = aParent.getComponentCount();
			for (int i = 0; i < n; i++)
			{
				Component c = aParent.getComponent(i);
				if (c.isVisible())
				{
					cd = aMinimum ? c.getMinimumSize() : c.getPreferredSize();
					total.width = Math.max(total.width, cd.width + mInsets.left + mInsets.right);
					total.height += cd.height;
					if (i > 0)
					{
						total.height += mVGap;
					}
				}
			}
		}
		Insets insets = aParent.getInsets();
		total.width += insets.left + insets.right;
		total.height += insets.top + insets.bottom + mInsets.top + mInsets.bottom;
		return total;
	}


	@Override
	public void layoutContainer(Container aParent)
	{
		Insets insets = aParent.getInsets();
		synchronized (aParent.getTreeLock())
		{
			int n = aParent.getComponentCount();
			Dimension pd = aParent.getSize();
			int y = 0;

			for (int i = 0; i < n; i++)
			{
				Component c = aParent.getComponent(i);
				Dimension cd = c.getPreferredSize();
				y += cd.height + mVGap;
			}
			y -= mVGap;

			if (mAnchor == TOP)
			{
				y = insets.top + mInsets.top;
			}
			else if (mAnchor == CENTER)
			{
				y = (pd.height - y) / 2;
			}
			else
			{
				y = pd.height - y - insets.bottom - mInsets.bottom;
			}

			for (int i = 0; i < n; i++)
			{
				Component c = aParent.getComponent(i);
				Dimension cd = c.getPreferredSize();
				int x = insets.left + mInsets.left;
				int wid = cd.width;
				int pdwidth = pd.width - mInsets.left - mInsets.right;
				if (mAlignment == CENTER)
				{
					x = (pdwidth - cd.width) / 2;
				}
				else if (mAlignment == RIGHT)
				{
					x = pdwidth - cd.width - insets.right;
				}
				else if (mAlignment == BOTH)
				{
					wid = pdwidth - insets.left - insets.right;
				}
				c.setBounds(x, y, wid, cd.height);
				y += cd.height + mVGap;
			}
		}
	}


	@Override
	public Dimension minimumLayoutSize(Container parent)
	{
		return layoutSize(parent, false);
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


	@Override
	public String toString()
	{
		return getClass().getName() + "[vgap=" + mVGap + " align=" + mAlignment + " anchor=" + mAnchor + "]";
	}


	public static void main(String ... args)
	{
		try
		{
			BoxLayout layout = new BoxLayout();
			JPanel panel = new JPanel(layout);
//			layout.spacing = 5;
//			layout.padx = 5;
//			layout.pady = 5;
//			layout.insets = new Insets(5,5,5,5);
//			layout.border = BorderFactory.createLineBorder(Color.RED, 3);
//			layout.fill = BoxLayout.Fill.EVEN;
//			layout.fill = BoxLayout.Fill.PARENT;
//			layout.fill = BoxLayout.Fill.NONE;
//			layout.anchor = BoxLayout.Anchor.EAST;
//			layout.align = BoxLayout.Align.CENTER;
			panel.add(new JButton("Alpha"));
			panel.add(new JButton("Beta"));
			panel.add(new JButton("Gamma"));

			JFrame frame = new JFrame();
			frame.add(panel);
			frame.setSize(1024, 768);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}
}
