package org.terifan.ui.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;


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
	protected boolean mSelectable;
	protected Integer mRowHeight;
	protected Color mForeground;
	protected Color mBackground;
	protected Color mRowBackground;
	protected Font mFont;


	public TreeNode(String aLabel)
	{
		this(aLabel, null);
	}


	public TreeNode(String aLabel, BufferedImage aIcon)
	{
		mChildren = new ArrayList<>();
		mLabel = aLabel;
		mExpanded = true;
		mSelectable = true;
		mIcon = aIcon;
		mForeground = Color.BLACK;
		mFont = new Font("arial", Font.PLAIN, 12);

		if (EXPAND_ICON == null)
		{
			try
			{
				BufferedImage icons = ImageIO.read(TreeNode.class.getResource("icons.png"));
				EXPAND_ICON = icons.getSubimage(10 * 16, 0, 16, 16);
				COLLAPSE_ICON = icons.getSubimage(11 * 16, 0, 16, 16);
//				EXPAND_ICON = icons.getSubimage(3 * 16, 0, 16, 16);
//				COLLAPSE_ICON = icons.getSubimage(4 * 16, 0, 16, 16);
			}
			catch (IOException e)
			{
				throw new IllegalStateException(e);
			}
		}
	}


	public boolean isSelectable()
	{
		return mSelectable;
	}


	public TreeNode setSelectable(boolean aSelectable)
	{
		mSelectable = aSelectable;
		return this;
	}


	public Color getForeground()
	{
		return mForeground;
	}


	public TreeNode setForeground(Color aColor)
	{
		mForeground = aColor;
		return this;
	}


	public Color getBackground()
	{
		return mBackground;
	}


	public TreeNode setBackground(Color aColor)
	{
		mBackground = aColor;
		return this;
	}


	public Color getRowBackground()
	{
		return mRowBackground;
	}


	public TreeNode setRowBackground(Color aColor)
	{
		mRowBackground = aColor;
		return this;
	}


	public Integer getRowHeight()
	{
		return mRowHeight;
	}


	public TreeNode setRowHeight(Integer aRowHeight)
	{
		mRowHeight = aRowHeight;
		return this;
	}


	public Font getFont()
	{
		return mFont;
	}


	public TreeNode setFont(Font aFont)
	{
		mFont = aFont;
		return this;
	}


	public TreeNode add(TreeNode aNode)
	{
		mChildren.add(aNode);
		return this;
	}


	public ArrayList<TreeNode> getChildren()
	{
		return mChildren;
	}


	protected int paintComponent(Tree aTree, Graphics aGraphics, int aWidth, int aY, int aLevel)
	{
		if (aLevel > 0 || aTree.isPaintRootNode())
		{
			int indent = aTree.mIndentWidth;
			int x = indent * aLevel;
			int h = getRowHeight(aTree);
			int adjustX = EXPAND_ICON.getWidth() / 2;

			if (mRowBackground != null)
			{
				aGraphics.setColor(mRowBackground);
				aGraphics.fillRect(0, aY, aWidth, h + aTree.mGap);
			}
			if (mBackground != null)
			{
				aGraphics.setColor(mBackground);
				aGraphics.fillRect(x, aY, aWidth - x, h + aTree.mGap);
			}

			if (aTree.isPaintIndentLines())
			{
				for (int i = 0, j = 0; i < aLevel; i++, j++)
				{
					int x0 = indent * j + adjustX;
					int y0 = aY - aTree.mGap / 2;
					int y1 = aY + h + aTree.mGap - aTree.mGap / 2;

					aGraphics.setColor(new Color(240, 240, 240));
					aGraphics.drawLine(x0, y0, x0, y1);
				}
			}

			if (aTree.isPaintHorizontalLines())
			{
				aGraphics.setColor(new Color(0xE0EAF9));
				aGraphics.drawLine(0, aY + h + aTree.mGap - 1, aWidth, aY + h + aTree.mGap - 1);
			}

			for (int i = 0, j = 0; i < aLevel; i++, j++)
			{
				Color color = aTree.getIndentBackgroundColor(i);

				if (color != null)
				{
					aGraphics.setColor(color);
					aGraphics.fillRect(indent * j, aY - aTree.mGap / 2, indent, h + aTree.mGap);
				}
			}

			if (mSelectable && (mRollover || mSelected))
			{
				aGraphics.setColor(aTree.mWindowFocused ? mRollover ? mSelected ? new Color(0xD1E8FF) : new Color(0xE5F3FB) : mSelected ? new Color(0xCBE8F6) : new Color(0xFFFFFF) : mRollover ? mSelected ? new Color(0xD1E8FF) : new Color(0xE5F3FB) : mSelected ? new Color(0xF7F7F7) : new Color(0xFFFFFF));
				aGraphics.fillRect(x, aY + aTree.mGap / 2, aWidth - x, h);
				aGraphics.setColor(aTree.mWindowFocused ? mRollover ? mSelected ? new Color(0x66A7E8) : new Color(0x70C0E7) : mSelected ? new Color(0x26A0DA) : new Color(0xFFFFFF) : mRollover ? mSelected ? new Color(0x66A7E8) : new Color(0x70C0E7) : mSelected ? new Color(0xDEDEDE) : new Color(0xFFFFFF));
				aGraphics.drawRect(x, aY + aTree.mGap / 2, aWidth - x - 1, h - 1);
			}

			for (int columnIndex = 0, x0 = 0; columnIndex < aTree.getColumns().size(); columnIndex++)
			{
				int w = aTree.getColumns().get(columnIndex).getWidth();

				int cx = columnIndex == 0 ? x : x0;

				if (columnIndex == 0)
				{
					if (!mChildren.isEmpty())
					{
						aGraphics.drawImage(mExpanded ? COLLAPSE_ICON : EXPAND_ICON, cx - aTree.mExpandWidth, aY + h / 2 - EXPAND_ICON.getHeight() / 2, null);
					}

					if (mIcon != null)
					{
						cx += aTree.mIconWidth;

						aGraphics.drawImage(mIcon, cx - mIcon.getWidth(), aY + (h - mIcon.getHeight()) / 2, null);
					}

					cx += aTree.mIconTextSpacing;
				}
				else if (aTree.isPaintVerticalLines())
				{
					aGraphics.setColor(new Color(0xE0EAF9));
					aGraphics.drawLine(cx, aY, cx, aY + aTree.mGap + h - 1);

					cx += 5;
				}

				aGraphics.setColor(mForeground);
				aGraphics.setFont(mFont);
				aGraphics.drawString(mLabel, cx, aY + aTree.mGap / 2 + h / 2 + 5);

				x0 += w;
			}

			aY += h + aTree.mGap;
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
		return mRowHeight == null ? aTree.mRowHeight : mRowHeight;
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
			if (aEvent.getY() >= aOffsetY.get() + aTree.mGap / 2 && aEvent.getY() < aOffsetY.get() + aTree.mGap / 2 + getRowHeight(aTree))
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
