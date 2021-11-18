package org.terifan.ui.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;


public class TreeNode
{
	protected static BufferedImage EXPAND_ICON;
	protected static BufferedImage COLLAPSE_ICON;

	protected String mLabel;
	protected ArrayList<TreeNode> mChildren;
	protected boolean mExpanded;
	protected BufferedImage mIcon;
	protected boolean mRollover;
	protected boolean mSelected;
	protected Integer mRowHeight;


	public TreeNode(String aLabel)
	{
		this(aLabel, null);
	}


	public TreeNode(String aLabel, BufferedImage aIcon)
	{
		mChildren = new ArrayList<>();
		mLabel = aLabel;
		mExpanded = true;
		mIcon = aIcon;

		if (EXPAND_ICON == null)
		{
			try
			{
				BufferedImage icons = ImageIO.read(TreeNode.class.getResource("icons.png"));
				EXPAND_ICON = icons.getSubimage(3 * 16, 0, 16, 16);
				COLLAPSE_ICON = icons.getSubimage(4 * 16, 0, 16, 16);
			}
			catch (IOException e)
			{
				throw new IllegalStateException(e);
			}
		}
	}


	public TreeNode setRowHeight(Integer aRowHeight)
	{
		mRowHeight = aRowHeight;
		return this;
	}


	public TreeNode add(TreeNode aNode)
	{
		mChildren.add(aNode);
		return this;
	}


	protected int paintComponent(Tree aTree, Graphics aGraphics, int aWidth, int aY, int aLevel)
	{
		if (aLevel > 0 || aTree.isPaintRootNode())
		{
			int x = aTree.mIndent * aLevel;

			aGraphics.setColor(aTree.mWindowFocused ? mRollover ? mSelected ? new Color(0xD1E8FF) : new Color(0xE5F3FB) : mSelected ? new Color(0xCBE8F6) : new Color(0xFFFFFF) : mRollover ? mSelected ? new Color(0xD1E8FF) : new Color(0xE5F3FB) : mSelected ? new Color(0xF7F7F7) : new Color(0xFFFFFF));
			aGraphics.fillRect(x, aY, aWidth, getRowHeight(aTree));
			aGraphics.setColor(aTree.mWindowFocused ? mRollover ? mSelected ? new Color(0x66A7E8) : new Color(0x70C0E7) : mSelected ? new Color(0x26A0DA) : new Color(0xFFFFFF) : mRollover ? mSelected ? new Color(0x66A7E8) : new Color(0x70C0E7) : mSelected ? new Color(0xDEDEDE) : new Color(0xFFFFFF));
			aGraphics.drawRect(x, aY, aWidth - 1, getRowHeight(aTree) - 1);

			for (int columnIndex = 0, x0 = 0; columnIndex < aTree.getColumns().size(); columnIndex++)
			{
				int w = aTree.getColumns().get(columnIndex).getWidth();

				int cx = columnIndex == 0 ? 5 + x : x0;

				if (columnIndex == 0 && mIcon != null)
				{
					aGraphics.drawImage(mIcon, cx, aY + (getRowHeight(aTree) - mIcon.getHeight()) / 2, null);

					cx += aTree.mIconWidth;
				}

				aGraphics.setColor(Color.BLACK);
				aGraphics.drawString(mLabel, cx, aY + getRowHeight(aTree) - 5);

				x0 += w;
			}

			if (!mChildren.isEmpty())
			{
				aGraphics.drawImage(mExpanded ? COLLAPSE_ICON : EXPAND_ICON, x - aTree.mIndent / 2 - EXPAND_ICON.getWidth() / 2, aY + getRowHeight(aTree) / 2 - EXPAND_ICON.getWidth() / 2, null);
			}

			if (aTree.isPaintIndentLines())
			{
				aGraphics.setColor(new Color(230, 230, 230));

				for (int i = 0, j = aLevel - (mChildren.isEmpty() ? 0 : 1); i < j; i++)
				{
					int x0 = aTree.mIndent * i + aTree.mIndent / 2;
					int y0 = aY - aTree.mGap / 2;
					int y1 = aY + getRowHeight(aTree) + aTree.mGap - aTree.mGap / 2;
					aGraphics.drawLine(x0, y0, x0, y1);
				}
			}

			aY += getRowHeight(aTree) + aTree.mGap;
		}

		if (mExpanded)
		{
			for (TreeNode node : mChildren)
			{
				aY = node.paintComponent(aTree, aGraphics, aWidth, aY, aLevel + 1);
			}
		}

		return aY;
	}


	protected int getRowHeight(Tree aTree)
	{
		return mRowHeight != null ? mRowHeight : aTree.mRowHeight;
	}


	protected Dimension getPreferredSize(Tree aTree, int aLevel)
	{
		Dimension result = new Dimension(0, 0);

		if (aLevel > 0 || aTree.isPaintRootNode())
		{
			result.height += getRowHeight(aTree) + aTree.mGap;
		}

		for (int columnIndex = 0, x = 0; columnIndex < aTree.getColumns().size(); columnIndex++)
		{
			int w = aTree.getColumns().get(columnIndex).getWidth();

			result.width += w;
		}

		if (mExpanded)
		{
			for (TreeNode node : mChildren)
			{
				Dimension d = node.getPreferredSize(aTree, aLevel + 1);
				result.width = Math.max(result.width, d.width);
				result.height += d.height;
			}
		}

		return result;
	}


	public TreeNode intersect(Tree aTree, MouseEvent aEvent, AtomicInteger aOffsetY, AtomicInteger aLevel)
	{
		if (aTree.isPaintRootNode() || aTree.getRoot() != this)
		{
			if (aEvent.getY() >= aOffsetY.get() && aEvent.getY() < aOffsetY.get() + getRowHeight(aTree))
			{
				return this;
			}

			aOffsetY.addAndGet(getRowHeight(aTree) + aTree.mGap);
		}

		if (mExpanded)
		{
			aLevel.incrementAndGet();

			for (TreeNode node : mChildren)
			{
				TreeNode tmp = node.intersect(aTree, aEvent, aOffsetY, aLevel);

				if (tmp != null)
				{
					return tmp;
				}
			}

			aLevel.decrementAndGet();
		}

		return null;
	}
}
