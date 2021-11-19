package org.terifan.ui.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.Icon;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class TreeNode<T>
{
	protected T mValue;
	protected ArrayList<TreeNode> mChildren;
	protected boolean mExpanded;
	protected boolean mRollover;
	protected boolean mSelected;
	protected boolean mSelectable;
	protected Integer mRowHeight;
	protected Color mForeground;
	protected Color mBackground;
	protected Color mRowBackground;
	protected Font mFont;
	protected Icon mIcon;


	public TreeNode(T aValue)
	{
		mChildren = new ArrayList<>();
		mValue = aValue;
		mExpanded = true;
		mSelectable = true;
		mForeground = Color.BLACK;
		mFont = new Font("arial", Font.PLAIN, 12);
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


	protected int paintComponent(Tree<T> aTree, Graphics aGraphics, int aWidth, int aY, int aLevel)
	{
		if (aLevel > 0 || aTree.isPaintRootNode())
		{
			int indent = aTree.mIndentWidth;
			int x = indent * aLevel;
			int h = getRowHeight(aTree);
			int hg = aTree.mGap / 2;

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

			if (mSelectable && (mRollover || mSelected))
			{
				aGraphics.setColor(aTree.mWindowFocused ? mRollover ? mSelected ? new Color(0xD1E8FF) : new Color(0xE5F3FB) : mSelected ? new Color(0xCBE8F6) : new Color(0xFFFFFF) : mRollover ? mSelected ? new Color(0xD1E8FF) : new Color(0xE5F3FB) : mSelected ? new Color(0xF7F7F7) : new Color(0xFFFFFF));
				aGraphics.fillRect(x, aY + hg, aWidth - x, h);
				aGraphics.setColor(aTree.mWindowFocused ? mRollover ? mSelected ? new Color(0x66A7E8) : new Color(0x70C0E7) : mSelected ? new Color(0x26A0DA) : new Color(0xFFFFFF) : mRollover ? mSelected ? new Color(0x66A7E8) : new Color(0x70C0E7) : mSelected ? new Color(0xDEDEDE) : new Color(0xFFFFFF));
				aGraphics.drawRect(x, aY + hg, aWidth - x - 1, h - 1);
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
					aGraphics.fillRect(indent * j, aY, indent, h + aTree.mGap);
				}
			}

			if (aTree.isPaintIndentLines())
			{
				for (int i = 0, j = 0; i < aLevel; i++, j++)
				{
					Color color = aTree.getIndentLineColor(j);

					if (color != null)
					{
						int x0 = indent * j + indent / 2;

						aGraphics.setColor(color);
						aGraphics.drawLine(x0, aY, x0, aY + h + aTree.mGap - 1);
					}
				}
			}

			for (int columnIndex = 0, x0 = 0; columnIndex < aTree.getColumns().size(); columnIndex++)
			{
				boolean lastColumn = columnIndex == aTree.getColumns().size() - 1;
				int cw = aTree.getColumns().get(columnIndex).getWidth();
				int cx = columnIndex == 0 ? x : x0;

				if (columnIndex == 0)
				{
					if (!mChildren.isEmpty())
					{
						BufferedImage icon = aTree.getIcon(mExpanded);
						aGraphics.drawImage(icon, cx - indent + (indent - icon.getWidth()) / 2, aY + hg + h / 2 - icon.getHeight() / 2, null);
					}

					if (mIcon != null)
					{
						cx += aTree.mIconWidth;

						mIcon.paintIcon(aTree, aGraphics, cx - indent + (indent - mIcon.getIconWidth()) / 2, aY + hg + (h - mIcon.getIconHeight()) / 2);
					}

					cx += aTree.mIconTextSpacing;
				}
				else if (aTree.isPaintVerticalLines())
				{
					aGraphics.setColor(new Color(0xE0EAF9));
					aGraphics.drawLine(cx, aY, cx, aY + aTree.mGap + h - 1);

					cx += aTree.mCellLeftMargin;
				}

				new TextBox(aTree.getFieldValueProvider().getText(aTree, columnIndex, mValue)).setForeground(mForeground).setFont(mFont).setBounds(cx, aY + hg, (lastColumn ? aWidth - cx : cw - cx + x0) - aTree.mCellRightMargin, h).setMaxLineCount(1).setBreakChars(null).setAnchor(Anchor.WEST).render(aGraphics);

				x0 += cw;
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


	protected Dimension getPreferredSize(Tree<T> aTree, int aLevel)
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
