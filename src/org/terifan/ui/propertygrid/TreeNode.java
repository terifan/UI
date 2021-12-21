package org.terifan.ui.propertygrid;

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
		mExpanded = true;
		mSelectable = true;

		mValue = aValue;

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
			int rowHeight = getRowHeight(aTree);

			if (mRowBackground != null)
			{
				aGraphics.setColor(mRowBackground);
				aGraphics.fillRect(0, aY, aWidth, rowHeight);
			}
			if (mBackground != null)
			{
				int x = indent * aLevel;
				aGraphics.setColor(mBackground);
				aGraphics.fillRect(x, aY, aWidth - x, rowHeight);
			}

			if (aTree.isPaintHorizontalLines())
			{
				aGraphics.setColor(aTree.getHorizontalLineColor());
				aGraphics.drawLine(0, aY + rowHeight - 1, aWidth, aY + rowHeight - 1);
			}

			for (int i = 0, j = 0; i < aLevel; i++, j++)
			{
				Color color = aTree.getIndentBackgroundColor(i);

				if (color != null)
				{
					aGraphics.setColor(color);
					aGraphics.fillRect(indent * j, aY, indent, rowHeight);
				}
			}

			if (mSelectable && (mRollover || mSelected))
			{
				int x = aTree.isHighlightFullRow() ? 0 : indent * aLevel;
				aGraphics.setColor(aTree.mWindowFocused ? mRollover ? mSelected ? new Color(0x80D1E8FF,true) : new Color(0x80E5F3FB,true) : mSelected ? new Color(0x80CBE8F6,true) : new Color(0xFFFFFF) : mRollover ? mSelected ? new Color(0x80D1E8FF,true) : new Color(0x80E5F3FB,true) : mSelected ? new Color(0x80F7F7F7,true) : new Color(0xFFFFFF));
				aGraphics.fillRect(x, aY, aWidth - x, rowHeight);
				aGraphics.setColor(aTree.mWindowFocused ? mRollover ? mSelected ? new Color(0x8066A7E8,true) : new Color(0x8070C0E7,true) : mSelected ? new Color(0x8026A0DA,true) : new Color(0xFFFFFF) : mRollover ? mSelected ? new Color(0x8066A7E8,true) : new Color(0x8070C0E7,true) : mSelected ? new Color(0x80DEDEDE,true) : new Color(0xFFFFFF));
				aGraphics.drawRect(x, aY, aWidth - x - 1, rowHeight - 1);
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
						aGraphics.drawLine(x0, aY, x0, aY + rowHeight - 1);
					}
				}
			}

			int[] columnWidths = TreeColumnHeader.computeColumnWidths(aTree.getColumns(), aWidth);

			for (int columnIndex = 0, x0 = 0; columnIndex < aTree.getColumns().size(); columnIndex++)
			{
				boolean lastColumn = columnIndex == aTree.getColumns().size() - 1;
				int cw = columnWidths[columnIndex];
				int cx = columnIndex == 0 ? indent * aLevel : x0;

				if (columnIndex == 0)
				{
					if (!mChildren.isEmpty())
					{
						BufferedImage icon = aTree.getIcon(mExpanded);
						aGraphics.drawImage(icon, cx - indent + (indent - icon.getWidth()) / 2, aY + rowHeight / 2 - icon.getHeight() / 2, null);
					}

					Icon icon = mIcon;
					if (icon == null)
					{
						icon = aTree.getFieldValueProvider().getIcon(aTree, mValue);
					}

					if (icon != null)
					{
						cx += aTree.mIconWidth;

						icon.paintIcon(aTree, aGraphics, cx - indent + (indent - icon.getIconWidth()) / 2, aY + (rowHeight - icon.getIconHeight()) / 2);
					}

					cx += aTree.mIconTextSpacing;
				}
				else if (aTree.isPaintVerticalLines())
				{
					aGraphics.setColor(aTree.getVerticalLineColor());
					aGraphics.drawLine(cx, aY, cx, aY + rowHeight - 1);

					cx += aTree.mCellLeftMargin;
				}

				Icon icon = aTree.getFieldValueProvider().getIcon(aTree, columnIndex, mValue);

				if (icon != null)
				{
					cx += aTree.mIconWidth;

					icon.paintIcon(aTree, aGraphics, cx - indent + (indent - icon.getIconWidth()) / 2, aY + (rowHeight - icon.getIconHeight()) / 2);
				}

				String text = aTree.getFieldValueProvider().getText(aTree, columnIndex, mValue);

				new TextBox(text).setForeground(mForeground).setFont(mFont).setBounds(cx, aY, (lastColumn ? aWidth - cx : cw - cx + x0) - aTree.mCellRightMargin, rowHeight).setMaxLineCount(1).setBreakChars(null).setAnchor(Anchor.WEST).render(aGraphics);

				x0 += cw;
			}

			aY += rowHeight;
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
			result.height += getRowHeight(aTree);
		}

		for (int columnIndex = 0, x = 0; columnIndex < aTree.getColumns().size(); columnIndex++)
		{
			Column column = aTree.getColumns().get(columnIndex);
			int w = Math.max(column.getWidth(), column.getMinimumWidth());

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

			aOffsetY.addAndGet(getRowHeight(aTree));
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
